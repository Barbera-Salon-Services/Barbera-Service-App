//package com.barbera.barberaserviceapp.ui;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import com.barbera.barberaserviceapp.MainActivity;
//import com.barbera.barberaserviceapp.R;
//import com.barbera.barberaserviceapp.ui.login.LoginActivity;
//import com.barbera.barberaserviceapp.ui.servicedetails.PersonalDetails;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class CheckUser extends AppCompatActivity {
//    private Button already_user,new_user;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_check_user);
//        already_user=findViewById(R.id.already_user);
//        new_user=findViewById(R.id.new_user_signup);
//        already_user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CheckUser.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//        new_user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CheckUser.this, PersonalDetails.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
//            startActivity(new Intent(CheckUser.this,MainActivity.class));
//        }
//    }
//}