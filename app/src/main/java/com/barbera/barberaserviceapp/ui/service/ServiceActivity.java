package com.barbera.barberaserviceapp.ui.service;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.barbera.barberaserviceapp.R;
import com.barbera.barberaserviceapp.Utils.OtpItem;
import com.barbera.barberaserviceapp.network.JsonPlaceHolderApi;
import com.barbera.barberaserviceapp.network.RCI_otp;
import com.barbera.barberaserviceapp.network.RetrofitClientInstance;
import com.barbera.barberaserviceapp.network.RetrofitClientInstanceBarber;
import com.barbera.barberaserviceapp.network.RetrofitClientInstanceBooking;
import com.barbera.barberaserviceapp.ui.bookings.BookingFragment;
import com.barbera.barberaserviceapp.ui.bookings.BookingItemAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServiceActivity extends AppCompatActivity {

    private EditText startotp;
    private EditText endotp;
    private CardView startOtpBtn;
    private  CardView endOtpBtn;
    private String token,userId,serviceId;
    private TextView timer;
    private String[] ch;
    private String startdt,enddt,starttime,endtime;
    private List<String> sidlist=new ArrayList<>();
    private CountDownTimer countDownTimer;
    private long TimeLeftInMil;
    private long totalTime;
    private int amount,time;
    public static boolean timerRunning = false;
    private long endTimer;
    private SharedPreferences sharedPreferences1,sharedPreferences;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        sharedPreferences=getSharedPreferences("ServiceInfo",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        Retrofit retrofit = RetrofitClientInstanceBarber.getRetrofitInstance();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        SharedPreferences preferences = getSharedPreferences("Token", Context.MODE_PRIVATE);
        token = preferences.getString("token", "no");

        Intent intent= getIntent();
        sidlist= (List<String>) intent.getSerializableExtra("sidlist");
        userId=intent.getStringExtra("userId");
        amount=intent.getIntExtra("amount",0);
        time=intent.getIntExtra("time",0);
        serviceId=intent.getStringExtra("serviceId");
        startotp = findViewById(R.id.editText);
        timer = findViewById(R.id.timer);
        endotp = findViewById(R.id.editText1);
        startOtpBtn = findViewById(R.id.otp);
        endOtpBtn = findViewById(R.id.otp1);

        assignServiceTimer();
        calculateTime();

        startOtpBtn.setOnClickListener(v -> {
            String otpentered = startotp.getText().toString().trim();
            Call<Success> call = jsonPlaceHolderApi.confirmStartOtp(new OtpItem(otpentered, sidlist, userId), "Bearer " + token);
            call.enqueue(new Callback<Success>() {
                @Override
                public void onResponse(Call<Success> call, Response<Success> response) {
                    if (response.code() == 200) {
                        startotp.setVisibility(View.INVISIBLE);
                        startOtpBtn.setVisibility(View.INVISIBLE);
                        timer.setVisibility(View.VISIBLE);
                        endotp.setVisibility(View.VISIBLE);
                        endOtpBtn.setVisibility(View.VISIBLE);
                        BookingItemAdapter.unique=serviceId;
//                        Date today = new Date();
//                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
//                        startdt = format.format(today);
//                        Calendar calendar = Calendar.getInstance();
//                        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
//                        starttime = mdformat.format(calendar.getTime());
                        //Toast.makeText(getApplicationContext(),startdt+""+starttime,Toast.LENGTH_SHORT).show();
                        startTimer();
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong otp entered", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Success> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });

        endOtpBtn.setOnClickListener(v -> {
            startOtpBtn.setEnabled(false);
            String otp2 = endotp.getText().toString();
            Call<Success> call = jsonPlaceHolderApi.confirmEndOtp(new OtpItem(otp2, sidlist, userId), "Bearer " + token);
            call.enqueue(new Callback<Success>() {
                @Override
                public void onResponse(Call<Success> call, Response<Success> response) {
                    if(response.code()==200){
                        Date today = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                        enddt = format.format(today);
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                        endtime = mdformat.format(calendar.getTime());
                        endotp.getText().clear();
                        int amt = amount;
                        int pay= sharedPreferences.getInt("payment",0);
                        int trip= sharedPreferences.getInt("trips",0);
                        int points = sharedPreferences.getInt("points",0);
                        editor.putInt("payment",pay+amt);
                        editor.putInt("trips",trip+1);
                        if(trip>=3 && trip<5){
                            points = points+5;
                            editor.putInt("points",points);
                        }
                        else if(trip>=5 && trip<7){
                            points += 10;
                            editor.putInt("points",points);
                        }
                        else if(trip>=7 && trip<10){
                            points+=25;
                            editor.putInt("points",points+25);
                        }
                        else if(trip>=10){
                            points+=50;
                            editor.putInt("points",points);
                        }
                        else {
                            points+=2;
                            editor.putInt("points", points);
                        }
                        editor.commit();
                        int finalPoints = points;
                        timerRunning =false;
                        TimeLeftInMil = 0;
                        endTimer = 0;
                        SharedPreferences prefs = getSharedPreferences("LiveTimer", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putLong("millisLeft", TimeLeftInMil);
                        editor.putBoolean("timerRunning", timerRunning);
                        editor.putLong("endTime", endTimer);
                        editor.apply();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Wrong otp entered", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Success> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("LiveTimer", MODE_PRIVATE);
        TimeLeftInMil = prefs.getLong("millisLeft",totalTime);
        timerRunning = prefs.getBoolean("timerRunning",false);
        if(TimeLeftInMil == 0){
            TimeLeftInMil = totalTime;
        }

        if(timerRunning){
            endTimer = prefs.getLong("endTime",0);
            TimeLeftInMil = endTimer - System.currentTimeMillis();
            if(TimeLeftInMil<0){
                timerRunning = false;
            }else{
                startTimer();
                startOtpBtn.setVisibility(View.INVISIBLE);
                startotp.setVisibility(View.INVISIBLE);
                timer.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("LiveTimer", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", TimeLeftInMil);
        editor.putBoolean("timerRunning", timerRunning);
        editor.putLong("endTime", endTimer);
        editor.apply();
    }

    private void calculateTime() {
        totalTime = time*60000;
    }

    private void startTimer() {
        endTimer = System.currentTimeMillis() + TimeLeftInMil;
        timerRunning = true;
        countDownTimer =  new CountDownTimer(TimeLeftInMil,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeLeftInMil = millisUntilFinished;
                int minutes = (int) millisUntilFinished/60000;
                int seconds =(int) millisUntilFinished%60000 /1000;
                String timer1="";
                if(minutes<10) {
                    timer1+="0";
                }
                 timer1 += minutes+":";
                if(seconds<10) timer1+= "0";
                timer1+=seconds;
                timer.setText(timer1);
            }

            @Override
            public void onFinish() {
                endotp.setVisibility(View.VISIBLE);
                endOtpBtn.setVisibility(View.VISIBLE);
                timerRunning =false;
            }
        }.start();

    }

//    private void updateInDb(String name, String service, int time, String address, String amount, int id, String date, String contact) {
//        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
//        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//        final ProgressDialog progressDialog=new ProgressDialog(ServiceActivity.this);
//        progressDialog.setMessage("Ending Service Please wait ....!!");
//        progressDialog.show();
//        progressDialog.setCancelable(true);
//        Call<String> call = jsonPlaceHolderApi.updateAssignee(name,service,time,address,amount,"done","update",2,id,date,contact);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if(response.isSuccessful()){
//                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//                Toast.makeText(getApplicationContext(),"NOt Success",Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//                finish();
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),"N Success",Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//                finish();
//            }
//        });
//    }

    private void assignServiceTimer() {
        sharedPreferences1 = getSharedPreferences("Timer",MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putInt("1",30);
        editor1.putInt("2",40);
        editor1.putInt("3",30);
        editor1.putInt("4",60);
        editor1.putInt("5",20);
        editor1.putInt("6",20);
        editor1.putInt("7",20);
        editor1.putInt("8",20);
        editor1.putInt("9",40);
        editor1.putInt("10",40);
        editor1.putInt("11",40);
        editor1.putInt("12",40);
        editor1.putInt("13",40);
        editor1.putInt("14",60);
        editor1.putInt("15",80);
        editor1.putInt("16",50);
        editor1.putInt("17",50);
        editor1.putInt("18",30);
        editor1.putInt("19",20);
        editor1.putInt("20",45);
        editor1.putInt("21",30);
        editor1.putInt("22",30);
        editor1.putInt("23",20);
        editor1.putInt("24",25);
        editor1.putInt("25",25);
        editor1.putInt("26",80);
        editor1.putInt("27",50);
        editor1.putInt("28",60);
        editor1.putInt("29",30);
        editor1.putInt("30",40);
        editor1.putInt("31",30);
        editor1.putInt("32",40);
        editor1.putInt("33",30);
        editor1.putInt("34",30);
        editor1.putInt("35",30);
        editor1.putInt("36",45);
        editor1.putInt("37",40);
        editor1.putInt("38",30);
        editor1.putInt("39",40);
        editor1.putInt("40",20);
        editor1.putInt("41",60);
        editor1.putInt("42",60);
        editor1.putInt("43",50);
        editor1.putInt("44",60);
        editor1.putInt("45",80);
        editor1.putInt("46",70);
        editor1.putInt("47",90);
        editor1.putInt("48",50);
        editor1.putInt("49",50);
        editor1.putInt("50",30);
        editor1.putInt("51",30);
        editor1.putInt("52",45);
        editor1.putInt("53",20);
        editor1.putInt("54",20);
        editor1.putInt("55",15);
        editor1.putInt("56",150);
        editor1.putInt("57",40);
        editor1.putInt("58",30);
        editor1.putInt("59",30);
        editor1.putInt("60",30);
        editor1.putInt("61",30);
        editor1.putInt("62",30);
        editor1.putInt("63",30);
        editor1.putInt("64",120);
        editor1.putInt("65",150);
        editor1.putInt("66",60);
        editor1.putInt("67",120);
        editor1.putInt("68",90);
        editor1.putInt("69",120);
        editor1.putInt("70",130);
        editor1.putInt("71",15);
        editor1.putInt("72",40);
        editor1.commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(ServiceActivity.this, BookingFragment.class));
    }
}