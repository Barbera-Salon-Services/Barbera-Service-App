package com.barbera.barberaserviceapp.ui.profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.barbera.barberaserviceapp.MainActivity;
import com.barbera.barberaserviceapp.R;
import com.barbera.barberaserviceapp.SecondScreen;
import com.barbera.barberaserviceapp.Utils.CoinsItem;
import com.barbera.barberaserviceapp.network.JsonPlaceHolderApi;
import com.barbera.barberaserviceapp.network.RetrofitClientInstanceBarber;
import com.barbera.barberaserviceapp.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private TextView name;
    private ImageView refresh;
    private TextView email;
    private TextView phone;
    private TextView coins;
    private TextView points;
    private  TextView trips;
    private Button items;
    private CardView logout;

    private int key =0;
    private String profilename="",token;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_profile,container,false);
       name = (TextView)view.findViewById(R.id.NameInProfile);
       email = (TextView)view.findViewById(R.id.EmailInProfile);
       refresh = view.findViewById(R.id.refresh);
       phone =(TextView)view.findViewById(R.id.PhoneInProfile);
       coins =(TextView)view.findViewById(R.id.coins_text);
       points =(TextView)view.findViewById(R.id.points);
       trips =(TextView)view.findViewById(R.id.trip);
       logout=(CardView)view.findViewById(R.id.logout);
       items = view.findViewById(R.id.items);
       sharedPreferences = getActivity().getSharedPreferences("UserInfo", MODE_PRIVATE);

        SharedPreferences preferences=getActivity().getSharedPreferences("Token",MODE_PRIVATE);
        token = preferences.getString("token","no");

       refresh.setOnClickListener(v -> {
           key =1;
           displayProfile();
       });

       items.setOnClickListener(v -> {
           Intent intent = new Intent(getActivity(),ItemList.class);
           startActivity(intent);
       });

       displayProfile();
       return view;
    }

    private void displayProfile() {
        if(token.equals("no")){
            Toast.makeText(getContext(),"User Not Logged In",Toast.LENGTH_SHORT).show();
        }
//        else if(sharedPreferences.getString("profilename","") == "" || key ==1){
//            final ProgressDialog progressDialog=new ProgressDialog(getContext());
//            progressDialog.setMessage("Fetching Details...");
//            progressDialog.show();
//            progressDialog.setCancelable(false);
//            key =0;
//            FirebaseFirestore.getInstance().collection("Service").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if(task.isSuccessful()){
//                        SharedPreferences.Editor editor =sharedPreferences.edit();
//                        profilename = task.getResult().get("name").toString();
//                        name.setText(profilename);
////                        email.setText(task.getResult().get("email").toString());
//                        //phone.setText(task.getResult().get("phone").toString());
//                        //earnings.setText(task.getResult().get("earnings").toString());
//                        points.setText(task.getResult().get("points").toString());
//                        trips.setText(task.getResult().get("trips").toString());
//                        editor.putString("profilename",profilename);
////                        editor.putString("email",task.getResult().get("email").toString());
////                        editor.putString("phone",task.getResult().get("phone").toString());
//                        //editor.putString("earnings",task.getResult().get("earnings").toString());
//                        editor.putString("points",task.getResult().get("points").toString());
//                        editor.putString("trips",task.getResult().get("trips").toString());
//                        editor.commit();
//                        name.setVisibility(View.VISIBLE);
////                        email.setVisibility(View.VISIBLE);
////                        phone.setVisibility(View.VISIBLE);
//                        //earnings.setVisibility(View.VISIBLE);
//                        points.setVisibility(View.VISIBLE);
//                        trips.setVisibility(View.VISIBLE);
//                        progressDialog.dismiss();
//                    }
//                }
//            });
//        }
        else {
            name.setVisibility(View.VISIBLE);
//            email.setVisibility(View.VISIBLE);
//            phone.setVisibility(View.VISIBLE);
            //earnings.setVisibility(View.VISIBLE);
            points.setVisibility(View.VISIBLE);
            trips.setVisibility(View.VISIBLE);
            name.setText(sharedPreferences.getString("profilename",""));
            Retrofit retrofit = RetrofitClientInstanceBarber.getRetrofitInstance();
            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

            Call<CoinsItem> call=jsonPlaceHolderApi.getCoins("Bearer "+token);
            call.enqueue(new Callback<CoinsItem>() {
                @Override
                public void onResponse(Call<CoinsItem> call, Response<CoinsItem> response) {
                    if(response.code()==200){
                        coins.setText(response.body().getCoins()+"");
                    }
                }

                @Override
                public void onFailure(Call<CoinsItem> call, Throwable t) {

                }
            });
//            email.setText(sharedPreferences.getString("email",""));
//            phone.setText(sharedPreferences.getString("phone",""));
            //earnings.setText(sharedPreferences.getString("earnings",""));
            points.setText(sharedPreferences.getString("points",""));
            trips.setText(sharedPreferences.getString("trips",""));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilename="";
                final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setMessage("Logout From This Device??");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences=getActivity().getSharedPreferences("Token",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("token","no");
                        editor.apply();
                        startActivity(new Intent(getActivity(), SecondScreen.class));
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
    }
}
