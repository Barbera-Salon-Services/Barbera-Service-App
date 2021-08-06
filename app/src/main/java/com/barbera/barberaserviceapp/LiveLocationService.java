package com.barbera.barberaserviceapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
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

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

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

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.barbera.barberaserviceapp.ServiceApplication.ID;
import static com.barbera.barberaserviceapp.ServiceApplication.pubnub;

public class LiveLocationService extends Service {
    private FusedLocationProviderClient mFusedLocationClient;
    public static String person;
    private boolean jobCancelled = false;
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    private LocationRequest locationRequest;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
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
                                    SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
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
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg1);
                     //       Log.d("stop","stopped");
        }
    }

//    @Override
//    public boolean onStartJob(JobParameters params) {
////        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
////        Log.d("on","oncreate");
////        locationRequest = LocationRequest.create();
////        locationRequest.setInterval(5000); // 5 second delay between each request
////        locationRequest.setFastestInterval(5000); // 5 seconds fastest time in between each request
////        locationRequest.setSmallestDisplacement(500); // 500 meters minimum displacement for new location request
////        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // enables GPS high accuracy location
//        doBackground(params);
//        return false;
//    }
//
//
////    @Override
////    public int onStartCommand(Intent intent, int flags, int startId) {
////
//////        AsyncTask.execute(new Runnable() {
//////            @Override
//////            public void run() {
////                try {
////            mFusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
////                @Override
////                public void onLocationResult(LocationResult locationResult) {
////                    Location location = locationResult.getLastLocation();
////                    LinkedHashMap<String, String> message = getNewLocationMessage(location.getLatitude(), location.getLongitude());
////                    pubnub.publish()
////                            .message(message)
////                            .channel(person)
////                            .async(new PNCallback<PNPublishResult>() {
////                                @Override
////                                public void onResponse(PNPublishResult result, PNStatus status) {
////                                    // handle publish result, status always present, result if successful
////                                    // status.isError() to see if error happened
////
////                                    if (!status.isError()) {
////                                        System.out.println("pub timetoken: " + result.getTimetoken());
////                                    }
////                                    System.out.println("pub status code: " + status.getStatusCode());
////                                    double lt=location.getLatitude();
////                                    double lon=location.getLongitude();
////                                    Geocoder geocoder;
////                                    List<Address> addresses = null;
////                                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
////                                    try {
////                                        addresses = geocoder.getFromLocation(lt, lon, 1);
////                                    } catch (IOException e) {
////                                        e.printStackTrace();
////                                    }
////                                    Address add=addresses.get(0);
////                                    Retrofit retrofit = RetrofitClientInstanceUser.getRetrofitInstance();
////                                    JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
////                                    SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
////                                    String token = preferences.getString("token", "no");
////                                    Call<Void> call=jsonPlaceHolderApi.updateAddress(new Register(lt,lon,add.getAddressLine(0)),"Bearer "+token);
////                                    call.enqueue(new Callback<Void>() {
////                                        @Override
////                                        public void onResponse(Call<Void> call, Response<Void> response) {
////                                            //Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_SHORT).show();
////                                        }
////
////                                        @Override
////                                        public void onFailure(Call<Void> call, Throwable t) {
////
////                                        }
////                                    });
////                                }
////                            });
////                }
////            }, Looper.myLooper());
////
////        } catch (SecurityException e) {
////            e.printStackTrace();
////        }
//////            }
//////        });
////        return START_STICKY;
////    }
//
//    private void doBackground(JobParameters parameters)  {
////        AsyncTask.execute(new Runnable() {
////            @Override
////            public void run() {
////        if (jobCancelled) {
////            return;
////        }
////                Intent notificationIntent = new Intent(this, MainActivity.class);
////                PendingIntent pendingIntent = PendingIntent.getActivity(this,
////                        0, notificationIntent, 0);
////                Notification notification = new NotificationCompat.Builder(this, ID)
////                        .setContentTitle("Location")
////                        .setContentText("Live Location is On")
////                        .setSmallIcon(R.drawable.ic_baseline_home_24)
////                        .setContentIntent(pendingIntent)
////                        .build();
//        SharedPreferences sharedPreferences = getSharedPreferences("ServiceChannel", MODE_PRIVATE);
//        person = sharedPreferences.getString("channel_name", "");
//        try {
//            mFusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
//                @Override
//                public void onLocationResult(LocationResult locationResult) {
//                    Location location = locationResult.getLastLocation();
//                    LinkedHashMap<String, String> message = getNewLocationMessage(location.getLatitude(), location.getLongitude());
//                    pubnub.publish()
//                            .message(message)
//                            .channel(person)
//                            .async(new PNCallback<PNPublishResult>() {
//                                @Override
//                                public void onResponse(PNPublishResult result, PNStatus status) {
//                                    // handle publish result, status always present, result if successful
//                                    // status.isError() to see if error happened
//
//                                    if (!status.isError()) {
//                                        System.out.println("pub timetoken: " + result.getTimetoken());
//                                    }
//                                    System.out.println("pub status code: " + status.getStatusCode());
//                                    double lt = location.getLatitude();
//                                    double lon = location.getLongitude();
//                                    Geocoder geocoder;
//                                    List<Address> addresses = null;
//                                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                                    try {
//                                        addresses = geocoder.getFromLocation(lt, lon, 1);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    Address add = addresses.get(0);
////                                    Retrofit retrofit = RetrofitClientInstanceUser.getRetrofitInstance();
////                                    JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
////                                    SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
////                                    String token = preferences.getString("token", "no");
////                                    Call<Void> call = jsonPlaceHolderApi.updateAddress(new Register(lt, lon, add.getAddressLine(0)), "Bearer " + token);
////                                    call.enqueue(new Callback<Void>() {
////                                        @Override
////                                        public void onResponse(Call<Void> call, Response<Void> response) {
////                                            Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_SHORT).show();
////                                        }
////
////                                        @Override
////                                        public void onFailure(Call<Void> call, Throwable t) {
////
////                                        }
////                                    });
//                                    Log.d("Live location","Called");
//                                }
//                            });
//                }
//            }, Looper.myLooper());
//
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }
//        jobFinished(parameters, false);
////                startForeground(1, notification);
//    }
////        });
////}
//    @Override
//    public boolean onStopJob(JobParameters params) {
//        jobCancelled=true;
//        return true;
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);
        //do heavy work on a background thread
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Log.d("on","oncreate");
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000); // 5 second delay between each request
        locationRequest.setFastestInterval(5000); // 5 seconds fastest time in between each request
        locationRequest.setSmallestDisplacement(1); // 500 meters minimum displacement for new location request
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // enables GPS high accuracy location requests
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    private LinkedHashMap<String, String> getNewLocationMessage(double lat, double lng) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("lat", String.valueOf(lat));
        map.put("lng", String.valueOf(lng));
        return map;
    }

}
