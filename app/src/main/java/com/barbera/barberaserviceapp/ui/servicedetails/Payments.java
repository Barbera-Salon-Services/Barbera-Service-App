package com.barbera.barberaserviceapp.ui.servicedetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.barbera.barberaserviceapp.ActivityPhoneVerification;
import com.barbera.barberaserviceapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Payments extends AppCompatActivity {
    private Button prev,submit;
    private ImageButton pay;
    private ImageView img6;
    private boolean check6=false;
    private Uri filePath;
    Bitmap rc= VehicleDetails.rc_bitmap;
    Bitmap license= VehicleDetails.lic_bitmap;
    Bitmap aad = PersonalDocuments.aad_bitmap;
    Bitmap pn = PersonalDocuments.pan_bitmap;
    Bitmap photo = PersonalDocuments.photo_bitmap;
    private Bitmap payment;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String name,number,address,aadhar,pan,gender,lic_no;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        prev = findViewById(R.id.prev);
        pay= findViewById(R.id.receiptUp);
        submit=findViewById(R.id.submit);
        img6=findViewById(R.id.img6);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        EnableRuntimePermission();

        SharedPreferences preferences = getSharedPreferences("Details",MODE_PRIVATE);
        name = preferences.getString("name",null);
        number = preferences.getString("number",null);
        address = preferences.getString("address",null);
        aadhar = preferences.getString("aadhar",null);
        pan = preferences.getString("pan",null);
        gender = preferences.getString("gender",null);
        lic_no=preferences.getString("license_no",null);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payments.this,VehicleDetails.class);
                startActivity(intent);
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!check6){
                    Toast.makeText(getApplicationContext(),"Please enter your receipt photo",Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadImageToFirebase(rc,"_rc");
                    uploadImageToFirebase(license,"_lic");
                    uploadImageToFirebase(aad,"_aadhar");
                    uploadImageToFirebase(pn,"_pan");
                    uploadImageToFirebase(photo,"_photo");
                    uploadImageToFirebase(payment,"_receipt");
                    Map<String,Object> mp = new HashMap<>();
                    mp.put("name",name);
                    mp.put("number",number);
                    mp.put("address",address);
                    mp.put("aadhar",aadhar);
                    mp.put("license_no",lic_no);
                    mp.put("pan",pan);
                    mp.put("gender",gender);
                    String id = FirebaseFirestore.getInstance().collection("Barber details").document().getId();
                    FirebaseFirestore.getInstance().collection("Barber details").document(id).set(mp)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"We are reviewing your response and will get back to you soon",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Payments.this, ActivityPhoneVerification.class);

                                    startActivity(intent);
                                }
                            });
                }

            }
        });
    }
    private void uploadImageToFirebase(Bitmap bitmap, String a) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();


        StorageReference ref = storageReference.child("barbers/"+name+a);

        ref.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    progressDialog.dismiss();
                    Toast.makeText(Payments.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(Payments.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(snapshot -> {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded"+(int)progress+"%");
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            payment = (Bitmap) data.getExtras().get("data");
            filePath = data.getData();
            img6.setVisibility(View.VISIBLE);
            check6=true;
        }
    }

    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(Payments.this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(Payments.this,"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(Payments.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Payments.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Payments.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}