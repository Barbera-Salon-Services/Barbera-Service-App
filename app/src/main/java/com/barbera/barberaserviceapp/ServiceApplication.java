package com.barbera.barberaserviceapp;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;

import io.realm.Realm;

public class ServiceApplication extends Application {
    public static final String ID ="live location";
    public static final String SID="schedule";
    public static final String NID="1 hour";
    public static PubNub pubnub;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        initPubnub();

        SharedPreferences sharedPreferences = getSharedPreferences("ServiceList",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("1","Simple hair cut");
        editor.putString("2","Stylish hair cut");
        editor.putString("3","Child hair cut");
        editor.putString("4","hair cut with hair wash");
        editor.putString("5","simple shaving");
        editor.putString("6","beard trimming");
        editor.putString("7","denim shave");
        editor.putString("8","foam shave");
        editor.putString("9","simple hair color");
        editor.putString("10","loreal hair color");
        editor.putString("11","garnier hair color");
        editor.putString("12","matrix hair color");
        editor.putString("13","simple facial ");
        editor.putString("14","gold facial");
        editor.putString("15","o3 facial");
        editor.putString("16","fruit facial");
        editor.putString("17","aroma facial");
        editor.putString("18","aroma cleanup");
        editor.putString("19","fruit face cleanup");
        editor.putString("20","o3 face cleanup");
        editor.putString("21","gold face cleanup");
        editor.putString("22","men highlight per streak");
        editor.putString("23","charcoal face mask");
        editor.putString("24","Dtan o3");
        editor.putString("25","Dtan basic");
        editor.putString("26","hair cut+ simple shaving + hair wash");
        editor.putString("27","hair cut+ simple shaving ");
        editor.putString("28","men + kid haircut");
        editor.putString("29","Hair cut (U,V,straight)");
        editor.putString("30","Stylish hair cut (step or layer)");
        editor.putString("31","Child hair cut ");
        editor.putString("32","Hair styling iron tongs");
        editor.putString("33","Hair straightening");
        editor.putString("34","Blow dry");
        editor.putString("35","hair wash");
        editor.putString("36","hair massage with wash");
        editor.putString("37","Garnier hair color");
        editor.putString("38","hena mehandi");
        editor.putString("39","loreal hair color");
        editor.putString("40","threading");
        editor.putString("41","gold facial");
        editor.putString("42","VLCC facial");
        editor.putString("43","fruit facial");
        editor.putString("44","diamond facial");
        editor.putString("45","kaya facial");
        editor.putString("46","lotus facial");
        editor.putString("47","shahnaz facial");
        editor.putString("48","skinny whitening");
        editor.putString("49","anti acne facial");
        editor.putString("50","hand wax");
        editor.putString("51","half leg");
        editor.putString("52","full leg");
        editor.putString("53","front wax");
        editor.putString("54","back wax");
        editor.putString("55","under arm");
        editor.putString("56","full body");
        editor.putString("57","pedicure");
        editor.putString("58","manicure");
        editor.putString("59","oxy face bleach");
        editor.putString("60","diamond face bleach");
        editor.putString("61","gold bleach");
        editor.putString("62","fruit bleach");
        editor.putString("63","laser bleach");
        editor.putString("64","gold facial + gold bleach + any hair cut");
        editor.putString("65","oxy bleach + gold facial + simple hand wax + full leg wax (simple)");
        editor.putString("66","hand wax + fruit face bleach");
        editor.putString("67","gold bleach + simple hand wax + herbal manicure+ herbal pedicure + therading");
        editor.putString("68","fruit facial + threading+ fruit bleach");
        editor.putString("69","oxy bleach+ hand wax+ diamond facial");
        editor.putString("70","gold facial+ milk hand wax+ full leg milk wax + threading");
        editor.putString("71","Head Massage");
        editor.putString("72","Hair color Application (Men)");
        editor.commit();

        createNotification();

    }
    private void initPubnub() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-27ca2eba-78f1-11eb-b338-de22a5d8105b");
        pnConfiguration.setPublishKey("pub-c-f5d42daf-d998-43a8-a9ac-272850696a87");
        pnConfiguration.setSecure(true);
        pubnub = new PubNub(pnConfiguration);
    }
    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    ID,
                    "Live Location Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
            NotificationChannel scheduleChannel = new NotificationChannel(
                    SID,
                    "Schedule Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager1 = getSystemService(NotificationManager.class);
            manager1.createNotificationChannel(scheduleChannel);
        }
    }

}
