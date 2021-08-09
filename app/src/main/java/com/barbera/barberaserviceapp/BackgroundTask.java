package com.barbera.barberaserviceapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class BackgroundTask extends Worker {
    private static int notificationId=220;
    public static final String ID ="live location";
    public static final String SID="schedule 1 hour";
    public BackgroundTask(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        runLongLoop();
        return Result.success();
    }

    public void runLongLoop() {
        Thread thread = new Thread() {
            public void run() {
                Looper.prepare();
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RunTimer();
                        runLongLoop();
                    }
                }, 300000);
                Looper.loop();
            }
        };
        thread.start();
    }

    public void RunTimer() {
//        new CountDownTimer(9000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                Log.d("ANAM", "seconds remaining:  " + millisUntilFinished);
//            }
//
//            public void onFinish() {
//                Log.d("ANAM", "Finished the timer");
//            }
//
//        }.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel scheduleChannel = new NotificationChannel(
                    SID,
                    "Schedule Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager1 = getApplicationContext().getSystemService(NotificationManager.class);
            manager1.createNotificationChannel(scheduleChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), SID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Service")
                .setContentText("You have a booking in 1 hour")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("Schedule", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Calendar rn=Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        String date = dateFormat.format(rn.getTime());
        Log.d("sch",date);
        //date="11";
        if(date.equals("05")){
            if(sharedPreferences.getString("6","").equals("b")){
                editor.putString("6","n");
                editor.putString("prev","06");
                editor.apply();
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("06")){
            if(sharedPreferences.getString("7","").equals("b")){
                editor.putString("7","n");
                editor.putString("prev","07");
                editor.apply();
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("07")){
            if(sharedPreferences.getString("8","").equals("b")){
                editor.putString("8","n");
                editor.putString("prev","08");
                editor.apply();
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("08")){
            if(sharedPreferences.getString("9","").equals("b")){
                editor.putString("9","n");
                editor.putString("prev","09");
                editor.apply();
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("09")){
            if(sharedPreferences.getString("10","").equals("b")){
                editor.putString("10","n");
                editor.putString("prev","10");
                editor.apply();
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("10")){
            if(sharedPreferences.getString("11","").equals("b")){
                editor.putString("11","n");
                editor.putString("prev","11");
                editor.apply();
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("11")){
            if(sharedPreferences.getString("12","").equals("b")){
                editor.putString("12","n");
                editor.putString("prev","12");
                editor.apply();
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("12")){
            if(sharedPreferences.getString("13","").equals("b")){
                editor.putString("13","n");
                editor.putString("prev","13");
                editor.apply();
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("13")){
            if(sharedPreferences.getString("14","").equals("b")){
                editor.putString("14","n");
                editor.putString("prev","14");
                editor.apply();
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("14")){
            if(sharedPreferences.getString("15","").equals("b")){
                editor.putString("15","n");
                editor.putString("prev","15");
                editor.apply();
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("15")){
            if(sharedPreferences.getString("16","").equals("b")){
                editor.putString("16","n");
                editor.putString("prev","16");
                editor.apply();
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("16")){
            if(sharedPreferences.getString("17","").equals("b")){
                editor.putString("17","n");
                editor.putString("prev","17");
                editor.apply();
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("17")){
            if(sharedPreferences.getString("18","").equals("b")){
                editor.putString("18","n");
                editor.putString("prev","18");
                editor.apply();
                notificationManager.notify(notificationId, builder.build());
            }
        }
    }


}
