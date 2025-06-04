package com.example.alarmapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    public static MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        String label = intent.getStringExtra("label");

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmSound == null) {
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        mediaPlayer = MediaPlayer.create(context, alarmSound);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            long[] pattern = {0, 1000, 1000};
            vibrator.vibrate(pattern, 0);
        }

        showNotification(context,label);
    }
    private void showNotification(Context context, String label) {
        String CHANNEL_ID = "alarm_channel";

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create notification channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Alarm Channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel for alarm notifications");
            notificationManager.createNotificationChannel(channel);
        }

        Intent stopIntent = new Intent(context, StopAlarmReceiver.class);
        stopIntent.setAction("ACTION_STOP_ALARM");

        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("Alarm ringing")
                .setContentText(label)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_alarm, "Stop", stopPendingIntent)
                .setOngoing(true);

        notificationManager.notify(1, builder.build());
    }
}
