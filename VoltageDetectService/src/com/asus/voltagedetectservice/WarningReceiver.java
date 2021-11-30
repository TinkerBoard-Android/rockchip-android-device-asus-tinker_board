package com.asus.voltagedetectservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WarningReceiver extends BroadcastReceiver {
    private final String TAG = "VoltageDetectService";
    String channelId = "default_channel_id";
    String channelDescription = "Default Channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "WarningReceiver onReceive");
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context, channelId);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, new Intent(context, WarningActivity.class), 0);

        builder.setContentTitle("Low Voltage");
        builder.setContentText("Click to show more");
        builder.setSmallIcon(R.drawable.iconplug);
        builder.setContentIntent(pIntent);
        builder.setAutoCancel(true);
        Notification notification = builder.getNotification();

        //Check if notification channel exists and if not create one
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = nManager.getNotificationChannel(channelId);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelId, channelDescription, importance);
                notificationChannel.enableVibration(true);
                nManager.createNotificationChannel(notificationChannel);
            }
        }

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        nManager.notify(1, notification);
    }
}
