package com.barbera.barberaserviceapp.ui.servicedetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.barbera.barberaserviceapp.R;

public class VehicleDetails extends AppCompatActivity {
    private Button prev,next;
    private ImageButton rc,license;
    private Uri filePath;
    public static Bitmap rc_bitmap, lic_bitmap;
    private String license_no;
    private EditText licNo;
    private ImageView img4,img5;
    private boolean check4=false,check5=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);
        rc=findViewById(R.id.rcUp);
        license=findViewById(R.id.licUp);
        prev=findViewById(R.id.Btn3);
        next=findViewById(R.id.Btn2);
        licNo=findViewById(R.id.licInp);
        img4=findViewById(R.id.img4);
        img5=findViewById(R.id.img5);

        SharedPreferences preferences = getSharedPreferences("Details",MODE_PRIVATE);
        license_no=preferences.getString("license_no",null);


        if(license_no!=null){
            licNo.setText(license_no);
        }

        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });
        rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 8);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lic = licNo.getText().toString();
                if(!check5){
                    Toast.makeText(getApplicationContext(),"Please enter your RC photo",Toast.LENGTH_SHORT).show();
                }
                else if(!check4){
                    Toast.makeText(getApplicationContext(),"Please enter your Driving license photo",Toast.LENGTH_SHORT).show();
                }
                else if(lic.length()!=16){
                    Toast.makeText(getApplicationContext(),"Please enter your Driving license number",Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences preferences = getSharedPreferences("Details",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("license_no",lic);
                    editor.apply();
                    Intent intent = new Intent(VehicleDetails.this,Payments.class);
                    startActivity(intent);
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(VehicleDetails.this,PersonalDocuments.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            lic_bitmap = (Bitmap) data.getExtras().get("data");
            img5.setVisibility(View.VISIBLE);
            filePath = data.getData();
            check4=true;
        }
        if (requestCode == 8 && resultCode == RESULT_OK) {
            rc_bitmap = (Bitmap) data.getExtras().get("data");
            filePath = data.getData();
            img4.setVisibility(View.VISIBLE);
            check5=true;
        }
    }

    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(VehicleDetails.this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(VehicleDetails.this,"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(VehicleDetails.this,new String[]{
                    Manifest.permission.CAMERA}, Payments.RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        switch (requestCode) {
            case Payments.RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(VehicleDetails.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VehicleDetails.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}