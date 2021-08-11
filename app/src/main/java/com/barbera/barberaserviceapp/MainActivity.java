package com.barbera.barberaserviceapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import android.app.ActionBar;
import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.MenuItem;
import android.widget.Toast;

import com.barbera.barberaserviceapp.ui.bookings.BookingFragment;
import com.barbera.barberaserviceapp.ui.bookings.BookingItem;
import com.barbera.barberaserviceapp.ui.bookings.BookingModel;
import com.barbera.barberaserviceapp.ui.home.HomeFragment;
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

public class MainActivity extends AppCompatActivity{
    public static List<BookingModel> itemList;
    private WorkManager mWorkManager,workManager,pWorkManager;
    public static boolean trig=true;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemList =new ArrayList<BookingModel>();
        actionBar = getActionBar();
        trig=true;
//        Calendar rn=Calendar.getInstance();
//        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH");
//        String date2 = dateFormat2.format(rn.getTime());
//        Toast.makeText(getApplicationContext(),date2,Toast.LENGTH_SHORT).show();
        mWorkManager = WorkManager.getInstance(getApplication());
        pWorkManager = WorkManager.getInstance(getApplication());
        workManager=WorkManager.getInstance(getApplication());
        SharedPreferences sharedPreferences=getSharedPreferences("abcd",MODE_PRIVATE);
        boolean first=sharedPreferences.getBoolean("first",false);
        if(!first) {
            SharedPreferences yo=getSharedPreferences("Schedule",MODE_PRIVATE);
            SharedPreferences.Editor editor1=sharedPreferences.edit();
            editor1.putString("prev","25");
            editor1.apply();
//            Toast.makeText(getApplicationContext(), "All tasks started", Toast.LENGTH_LONG).show();
//            Calendar currentDate = Calendar.getInstance();
//            Calendar dueDate = Calendar.getInstance();
//            dueDate.set(Calendar.HOUR_OF_DAY, 14);
//            dueDate.set(Calendar.MINUTE, 30);
//            dueDate.set(Calendar.SECOND, 0);
//            if (dueDate.before(currentDate)) {
//                dueDate.add(Calendar.HOUR_OF_DAY, 24);
//            }
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//            String date = dateFormat.format(currentDate.getTime());
//            String date1 = dateFormat.format(dueDate.getTime());
//            Log.d("abs", date);
//            Log.d("abs", date1);
//            long timeDiff = dueDate.getTimeInMillis() - currentDate.getTimeInMillis();
//            OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(PeriodicWork.class)
//                    .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS).build();
//            WorkManager.getInstance().enqueue(oneTimeWorkRequest);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("first", true);
            editor.apply();
        }
        applyBlur(1);
        checkPermission();
        getInfo();
        Fragment fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        triggerRunner();
    }
    void applyBlur(int blurLevel) {
        Calendar currentDate = Calendar.getInstance();
        int x=currentDate.get(Calendar.HOUR_OF_DAY);
        Calendar dueDate = Calendar.getInstance();
            x++;
            if(x==24){
                x=0;
            }
            dueDate.set(Calendar.HOUR_OF_DAY, x);
            dueDate.set(Calendar.MINUTE, 0);
            dueDate.set(Calendar.SECOND, 0);
            long timeDiff = dueDate.getTimeInMillis() - currentDate.getTimeInMillis();
            OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BackgroundTask.class)
                    .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS).build();

        //mWorkManager.enqueue(oneTimeWorkRequest);
        mWorkManager.enqueue(OneTimeWorkRequest.from(BackgroundTask.class));
        workManager.enqueue(OneTimeWorkRequest.from(PeriodicWork.class));
    }

    private void getInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("ServiceChannel",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("channel_name","FS");
        editor.apply();

    }
    public void triggerRunner() {
        Log.d("ATM", "Insider triggering of runner");
        pWorkManager.enqueue(OneTimeWorkRequest.from(LiveLocationWork.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void triggerPiP() {
        Log.d("pip","triggered");
        Display d = getWindowManager()
                .getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);
        int width = p.x;
        int height = p.y;

        Rational ratio
                = new Rational(width, height);
        PictureInPictureParams.Builder
                pip_Builder
                = new PictureInPictureParams
                .Builder();
        pip_Builder.setAspectRatio(ratio).build();
        enterPictureInPictureMode(pip_Builder.build());
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        Fragment selectedFragment =null;
        switch (item.getItemId()){
            case R.id.nav_home:
                trig=false;
                selectedFragment = new HomeFragment();
                break;
            case R.id.profile:
                trig=false;
                selectedFragment = new ProfileFragment();
                break;
            case R.id.nav_mybookings:
                trig=false;
                selectedFragment = new BookingFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        return true;
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//        homeIntent.addCategory( Intent.CATEGORY_HOME );
//        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(homeIntent);
        triggerPiP();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        super.onPause();
        if(trig){
            Log.d("pip","made pip");
            triggerPiP();
        }
    }
}

