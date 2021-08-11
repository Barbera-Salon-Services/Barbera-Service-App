package com.barbera.barberaserviceapp.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaserviceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {
    private List<ItemModel> itemAdapterList;
    private Context context;
    private FragmentManager fragmentManager;
    public ItemListAdapter(List<ItemModel> itemModelList, Context context, FragmentManager fragmentManager){
        this.itemAdapterList = itemModelList;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }
    @NonNull
    @Override
    public ItemListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);

        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ItemViewHolder holder, int position) {
        ItemModel itemModel = itemAdapterList.get(position);
        Log.d("content",itemModel.getQuantity()+itemModel.getItem()+itemModel.getDate());
        holder.item.setText("Item name: "+itemModel.getItem());
        holder.date.setText("Date of issue: "+itemModel.getDate());
        holder.quantity.setText("Quantity: "+itemModel.getQuantity());
//        holder.aflot.setText(itemModel.getAflot());
//        holder.tiss.setText(itemModel.getTiss());
//        holder.tri.setText(itemModel.getTri());
//        holder.tsh.setText(itemModel.getTsh());
//        holder.disp.setText(itemModel.getDisp());
//        holder.neck.setText(itemModel.getNeck());
//        holder.mir.setText(itemModel.getMir());
//        holder.bag.setText(itemModel.getBag());
//        holder.clecre.setText(itemModel.getClecre());
//        holder.apron.setText(itemModel.getApron());
//        holder.car.setText(itemModel.getCar());
//        holder.shavapr.setText(itemModel.getShavapr());
//        holder.shavfoa.setText(itemModel.getShavfoa());
//        holder.date.setText(itemModel.getDate());
//        holder.docID = itemModel.getDocId();
//        holder.seen=itemModel.getSeen();

//        if(holder.seen.equals("Yes")){
//            holder.ok.setVisibility(View.GONE);
//        }
//        holder.ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.ok.setVisibility(View.GONE);
//                FirebaseFirestore.getInstance().collection("Service").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Item")
//                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                if(documentSnapshot.getId().equals(holder.docID)){
//                                    Map<String,Object> mp=new HashMap<>();
//                                    mp.put("Seen","Yes");
//                                    FirebaseFirestore.getInstance().collection("Service").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Item")
//                                            .document(documentSnapshot.getId()).update(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//
//                                        }
//                                    });
//                                }
//
//                            }
//                        }
//                    }
//                });
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return itemAdapterList.size();
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
//        private TextView apron,car,disp,mir,neck,tsh,tiss,tri,aflot,bag,clecre,shavapr,shavfoa,date;
//        private Button ok;
//        private String docID,seen;
        private TextView item,date,quantity;
        public ItemViewHolder(@NonNull View itemView){
            super(itemView);
            item=itemView.findViewById(R.id.item_name);
            date=itemView.findViewById(R.id.item_date);
            quantity=itemView.findViewById(R.id.item_quantity);
//            apron=itemView.findViewById(R.id.apron);
//            car=itemView.findViewById(R.id.carpet);
//            mir=itemView.findViewById(R.id.mirror);
//            disp=itemView.findViewById(R.id.disposable);
//            neck=itemView.findViewById(R.id.necktape);
//            tsh=itemView.findViewById(R.id.tshirt);
//            tiss=itemView.findViewById(R.id.tissue);
//            tri=itemView.findViewById(R.id.tripod);
//            aflot=itemView.findViewById(R.id.afterlotion);
//            bag=itemView.findViewById(R.id.bag);
//            clecre=itemView.findViewById(R.id.cleansingcream);
//            shavapr=itemView.findViewById(R.id.shavingapron);
//            shavfoa=itemView.findViewById(R.id.shavingfoam);
//            date=itemView.findViewById(R.id.date_of_item);
//            ok=itemView.findViewById(R.id.ok_btn);
        }
    }
}
