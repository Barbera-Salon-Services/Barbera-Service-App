package com.barbera.barberaserviceapp.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.barbera.barberaserviceapp.R;
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

public class ItemList extends AppCompatActivity {
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

//        FirebaseFirestore.getInstance().collection("Service").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Item")
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                        itemModelList.add(new ItemModel(documentSnapshot.get("Apron").toString(), documentSnapshot.get("Carpet").toString(), documentSnapshot.get("Disposable").toString(),
//                                documentSnapshot.get("Mirror").toString(), documentSnapshot.get("Neck Tape").toString(), documentSnapshot.get("T-shirt").toString(),
//                                documentSnapshot.get("Tissue").toString(), documentSnapshot.get("Tripod").toString(), documentSnapshot.get("after lotion").toString(),
//                                documentSnapshot.get("bag").toString(), documentSnapshot.get("cleansing cream").toString(), documentSnapshot.get("shaving apron").toString(),
//                                documentSnapshot.get("shaving foam").toString(),documentSnapshot.get("date").toString(),documentSnapshot.getId(),documentSnapshot.get("Seen").toString()));
//                    }
//                    recyclerView.setAdapter(adapter);
//                }
//            }
//                });

    }
}