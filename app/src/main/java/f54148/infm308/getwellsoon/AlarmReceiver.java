package f54148.infm308.getwellsoon;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "CHANNEL_SAMPLE";

    @Override
    public void onReceive(Context context, Intent intent) {

        //Get information from intent
        int notificationId = intent.getIntExtra("notificationId", 0);
        String message = intent.getStringExtra("message");
        int currentMedicineIntake = intent.getIntExtra("currentMedicineIntake", 0);
        int totalIntakesADay = intent.getIntExtra("totalIntakesADay", 0);


        //call MainActivity when notification is tapped.
        Intent mainIntent = new Intent(context, AddMedicineActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, FLAG_IMMUTABLE);

        //notificationManager
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For API 26 and above
            CharSequence channelName = "Get Well Soon Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        //prepare Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.pills)
                .setContentTitle(message)
                .setContentText("Доза " + currentMedicineIntake + " от " + totalIntakesADay + " за деня.")
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);


        //notify
        notificationManager.notify(notificationId, builder.build());
    }
}
