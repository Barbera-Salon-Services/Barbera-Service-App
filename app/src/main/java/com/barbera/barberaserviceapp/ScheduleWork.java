package com.barbera.barberaserviceapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;

public class ScheduleWork extends Worker {
    private Context context;
    public ScheduleWork(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        SharedPreferences sharedPreferences=context.getSharedPreferences("Schedule",context.MODE_PRIVATE);

        return Result.success();
    }
}
