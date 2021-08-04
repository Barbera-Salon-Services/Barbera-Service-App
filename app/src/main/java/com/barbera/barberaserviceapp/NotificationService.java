package com.barbera.barberaserviceapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.barbera.barberaserviceapp.network.JsonPlaceHolderApi;
import com.barbera.barberaserviceapp.network.RetrofitClientInstanceBarber;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String x=remoteMessage.getNotification().getBody();
        Retrofit retrofit= RetrofitClientInstanceBarber.getRetrofitInstance();
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        SharedPreferences preferences= getSharedPreferences("Token", Context.MODE_PRIVATE);
        String token=preferences.getString("token","no");
        SharedPreferences sharedPreferences=getSharedPreferences("Schedule",MODE_PRIVATE);
        Log.d("av",x);
        if(x.startsWith("You have been booked")){
            String z=x.substring(24,34);   //dd-mm-yyyy
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            Log.d("av",formattedDate);
            Log.d("av",z);
            if(formattedDate.equals(z)){
                Call<ScheduleList> call=jsonPlaceHolderApi.getSchedule("Bearer "+token);
                call.enqueue(new Callback<ScheduleList>() {
                    @Override
                    public void onResponse(Call<ScheduleList> call, Response<ScheduleList> response) {
                        if(response.code()==200){
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
            }
        }
        else{
            String y=x.substring(0,6);
            SharedPreferences sharedPreferences1=getSharedPreferences("Notification",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences1.edit();
            editor.putString("notif",y);
            editor.apply();
        }
        sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }
    private void sendNotification(String title,String message){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("Barbera","Barbera", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"Barbera")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(message);

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(999,builder.build());

    }

}
