//package com.barbera.barberaserviceapp.ui.service;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import android.Manifest;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.FileUtils;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.barbera.barberaserviceapp.R;
//import com.google.android.gms.tasks.OnCanceledListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.io.ByteArrayOutputStream;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//import java.util.Objects;
//
//import static com.barbera.barberaserviceapp.LiveLocationService.person;
//
//public class ImageVerifyActivity extends AppCompatActivity {
//    private ImageView imageView;
//    private Button cont;
//    private Button retry;
//    private Bitmap bitmap;
//    private Uri filePath;
//    public static final int RequestPermissionCode = 1;
//
//    private String name;
//    private String start;
//    private String end;
//    private String service;
//    private int time;
//    private String address;
//    private String amount;
//    private int id;
//    private String date;
//    private String contact;
//
//    private FirebaseStorage firebaseStorage;
//    private StorageReference storageReference;;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_image_verify);
//
//        firebaseStorage = FirebaseStorage.getInstance();
//        storageReference = firebaseStorage.getReference();
//
//        imageView = findViewById(R.id.img);
//        cont  = findViewById(R.id.button);
//        retry =findViewById(R.id.retry);
//
//        name= Objects.requireNonNull(getIntent().getExtras()).getString("name");
//        service = getIntent().getExtras().getString("service");
//        time = getIntent().getExtras().getInt("time");
//        address = getIntent().getExtras().getString("address");
//        amount = getIntent().getExtras().getString("amount");
//        id= getIntent().getExtras().getInt("id");
//        date =getIntent().getExtras().getString("date");
//        contact = getIntent().getExtras().getString("contact");
//
//        EnableRuntimePermission();
//
//        retry.setOnClickListener(v -> {
//            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(intent, 7);
//        });
//
//        cont.setOnClickListener(v -> {
//            uploadImageToFirebase(bitmap);
//        });
//    }
//
//    private void uploadImageToFirebase(Bitmap bitmap) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//            ProgressDialog progressDialog
//                    = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
//
//            Date date = new Date();
//
//            StorageReference ref = storageReference.child("servicemen/"+person+date.toString());
//
//            ref.putBytes(data)
//                    .addOnSuccessListener(taskSnapshot -> {
//                        progressDialog.dismiss();
//                        Toast.makeText(ImageVerifyActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
//                        sendtoServiceAct();
//                    })
//                    .addOnFailureListener(e -> {
//                        progressDialog.dismiss();
//                        Toast.makeText(ImageVerifyActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    })
//                    .addOnProgressListener(snapshot -> {
//                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
//                        progressDialog.setMessage("Uploaded"+(int)progress+"%");
//                    });
//    }
//
//    private void sendtoServiceAct() {
//        Intent intent = new Intent(ImageVerifyActivity.this,ServiceActivity.class);
//        intent.putExtra("name",name);
//        intent.putExtra("service",service);
//        intent.putExtra("time",time);
//        intent.putExtra("address",address);
//        intent.putExtra("amount",amount);
//        intent.putExtra("id",id);
//        intent.putExtra("date",date);
//        intent.putExtra("contact",contact);
//        finish();
//        startActivity(intent);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 7 && resultCode == RESULT_OK) {
//            bitmap = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(bitmap);
//            filePath = data.getData();
//        }
//    }
//
//    public void EnableRuntimePermission(){
//        if (ActivityCompat.shouldShowRequestPermissionRationale(ImageVerifyActivity.this,
//                Manifest.permission.CAMERA)) {
//            Toast.makeText(ImageVerifyActivity.this,"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
//        } else {
//            ActivityCompat.requestPermissions(ImageVerifyActivity.this,new String[]{
//                    Manifest.permission.CAMERA}, RequestPermissionCode);
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
//        switch (requestCode) {
//            case RequestPermissionCode:
//                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(ImageVerifyActivity.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(ImageVerifyActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
//                }
//                break;
//        }
//    }
//}