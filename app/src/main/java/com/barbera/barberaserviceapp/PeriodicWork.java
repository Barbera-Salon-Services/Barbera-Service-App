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
                .setInitialDelay(timeDiff, TimeUnit.MINUTES).build();

//            PeriodicWorkRequest periodicWorkRequest=new PeriodicWorkRequest.Builder(
//                    PeriodicWork.class,timeDiff, TimeUnit.MILLISECONDS
//            ).build();
        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
        Retrofit retrofit= RetrofitClientInstanceBarber.getRetrofitInstance();
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        SharedPreferences preferences = context.getSharedPreferences("Token", context.MODE_PRIVATE);
        String token = preferences.getString("token", "no");
        Call<ScheduleList> call=jsonPlaceHolderApi.getSchedule("Bearer "+token);
        call.enqueue(new Callback<ScheduleList>() {
            @Override
            public void onResponse(Call<ScheduleList> call, Response<ScheduleList> response) {
                if(response.code()==200){
                    SharedPreferences sharedPreferences=context.getSharedPreferences("Schedule",context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1=sharedPreferences.edit();
                    editor1.clear();
                    ScheduleList scheduleList=response.body();
                    ScheduleItem scheduleItem=scheduleList.getScheduleItem();
                    editor1.putString("6",scheduleItem.getN1());
                    editor1.putString("7",scheduleItem.getN2());
                    editor1.putString("8",scheduleItem.getN3());
                    editor1.putString("9",scheduleItem.getN4());
                    editor1.putString("10",scheduleItem.getN5());
                    editor1.putString("11",scheduleItem.getN6());
                    editor1.putString("12",scheduleItem.getN7());
                    editor1.putString("13",scheduleItem.getN8());
                    editor1.putString("14",scheduleItem.getN9());
                    editor1.putString("15",scheduleItem.getN10());
                    editor1.putString("16",scheduleItem.getN11());
                    editor1.putString("17",scheduleItem.getN12());
                    editor1.putString("18",scheduleItem.getN13());
                    editor1.apply();
                }
            }

            @Override
            public void onFailure(Call<ScheduleList> call, Throwable t) {

            }
        });
        return Result.success();
    }
}
