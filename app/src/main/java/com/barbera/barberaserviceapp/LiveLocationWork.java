package com.barbera.barberaserviceapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.barbera.barberaserviceapp.network.JsonPlaceHolderApi;
import com.barbera.barberaserviceapp.network.Register;
import com.barbera.barberaserviceapp.network.RetrofitClientInstanceUser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.barbera.barberaserviceapp.ServiceApplication.pubnub;

public class LiveLocationWork extends Worker {
    private FusedLocationProviderClient mFusedLocationClient;
    public static String person;
    private LocationRequest locationRequest;

    public LiveLocationWork(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        //Log.d("on","oncreate");
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // 5 second delay between each request
        locationRequest.setFastestInterval(5000); // 5 seconds fastest time in between each request
        locationRequest.setSmallestDisplacement(400); // 500 meters minimum displacement for new location request
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // enables GPS high accuracy location requests
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
                        Calendar rightNow = Calendar.getInstance();
                        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
                        Log.d("cur",currentHourIn24Format+"");
                        if(currentHourIn24Format>=6 && currentHourIn24Format<=18){
                            runTimer();
                            runLongLoop();
                        }
                    }
                }, 300000);
                Looper.loop();
            }
        };
        thread.start();
    }
    public void runTimer(){
        Log.d("loc","loc");
        try {
            mFusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    LinkedHashMap<String, String> message = getNewLocationMessage(location.getLatitude(), location.getLongitude());
                    pubnub.publish()
                            .message(message)
                            .channel(person)
                            .async(new PNCallback<PNPublishResult>() {
                                @Override
                                public void onResponse(PNPublishResult result, PNStatus status) {
                                    // handle publish result, status always present, result if successful
                                    // status.isError() to see if error happened

                                    if (!status.isError()) {
                                        System.out.println("pub timetoken: " + result.getTimetoken());
                                    }
                                    System.out.println("pub status code: " + status.getStatusCode());
                                    double lt=location.getLatitude();
                                    double lon=location.getLongitude();
                                    Geocoder geocoder;
                                    List<Address> addresses = null;
                                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    try {
                                        addresses = geocoder.getFromLocation(lt, lon, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Address add=addresses.get(0);
                                    Retrofit retrofit = RetrofitClientInstanceUser.getRetrofitInstance();
                                    JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
                                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("Token", Context.MODE_PRIVATE);
                                    String token = preferences.getString("token", "no");
                                    Call<Void> call=jsonPlaceHolderApi.updateAddress(new Register(lt,lon,add.getAddressLine(0)),"Bearer "+token);
                                    call.enqueue(new retrofit2.Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            //Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {

                                        }
                                    });
                                }
                            });
                }
            }, Looper.myLooper());

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    private LinkedHashMap<String, String> getNewLocationMessage(double lat, double lng) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("lat", String.valueOf(lat));
        map.put("lng", String.valueOf(lng));
        return map;
    }

}
