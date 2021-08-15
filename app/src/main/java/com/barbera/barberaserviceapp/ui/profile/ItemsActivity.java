package com.barbera.barberaserviceapp.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaserviceapp.R;
import com.barbera.barberaserviceapp.network.JsonPlaceHolderApi;
import com.barbera.barberaserviceapp.network.RetrofitClientInstanceAdmin;
import com.barbera.barberaserviceapp.network.RetrofitClientInstanceBooking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ItemsActivity extends AppCompatActivity {
    private List<ItemModel> itemModelList=new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        recyclerView=findViewById(R.id.item_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new ItemListAdapter(itemModelList, getApplicationContext(), getSupportFragmentManager());

        Retrofit retrofit = RetrofitClientInstanceAdmin.getRetrofitInstance();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        SharedPreferences preferences= getSharedPreferences("Token", Context.MODE_PRIVATE);
        String token=preferences.getString("token","no");
        Call<ItemList> call=jsonPlaceHolderApi.getItems("Bearer "+token);
        call.enqueue(new Callback<ItemList>() {
            @Override
            public void onResponse(Call<ItemList> call, Response<ItemList> response) {
                if(response.code()==200){
                    ItemList itemList=response.body();
                    List<ItemModel> list=itemList.getList();
                    if(list.size()==0){
                        Toast.makeText(getApplicationContext(),"No items issued",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        for(ItemModel model:list){
                            itemModelList.add(model);
                        }
                        recyclerView.setAdapter(adapter);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Could not load item list",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ItemList> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}