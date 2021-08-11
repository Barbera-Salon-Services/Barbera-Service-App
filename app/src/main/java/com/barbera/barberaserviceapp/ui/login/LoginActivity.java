//package com.barbera.barberaserviceapp.ui.login;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//
//import com.barbera.barberaserviceapp.MainActivity;
//import com.barbera.barberaserviceapp.R;
//
//import com.barbera.barberaserviceapp.ui.servicedetails.PersonalDetails;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class LoginActivity extends AppCompatActivity {
//    private CardView skipLogin;
//    private EditText email;
//    private EditText password;
//    private CardView login;
//    private FirebaseAuth firebaseAuth;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        skipLogin =findViewById(R.id.skip_login);
//        email = findViewById(R.id.email);
//        password =findViewById(R.id.Password);
//        login =findViewById(R.id.Login);
//        firebaseAuth=FirebaseAuth.getInstance();
//
//        skipLogin.setOnClickListener(v -> {
//            skipLogin.setEnabled(false);
//            startActivity(new Intent(LoginActivity.this,MainActivity.class));
//        });
//
//        login.setOnClickListener(v -> {
//            if(checkEmailAndPassword()){
//                final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
//                progressDialog.setMessage("Logging You In...");
//                progressDialog.show();
//                progressDialog.setCancelable(false);
//                login.setEnabled(false);
//                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
//                        .addOnCompleteListener(task -> {
//                            if(task.isSuccessful()){
//                                progressDialog.dismiss();
////                                SharedPreferences preferences = getSharedPreferences("Login",MODE_PRIVATE);
////                                SharedPreferences.Editor editor=preferences.edit();
////                                editor.putBoolean("login",true);
////                                editor.commit();
////                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
//                                finish();
//                            }
//                            else{
//                                progressDialog.dismiss();
//                                Toast.makeText(getApplicationContext(),"Error Credentials",Toast.LENGTH_SHORT).show();
//                                login.setEnabled(true);
//                                /*new_user.setEnabled(true);
//                                login_using_phone.setEnabled(true);
//                                withoutLogin.setEnabled(true);*/
//                            }
//                        });
//
//            }
//        });
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    }
//
//    private boolean checkEmailAndPassword() {
//        if(email.getText().toString().isEmpty()){
//            email.setError("Please Enter a Registered Email Address");
//            email.requestFocus();
//            return false;
//        }
//        if(password.getText().toString().isEmpty()||password.getText().toString().length()<8){
//            password.setError("Please Enter a Valid Password");
//            password.requestFocus();
//            return false;
//        }
//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
//        if(email.getText().toString().matches(emailPattern)){
//            return true;
//        }
//        else{
//            email.setError("Enter a Valid Email Address");
//            return false;
//        }
//    }
//
//    private void sendToServiceDetails(){
//        Intent intent = new Intent(LoginActivity.this, PersonalDetails.class);
//        startActivity(intent);
//    }
//}