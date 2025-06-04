package com.example.alarmapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

public class StopAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("ACTION_STOP_ALARM".equals(intent.getAction())) {
            if (AlarmReceiver.mediaPlayer != null && AlarmReceiver.mediaPlayer.isPlaying()) {
                AlarmReceiver.mediaPlayer.stop();
                AlarmReceiver.mediaPlayer.release();
                AlarmReceiver.mediaPlayer = null;
            }

            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.cancel();
            }

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);
        }
    }
}

