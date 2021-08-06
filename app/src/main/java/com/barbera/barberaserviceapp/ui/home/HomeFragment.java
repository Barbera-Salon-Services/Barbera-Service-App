package com.barbera.barberaserviceapp.ui.home;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.barbera.barberaserviceapp.LiveLocationService;
import com.barbera.barberaserviceapp.R;
import com.barbera.barberaserviceapp.ScheduleService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.Objects;

import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private TextView trips;
    private TextView holidays;
    private  TextView cancelled;
    private TextView points;
    private SharedPreferences sharedPreferences;
    private Toolbar toolbar;
    private SwitchCompat switchCompat;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences= Objects.requireNonNull(getActivity()).getSharedPreferences("ServiceInfo",MODE_PRIVATE);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home,container,false);
        toolbar= view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        trips= view.findViewById(R.id.tripps);
        //earnings = view.findViewById(R.id.earning);
        cancelled = view.findViewById(R.id.cancelled);
        points = view.findViewById(R.id.points);
        switchCompat = view.findViewById(R.id.switchs);
        holidays=view.findViewById(R.id.holiday);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        if(sharedPreferences.getBoolean("ischecked",false)){
//            startFS();
//            switchCompat.setChecked(true);
//        }

        trips.setText(sharedPreferences.getInt("trips",0)+"");
        //earnings.setText("Rs. "+sharedPreferences.getInt("payment",0));
        cancelled.setText(sharedPreferences.getInt("cancel",0)+"");
        points.setText(sharedPreferences.getInt("points",0)+"");

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                editor.putBoolean("ischecked",true);
                startFS();
//                scheduleJob(buttonView);
//                Toast.makeText(getContext(),"Checked",Toast.LENGTH_SHORT).show();
            }else{
//                Intent serviceIntent = new Intent(getContext(), LiveLocationService.class);
//                getActivity().stopService(serviceIntent);
//                Intent scheduleIntent= new Intent(getContext(), ScheduleService.class);
//                getActivity().stopService(scheduleIntent);
//                cancelJob();
                editor.putBoolean("ischecked",false);
                Toast.makeText(getContext(),"Not checked",Toast.LENGTH_SHORT).show();
            }
            editor.apply();
        });

        return view;
    }

    private void startFS() {
//        Intent serviceIntent = new Intent(getContext(), LiveLocationService.class);
//        ContextCompat.startForegroundService(getContext(), serviceIntent);
        getActivity().startService(new Intent(getContext(), LiveLocationService.class));
//        Intent scheduleIntent= new Intent(getContext(), ScheduleService.class);
//        ContextCompat.startForegroundService(getContext(),scheduleIntent);
    }
    public void scheduleJob(View v){
        ComponentName componentName=new ComponentName(getContext(),LiveLocationService.class);
        JobInfo info= new JobInfo.Builder(123,componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15*60*1000)
                .build();
        JobScheduler scheduler=(JobScheduler) getContext().getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode=scheduler.schedule(info);
        if(resultCode==JobScheduler.RESULT_SUCCESS){
            Log.d("location job","Job scheduled");
        }
    }

    public  void cancelJob(){
        JobScheduler scheduler=(JobScheduler) getContext().getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d("location job","Job cancelled");
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
