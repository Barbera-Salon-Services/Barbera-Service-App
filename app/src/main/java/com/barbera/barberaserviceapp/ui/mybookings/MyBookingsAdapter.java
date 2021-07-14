package com.barbera.barberaserviceapp.ui.mybookings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaserviceapp.R;
import com.barbera.barberaserviceapp.network.JsonPlaceHolderApi;
import com.barbera.barberaserviceapp.network.RetrofitClientInstance;
import com.barbera.barberaserviceapp.ui.bookings.BookingItem;
import com.barbera.barberaserviceapp.ui.service.ImageVerifyActivity;
import com.barbera.barberaserviceapp.ui.service.ServiceActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.barbera.barberaserviceapp.ui.service.ServiceActivity.timerRunning;

public class MyBookingsAdapter {
// extends RecyclerView.Adapter<MyBookingsAdapter.MyBookingItemHolder> {
//    private List<BookingItem> bookingItemList;
//    private Context context;
//    public static  int counterId;
//
//    public MyBookingsAdapter(List<BookingItem> booking, Context context){
//        this.bookingItemList = booking;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public MyBookingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.mybooking_item,parent,false);
//        return new MyBookingItemHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyBookingItemHolder holder, int position) {
//        BookingItem bookingItem = bookingItemList.get(position);
//
////        String time =convertTime(bookingItem.getTime());
//        SharedPreferences sharedPreferences = context.getSharedPreferences("ServiceList",Context.MODE_PRIVATE);
//        SharedPreferences sharedPreferences1 = context.getSharedPreferences("ServiceInfo",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor= sharedPreferences1.edit();
//        String services[] = bookingItem.getService().split(",");
//        String Servicenames ="";
//        for(int i=0;i<services.length;i++){
//            Servicenames = Servicenames + sharedPreferences.getString(services[i],"")+",";
//        }
//
//        String time = convertTime(bookingItem.());
//
//        holder.address.setText(bookingItem);
//        holder.name.setText(bookingItem.getName());
//        holder.time.setText(time);
//        holder.contact.setText(bookingItem.getContact());
//        holder.service.setText(Servicenames);
//        holder.amount.setText(bookingItem.getAmount());
////        Date date = bookingItem.getDate();
//
//        if(timerRunning && counterId ==bookingItem.getId()){
//            holder.start.setVisibility(View.INVISIBLE);
//            holder.cancel.setVisibility(View.INVISIBLE);
//            holder.live.setVisibility(View.VISIBLE);
//            holder.livebtn.setVisibility(View.VISIBLE);
//        }
//        holder.livebtn.setOnClickListener(v -> {
//            counterId = bookingItem.getId();
//            Intent intent = new Intent(context, ServiceActivity.class);
//            intent.putExtra("name",bookingItem.getName());
//            intent.putExtra("service",bookingItem.getService());
//            intent.putExtra("time",bookingItem.getTime());
//            intent.putExtra("address",bookingItem.getAddress());
//            intent.putExtra("amount",bookingItem.getAmount());
//            intent.putExtra("id",bookingItem.getId());
//            intent.putExtra("date",bookingItem.getDate());
//            intent.putExtra("contact",bookingItem.getContact());
//            context.startActivity(intent);
//        });
//
//        holder.contact.setOnClickListener(v->{
//            Intent intent = new Intent(Intent.ACTION_DIAL);
//            intent.setData(Uri.parse("tel:"+bookingItem.getContact()));
//            context.startActivity(intent);
//        });
//
//        holder.direction.setOnClickListener(v -> {
//            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                    Uri.parse("google.navigation:q="+bookingItem.getAddress()));
//            context.startActivity(intent);
//        });
//
//        holder.start.setOnClickListener(v -> {
//            counterId = bookingItem.getId();
//            Intent intent = new Intent(context, ImageVerifyActivity.class);
//            intent.putExtra("name",bookingItem.getName());
//            intent.putExtra("service",bookingItem.getService());
//            intent.putExtra("time",bookingItem.getTime());
//            intent.putExtra("address",bookingItem.getAddress());
//            intent.putExtra("amount",bookingItem.getAmount());
//            intent.putExtra("id",bookingItem.getId());
//            intent.putExtra("date",bookingItem.getDate());
//            intent.putExtra("contact",bookingItem.getContact());
//            context.startActivity(intent);
//        });
//
//        holder.cancel.setOnClickListener(v -> {
//            int points = sharedPreferences1.getInt("points",0);
//            int cancel = sharedPreferences1.getInt("cancel",0);
//            editor.putInt("points",points-20);
//            editor.putInt("cancel",cancel+1);
//            editor.commit();
//            bookingItemList.remove(position);
//            notifyDataSetChanged();
//            updateAssigneeInDb(bookingItem.getName(),bookingItem.getService(),bookingItem.getTime(),bookingItem.getAddress(),bookingItem.getAmount(),
//                    bookingItem.getId(),bookingItem.getDate(),bookingItem.getContact());
//        });
//
//    }
//
//    private void updateAssigneeInDb(String name, String service, int time, String address, String amount, int id, String date, String contact) {
//        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
//        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//        final ProgressDialog progressDialog=new ProgressDialog(context);
//        progressDialog.setMessage("Cancelling Booking!!");
//        progressDialog.show();
//        progressDialog.setCancelable(true);
//        String unique=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
//        Call<String> call = jsonPlaceHolderApi.updateAssignee(name,service,time,address,amount,unique,"update",0,id,date,contact);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if(response.code() == 200){
//                    Toast.makeText(context,"Added booking",Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(context,"Something is wrong",Toast.LENGTH_SHORT).show();
//                }
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
////                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
//                Toast.makeText(context,"Cancelled booking",Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//            }
//        });
//    }
//    private String convertTime(int time) {
//        int hrs= time /100;
//        int min = time%100;
//        int hrs1= hrs;
//        String Time="";
//        if(hrs>12){
//            hrs = hrs -12;
//            if(min == 0)
//                Time = min+"0 ";
//            else
//                Time = min+" ";
//        }else{
//            if(min == 0)
//                Time = min+"0 ";
//            else
//                Time = min+" ";
//        }
//        if(hrs1>11)
//            Time =Time+ "PM";
//        else
//            Time = Time +"AM";
//
//        Time = hrs+":"+ Time;
//        return Time;
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return  bookingItemList.size();
//    }
//
//    public class MyBookingItemHolder extends RecyclerView.ViewHolder{
//        private TextView address;
//        private ImageView direction;
//        private TextView name;
//        private TextView contact;
//        private TextView time;
//        private TextView service;
//        private TextView amount;
//        private Button start;
//        private Button cancel;
//        private TextView live;
//        private Button livebtn;
//
//
//        public MyBookingItemHolder(@NonNull View itemView) {
//            super(itemView);
//
//            address = itemView.findViewById(R.id.address);
//            direction = itemView.findViewById(R.id.direction);
//            name = itemView.findViewById(R.id.nameIn);
//            contact = itemView.findViewById(R.id.contactIn);
//            time = itemView.findViewById(R.id.time1);
//            service =itemView.findViewById(R.id.serviceadd);
//            amount = itemView.findViewById(R.id.amt123);
//            start =itemView.findViewById(R.id.start);
//            cancel = itemView.findViewById(R.id.cancel_button);
//            live = itemView.findViewById(R.id.live);
//            livebtn = itemView.findViewById(R.id.liveBtn);
//
//        }
//    }
}
