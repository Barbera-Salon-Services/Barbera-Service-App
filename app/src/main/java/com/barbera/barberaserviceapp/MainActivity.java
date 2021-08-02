package com.barbera.barberaserviceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.barbera.barberaserviceapp.ui.bookings.BookingFragment;
import com.barbera.barberaserviceapp.ui.bookings.BookingItem;
import com.barbera.barberaserviceapp.ui.bookings.BookingModel;
import com.barbera.barberaserviceapp.ui.home.HomeFragment;
import com.barbera.barberaserviceapp.ui.mybookings.MyBookingFragment;
import com.barbera.barberaserviceapp.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static List<BookingModel> itemList;
    public static  List<BookingItem> myBookingItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemList =new ArrayList<BookingModel>();
        SharedPreferences sharedPreferences=getSharedPreferences("abcd",MODE_PRIVATE);
        boolean first=sharedPreferences.getBoolean("first",false);
        if(!first){
            Calendar currentDate = Calendar.getInstance();
            Calendar dueDate = Calendar.getInstance();
            // Set Execution around 01:00:00 AM
            dueDate.set(Calendar.HOUR_OF_DAY, 12);
            dueDate.set(Calendar.MINUTE, 30);
            dueDate.set(Calendar.SECOND, 0);
            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR_OF_DAY, 24);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String date = dateFormat.format(currentDate.getTime());
            String date1 = dateFormat.format(dueDate.getTime());
            Log.d("abs",date);
            Log.d("abs",date1);
            long timeDiff = dueDate.getTimeInMillis() -currentDate.getTimeInMillis();
            OneTimeWorkRequest oneTimeWorkRequest= new OneTimeWorkRequest.Builder(PeriodicWork.class)
                    .setInitialDelay(timeDiff,TimeUnit.MILLISECONDS).build();

//            PeriodicWorkRequest periodicWorkRequest=new PeriodicWorkRequest.Builder(
//                    PeriodicWork.class,timeDiff, TimeUnit.MILLISECONDS
//            ).build();
            WorkManager.getInstance().enqueue(oneTimeWorkRequest);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("first",true);
            editor.apply();
        }
        myBookingItemList = new ArrayList<BookingItem>();
        checkPermission();
        getInfo();

        Fragment fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();

        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private void getInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("ServiceChannel",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("channel_name","FS");
        editor.apply();

    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        Fragment selectedFragment =null;
        switch (item.getItemId()){
            case R.id.nav_home:
                selectedFragment = new HomeFragment();
                break;
            case R.id.profile:
                selectedFragment = new ProfileFragment();
                break;
            case R.id.nav_mybookings:
                selectedFragment = new BookingFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        return true;
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
    }
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }
}