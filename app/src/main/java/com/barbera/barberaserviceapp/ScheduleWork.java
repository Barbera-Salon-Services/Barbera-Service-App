package com.barbera.barberaserviceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;

import static com.barbera.barberaserviceapp.ServiceApplication.NID;

public class ScheduleWork extends Worker {
    private Context context;
    private static int notificationId=220;
    public ScheduleWork(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Service")
                .setContentText("You have a booking in 1 hour")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        SharedPreferences sharedPreferences=context.getSharedPreferences("Schedule",context.MODE_PRIVATE);
        Calendar rn=Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        String date = dateFormat.format(rn.getTime());
        Log.d("sch",date);
        if(date.equals("05")){
            if(sharedPreferences.getString("6","").equals("b")){
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("06")){
            if(sharedPreferences.getString("7","").equals("b")){
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("07")){
            if(sharedPreferences.getString("8","").equals("b")){
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("08")){
            if(sharedPreferences.getString("9","").equals("b")){
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("09")){
            if(sharedPreferences.getString("10","").equals("b")){
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("10")){
            if(sharedPreferences.getString("11","").equals("b")){
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("11")){
            if(sharedPreferences.getString("12","").equals("b")){
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("12")){
            if(sharedPreferences.getString("13","").equals("b")){
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("13")){
            if(sharedPreferences.getString("14","").equals("b")){
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("14")){
            if(sharedPreferences.getString("15","").equals("b")){
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("15")){
            if(sharedPreferences.getString("16","").equals("b")){
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("16")){
            if(sharedPreferences.getString("17","").equals("b")){
                notificationManager.notify(notificationId, builder.build());
            }
        }
        else if(date.equals("17")){
            if(sharedPreferences.getString("18","").equals("b")){
                notificationManager.notify(notificationId, builder.build());
            }
        }
        OneTimeWorkRequest oneTimeWorkRequest= new OneTimeWorkRequest.Builder(ScheduleWork.class)
                .setInitialDelay(1, TimeUnit.HOURS).build();
        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
        return Result.success();
    }
}
