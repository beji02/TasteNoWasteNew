package com.example.usermobile.Notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

public class CustomNotificationManager extends AppCompatActivity {
    static String CHANNEL_ID = "channel 2";
    public static boolean NOTIFY_ON = true;

    Context context;
    public CustomNotificationManager(Context context) {
        this.context = context;
        createNotificationChannel();
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    android.app.NotificationManager.IMPORTANCE_HIGH
            );

            android.app.NotificationManager manager = context.getSystemService(android.app.NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    public void sendNotification(CustomNotification notification) {
        if (NOTIFY_ON == false)
            return;
        Intent intent = new Intent(context, NotificationBroadcast.class);
        intent.putExtra("id", notification.id);
        intent.putExtra("title", notification.title);
        intent.putExtra("text", notification.text);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notification.id, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notification.timeToRing, pendingIntent);
    }
}
