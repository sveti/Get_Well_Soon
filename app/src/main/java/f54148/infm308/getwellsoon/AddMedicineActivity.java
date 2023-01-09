package f54148.infm308.getwellsoon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;

public class AddMedicineActivity extends AppCompatActivity {

    private SQLightManager sqLightManager = SQLightManager.instanceOfDatabase(this);
    private SwitchCompat multipleTimesADaySwitch;
    private RelativeLayout multipleTimesADayOptions;
    private Button submitNewMedicineButton;
    private EditText newMedicineName;
    private EditText medicineTimeInterval;
    private EditText medicineTimesADay;
    private EditText medicineDays;
    private TimePicker timePicker;
    private boolean multipleTimesADay = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //disable dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //remove title
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_add_medicine);

        initFields();

    }

    private void initFields() {
        //bind switch to element and event handler
        multipleTimesADaySwitch = findViewById(R.id.multiple);
        multipleTimesADaySwitch.setOnCheckedChangeListener(toggleMultipleTimesADay);
        multipleTimesADayOptions = findViewById(R.id.multipleIntakesOptions);

        //bind submit button to handler
        submitNewMedicineButton = findViewById(R.id.submitNewMedicine);
        submitNewMedicineButton.setOnClickListener(newMedicineSubmitHandler);

        //bind medicine info
        newMedicineName = findViewById(R.id.medicineName);
        medicineTimeInterval = findViewById(R.id.medicineTimeInterval);
        medicineTimesADay = findViewById(R.id.medicineTimesADay);
        timePicker = findViewById(R.id.medicineStartTime);
        medicineDays = findViewById(R.id.medicineDays);
    }


    private View.OnClickListener newMedicineSubmitHandler = new View.OnClickListener() {
        public void onClick(View v) {

            int totalIntakesADay = Helpers.extractFomEditTextOrUseDefault(medicineTimesADay, multipleTimesADay ? 2 : 1);
            int hoursBetweenIntakes = Helpers.extractFomEditTextOrUseDefault(medicineTimeInterval, multipleTimesADay ? 2 : 0);
            int days = Helpers.extractFomEditTextOrUseDefault(medicineDays, 3);

            ///cleanup of invalid data
            if (hoursBetweenIntakes * totalIntakesADay > 24) {
                Toast.makeText(AddMedicineActivity.this, R.string.invalidData, Toast.LENGTH_SHORT).show();
                return;
            }
            if (newMedicineName.getText().toString().equals("")) {
                Toast.makeText(AddMedicineActivity.this, R.string.invalidName, Toast.LENGTH_SHORT).show();
                return;
            }

            /// calculate total number of notifications that need to be set
            /// 4 days * 3 pills a day = 12 notifications
            int totalNumberOfNotifications = totalIntakesADay * days;

            //setup alarm manager
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            int hour = timePicker.getCurrentHour();
            int minutes = timePicker.getCurrentMinute();

            ///Lets assume 4 days
            // 3 pills a day
            ///start time : 8
            ///time between intakes: 4 hours

            ///index: 0 time: 8 + 0*4 day: 0
            ///index: 1 time: 8 + 1*4 day: 0
            ///index: 2 time: 8 + 2*4 day: 0
            ///index: 3 time: 8 + 0*4 day: 1
            ///index: 4 time: 8 + 1*4 day: 1
            ///index: 5 time: 8 + 2*4 day: 1

            ///conclusion:
            // time for notification:start time + index % time between intakes * hoursBetweenIntakes
            ///day: index / pills a day

            Calendar[] startTimes = new Calendar[totalNumberOfNotifications];
            //for debugging
            StringBuilder allAllTimes = new StringBuilder();

            StringBuilder pendingIntents = new StringBuilder();
            //updating db is slow
            //doing it before actually using the values to give it enough reaction time
            int notificationId = sqLightManager.getNextNotificationId();
            sqLightManager.updateNextNotificationId(notificationId, notificationId + totalNumberOfNotifications);

            for (int i = 0; i < totalNumberOfNotifications; i++) {
                startTimes[i] = Calendar.getInstance();

                ///add days
                //startTimes[i].add(Calendar.DATE,i / totalIntakesADay);
                ///add times
                //startTimes[i].set(Calendar.HOUR_OF_DAY, hour + (i % totalIntakesADay) * hoursBetweenIntakes);

                ///for debugging
                allAllTimes.append(hour + (i % totalIntakesADay) * hoursBetweenIntakes).append(" =day=").append(i / totalIntakesADay).append("\n");

                startTimes[i].set(Calendar.HOUR_OF_DAY, hour);
                //startTimes[i].set(Calendar.MINUTE, minutes);
                //for demo purposes
                startTimes[i].set(Calendar.MINUTE, minutes + i);
                startTimes[i].set(Calendar.SECOND, 0);

                ///create intent
                Intent intent = new Intent(AddMedicineActivity.this, AlarmReceiver.class);
                intent.putExtra("notificationId", notificationId);
                intent.putExtra("message", newMedicineName.getText().toString());
                intent.putExtra("currentMedicineIntake", i % totalIntakesADay + 1);
                intent.putExtra("totalIntakesADay", totalIntakesADay);
                ///every PendingIntent should have a different requestCode (second parameter) so the notifications don't overwrite
                PendingIntent alarmIntent = PendingIntent.getBroadcast(
                        AddMedicineActivity.this, notificationId, intent, PendingIntent.FLAG_IMMUTABLE
                );

                pendingIntents.append(notificationId);
                pendingIntents.append("=");

                notificationId++;

                alarmManager.set(AlarmManager.RTC_WAKEUP, startTimes[i].getTimeInMillis(), alarmIntent);
            }
            //for debugging
//            System.out.println(allAllTimes.toString());

            saveMedicine(newMedicineName.getText().toString(), totalIntakesADay, hoursBetweenIntakes, days, pendingIntents.toString());

            Toast.makeText(AddMedicineActivity.this, R.string.notificationSet, Toast.LENGTH_SHORT).show();
        }
    };

    private void saveMedicine(String name, int totalIntakesADay, int hoursBetweenIntakes, int days, String pendingIntentsString) {
        Medicine medicine = new Medicine(name, days, totalIntakesADay, hoursBetweenIntakes, pendingIntentsString, multipleTimesADay);
        SQLightManager sqLightManager = SQLightManager.instanceOfDatabase(this);
        sqLightManager.saveMedicineInDB(medicine);
    }

    private final CompoundButton.OnCheckedChangeListener toggleMultipleTimesADay = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            //if pill is being taken multiple times a day show the extra panel
            if (isChecked) {
                multipleTimesADayOptions.setVisibility(View.VISIBLE);
                multipleTimesADaySwitch.setText(R.string.yesText);
                multipleTimesADay = true;
            } else {
                //hide the extra panel
                multipleTimesADayOptions.setVisibility(View.GONE);
                multipleTimesADaySwitch.setText(R.string.noText);
                multipleTimesADay = false;
            }
        }
    };

    public AddMedicineActivity getPackageContext() {
        return AddMedicineActivity.this;
    }
}