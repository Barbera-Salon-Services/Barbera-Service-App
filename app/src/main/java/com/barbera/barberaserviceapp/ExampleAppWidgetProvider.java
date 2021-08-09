//package com.barbera.barberaserviceapp;
//
//import android.app.PendingIntent;
//import android.appwidget.AppWidgetManager;
//import android.appwidget.AppWidgetProvider;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Log;
//import android.widget.RemoteViews;
//
//import com.barbera.barberaserviceapp.network.JsonPlaceHolderApi;
//import com.barbera.barberaserviceapp.network.Register;
//import com.barbera.barberaserviceapp.network.RetrofitClientInstanceUser;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.pubnub.api.callbacks.PNCallback;
//import com.pubnub.api.models.consumer.PNPublishResult;
//import com.pubnub.api.models.consumer.PNStatus;
//
//import java.io.IOException;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Locale;
//
//import retrofit2.Call;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//
//import static com.barbera.barberaserviceapp.ServiceApplication.pubnub;
//
//public class ExampleAppWidgetProvider extends AppWidgetProvider {
//    public static String REFRESH_ACTION="Refresh_Widget";
//    private FusedLocationProviderClient mFusedLocationClient;
//    private LocationRequest locationRequest;
//    public static String person;
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String action=intent.getAction();
//        if(action.equals(REFRESH_ACTION)){
//            runLongLoop(context);
//        }
//        super.onReceive(context, intent);
//    }
//
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        final int N = appWidgetIds.length;
//
//        // Perform this loop procedure for each App Widget that belongs to this provider
//        for (int i=0; i<N; i++) {
//            int appWidgetId = appWidgetIds[i];
//
//            // Create an Intent to launch ExampleActivity
//            Intent intent = new Intent(context, ExampleAppWidgetProvider.class);
//            intent.setAction(ExampleAppWidgetProvider.REFRESH_ACTION);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//
//            // Get the layout for the App Widget and attach an on-click listener
//            // to the button
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_widget);
//            views.setOnClickPendingIntent(R.id.widg_btn, pendingIntent);
//
//            // Tell the AppWidgetManager to perform an update on the current app widget
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//        }
//    }
//    private LinkedHashMap<String, String> getNewLocationMessage(double lat, double lng) {
//        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//        map.put("lat", String.valueOf(lat));
//        map.put("lng", String.valueOf(lng));
//        return map;
//    }
//    public void runLongLoop(Context context) {
//        Log.d("In","loop");
//        Thread thread = new Thread() {
//            public void run() {
//                Looper.prepare();
//                Handler mHandler = new Handler();
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        RunTimer(context);
//                        //runLongLoop(context);
//                    }
//                }, 1000);
//                Looper.loop();
//            }
//        };
//        thread.start();
//    }
//    public void RunTimer(Context context) {
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
//        Log.d("on","oncreate");
//        locationRequest = LocationRequest.create();
//        locationRequest.setInterval(5000); // 5 second delay between each request
//        locationRequest.setFastestInterval(5000); // 5 seconds fastest time in between each request
//        locationRequest.setSmallestDisplacement(1); // 500 meters minimum displacement for new location request
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // enables GPS high accuracy location requests
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
//                                    double lt=location.getLatitude();
//                                    double lon=location.getLongitude();
//                                    Geocoder geocoder;
//                                    List<Address> addresses = null;
//                                    geocoder = new Geocoder(context.getApplicationContext(), Locale.getDefault());
//                                    try {
//                                        addresses = geocoder.getFromLocation(lt, lon, 1);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    Address add=addresses.get(0);
//                                    Retrofit retrofit = RetrofitClientInstanceUser.getRetrofitInstance();
//                                    JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
//                                    SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("Token", Context.MODE_PRIVATE);
//                                    String token = preferences.getString("token", "no");
//                                    Call<Void> call=jsonPlaceHolderApi.updateAddress(new Register(lt,lon,add.getAddressLine(0)),"Bearer "+token);
//                                    call.enqueue(new retrofit2.Callback<Void>() {
//                                        @Override
//                                        public void onResponse(Call<Void> call, Response<Void> response) {
//                                            //Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_SHORT).show();
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<Void> call, Throwable t) {
//
//                                        }
//                                    });
//                                }
//                            });
//                }
//            }, Looper.myLooper());
//
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
