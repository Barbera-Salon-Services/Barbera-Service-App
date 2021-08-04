package com.barbera.barberaserviceapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
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
    public static  String person;

    private LocationRequest locationRequest;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, ID)
                .setContentTitle("Location")
                .setContentText("Live Location is On")
                .setSmallIcon(R.drawable.ic_baseline_home_24)
                .setContentIntent(pendingIntent)
                .build();
        SharedPreferences sharedPreferences = getSharedPreferences("ServiceChannel",MODE_PRIVATE);
        person = sharedPreferences.getString("channel_name","");
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
                                    Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_SHORT).show();
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
                                    call.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {

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
        startForeground(1, notification);
        //do heavy work on a background thread
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000); // 5 second delay between each request
        locationRequest.setFastestInterval(5000); // 5 seconds fastest time in between each request
        locationRequest.setSmallestDisplacement(1); // 500 meters minimum displacement for new location request
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // enables GPS high accuracy location requests
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private LinkedHashMap<String, String> getNewLocationMessage(double lat, double lng) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("lat", String.valueOf(lat));
        map.put("lng", String.valueOf(lng));
        return map;
    }
}
