package com.barbera.barberaserviceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.barbera.barberaserviceapp.network.JsonPlaceHolderApi;
import com.barbera.barberaserviceapp.network.RetrofitClientInstanceBarber;
import com.barbera.barberaserviceapp.network.RetrofitClientInstanceBooking;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PeriodicWork extends Worker {
    private Context context;

    public PeriodicWork(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        Log.d("work","Work is done");
        Calendar currentDate = Calendar.getInstance();
        Calendar dueDate = Calendar.getInstance();
        // Set Execution around 01:00:00 AM
        dueDate.set(Calendar.HOUR_OF_DAY, 0);
        dueDate.set(Calendar.MINUTE, 30);
        dueDate.set(Calendar.SECOND, 0);

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24);
        }
        Log.d("abs",dueDate.getTimeInMillis()+" "+currentDate.getTimeInMillis());
        long timeDiff = dueDate.getTimeInMillis() -currentDate.getTimeInMillis();
        OneTimeWorkRequest oneTimeWorkRequest= new OneTimeWorkRequest.Builder(PeriodicWork.class)
                .setInitialDelay(15, TimeUnit.MINUTES).build();

//            PeriodicWorkRequest periodicWorkRequest=new PeriodicWorkRequest.Builder(
//                    PeriodicWork.class,timeDiff, TimeUnit.MILLISECONDS
//            ).build();
        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
        Retrofit retrofit= RetrofitClientInstanceBarber.getRetrofitInstance();
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        SharedPreferences preferences = context.getSharedPreferences("Token", context.MODE_PRIVATE);
        String token = preferences.getString("token", "no");
        Call<Void> call= jsonPlaceHolderApi.newLog("Bearer "+token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
        return Result.success();
    }
}
