package com.barbera.barberaserviceapp.ui.bookings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaserviceapp.R;
import com.barbera.barberaserviceapp.network.JsonPlaceHolderApi;
import com.barbera.barberaserviceapp.network.RetrofitClientInstance;
import com.barbera.barberaserviceapp.ui.service.ImageVerifyActivity;
import com.barbera.barberaserviceapp.ui.service.ServiceActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingItemAdapter extends RecyclerView.Adapter<BookingItemAdapter.BookingItemHolder> {

    private List<BookingModel> bookingItemList;
    private  Context context;


    public BookingItemAdapter(List<BookingModel> booking, Context context){
        this.bookingItemList = booking;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item,parent,false);
        return new BookingItemHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookingItemAdapter.BookingItemHolder holder, int position) {
        BookingModel bookingItem = bookingItemList.get(position);

        holder.address.setText(bookingItem.getAddress());
        holder.service.setText(bookingItem.getSummary());
        holder.amount.setText(bookingItem.getAmount()+"");
        holder.time.setText(bookingItem.getDate()+" "+bookingItem.getTime()+":00");
        holder.distance.setText(bookingItem.getDistance()+"");

        holder.start.setOnClickListener(v -> {
            Intent intent = new Intent(context,ServiceActivity.class);
            intent.putExtra("userId",bookingItem.getUserId());
            intent.putExtra("sidlist",(Serializable)bookingItem.getSidlist());
            intent.putExtra("amount",bookingItem.getAmount());
            ((Activity)context).finish();
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return bookingItemList.size();
    }

    public static class BookingItemHolder extends RecyclerView.ViewHolder {
        private TextView address;
        private TextView service;
        private TextView amount;
        private TextView time;
        private TextView distance;
        private Button start;
        public BookingItemHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.add);
            service = itemView.findViewById(R.id.service);
            amount = itemView.findViewById(R.id.amt);
            time = itemView.findViewById(R.id.Time1);
            distance=itemView.findViewById(R.id.distance);
        }
    }
}
