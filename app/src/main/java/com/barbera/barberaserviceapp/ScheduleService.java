package com.barbera.barberaserviceapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.barbera.barberaserviceapp.ServiceApplication.ID;

public class ScheduleService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    @Override
//    public boolean onStartJob(JobParameters params) {
//        return false;
//    }
//
//    @Override
//    public boolean onStopJob(JobParameters params) {
//        return false;
//    }

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
        Calendar rn=Calendar.getInstance();
        int min=rn.get(Calendar.MINUTE);
        int rem=60-min;

        OneTimeWorkRequest oneTimeWorkRequest= new OneTimeWorkRequest.Builder(ScheduleWork.class)
                .setInitialDelay(rem, TimeUnit.MINUTES).build();
        WorkManager.getInstance().enqueue(oneTimeWorkRequest);

        startForeground(2, notification);
        //do heavy work on a background thread
        return START_NOT_STICKY;
    }
}
