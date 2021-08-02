package com.barbera.barberaserviceapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import static com.barbera.barberaserviceapp.ServiceApplication.ID;

public class ScheduleService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent scheduleIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, scheduleIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, ID)
                .setContentTitle("Schedule")
                .setContentText("Checking your schedule")
                .setSmallIcon(R.drawable.ic_baseline_home_24)
                .setContentIntent(pendingIntent)
                .build();
        PeriodicWorkRequest periodicWorkRequest= new PeriodicWorkRequest.Builder(
                ScheduleWork.class,1, TimeUnit.HOURS
        ).build();
        WorkManager.getInstance().enqueue(periodicWorkRequest);

        startForeground(2, notification);
        //do heavy work on a background thread
        return START_NOT_STICKY;
    }
}
