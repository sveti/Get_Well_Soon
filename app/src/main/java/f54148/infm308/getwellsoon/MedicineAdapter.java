package f54148.infm308.getwellsoon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedicineAdapter extends ArrayAdapter<Medicine> {

    private AlarmManager alarmManager;
    SQLightManager sqLightManager;
    Context context;

    public MedicineAdapter(Context context, List<Medicine> medicines) {
        super(context, 0, medicines);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        sqLightManager = SQLightManager.instanceOfDatabase(context);
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Medicine medicine = getItem(position);

        if (convertView == null) {
            ///set View if Not set
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.medicine_cell, parent, false);
        }

        ///bind elements
        TextView medicineTitle = convertView.findViewById(R.id.medicineCellTitle);
        TextView medicineTimesADay = convertView.findViewById(R.id.medicineCellTimesADay);
        TextView medicineDays = convertView.findViewById(R.id.medicineCellDays);

        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);
        AddMedicineActivity addMedicineActivity = new AddMedicineActivity();


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int totalNumberOfNotifications = medicine.getTimesADay() * medicine.getDays();
                ArrayList<Integer> intentIds = new ArrayList<>();
                String[] parts = medicine.getPendingIntentsIdsString().split("=");

                for (String part : parts) {
                    intentIds.add(Integer.valueOf(part));
                }

                for (int i = 0; i < totalNumberOfNotifications; i++) {
                    Intent intentForCancelling = new Intent(context, AlarmReceiver.class);
                    intentForCancelling.putExtra("notificationId", intentIds.get(i));
                    intentForCancelling.putExtra("message", medicine.getName());
                    intentForCancelling.putExtra("currentMedicineIntake", i % medicine.getTimesADay() + 1);
                    intentForCancelling.putExtra("totalIntakesADay", medicine.getTimesADay());
                    ///every PendingIntent should have a different requestCode (second parameter) so the notifications don't overwrite
                    PendingIntent alarmIntent = PendingIntent.getBroadcast(
                            context, intentIds.get(i), intentForCancelling, PendingIntent.FLAG_IMMUTABLE
                    );
                    // alarmIntent.cancel();
                    alarmManager.cancel(alarmIntent);

                }
                medicine.setDeleted(new Date());
                medicine.setPendingIntentsIdsString("");
                sqLightManager.deleteMedicineFromDB(medicine);
                Toast.makeText(context, R.string.medicineDeleted, Toast.LENGTH_SHORT).show();

                Intent homepage = new Intent(context, MainActivity.class);
                homepage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(homepage);
            }
        });

        ///set content
        medicineTitle.setText(medicine.getName());
        if (medicine.isMultiple()) {
            String displayMessage = medicine.getTimesADay() + " пъти на ден през " + medicine.getHoursBetweenIntakes() + " часа";
            medicineTimesADay.setText(displayMessage);
        } else {
            medicineTimesADay.setVisibility(View.GONE);
        }
        String displayMessageDays = medicine.getDays() + (medicine.getDays() == 1 ? "ден" : " дни") + " общо";
        medicineDays.setText(displayMessageDays);

        return convertView;
    }


}
