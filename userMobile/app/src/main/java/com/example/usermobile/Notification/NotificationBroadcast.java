package com.example.usermobile.Notification;

import static com.example.usermobile.Notification.CustomNotificationManager.CHANNEL_ID;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.usermobile.R;
import com.example.usermobile.Storage.Storage;
import com.example.usermobile.Storage.StorageListView;

public class NotificationBroadcast  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", 100);
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");

        sendNotification(context, id, title, text);
    }

    private void sendNotification(Context context, int id, String title, String text) {
        Intent resultIntent = new Intent(context, StorageListView.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());
    }
}
