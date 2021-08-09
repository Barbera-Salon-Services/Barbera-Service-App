package com.barbera.barberaserviceapp.ui.bookings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaserviceapp.R;
import com.barbera.barberaserviceapp.Utils.OtpItem;
import com.barbera.barberaserviceapp.network.JsonPlaceHolderApi;
import com.barbera.barberaserviceapp.network.RetrofitClientInstance;
import com.barbera.barberaserviceapp.network.RetrofitClientInstanceBarber;
import com.barbera.barberaserviceapp.network.RetrofitClientInstanceBooking;

import com.barbera.barberaserviceapp.ui.service.Success;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static io.realm.Realm.getApplicationContext;

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
        SharedPreferences preferences= context.getSharedPreferences("Token", Context.MODE_PRIVATE);
        String token=preferences.getString("token","no");
        Retrofit retrofit = RetrofitClientInstanceBarber.getRetrofitInstance();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        if(bookingItem.getStatus().equals("done")){
            holder.start.setVisibility(View.INVISIBLE);
            holder.enterOtp.setVisibility(View.INVISIBLE);
            holder.barberLeft.setVisibility(View.INVISIBLE);
            holder.status.setText("Completed");
        }
        if(bookingItem.getStatus().equals("ongoing")){
            holder.start.setVisibility(View.VISIBLE);
            holder.enterOtp.setVisibility(View.VISIBLE);
            holder.enterOtp.setHint("Enter end otp");
            holder.barberLeft.setVisibility(View.INVISIBLE);
            holder.status.setText("Ongoing");
        }
        if(bookingItem.getStatus().equals("pending")){
            holder.start.setVisibility(View.VISIBLE);
            holder.enterOtp.setVisibility(View.VISIBLE);
            holder.enterOtp.setHint("Enter start otp");
            holder.barberLeft.setVisibility(View.VISIBLE);
            holder.status.setText("Pending");
        }
        if(bookingItem.getMode().equals("start")){
            //Log.d("here","start");
            holder.barberLeft.setVisibility(View.INVISIBLE);
        }
        else{
            holder.barberLeft.setVisibility(View.VISIBLE);
        }
        if(position!=0){
            holder.barberLeft.setVisibility(View.INVISIBLE);
        }

        holder.address.setText(bookingItem.getAddress());
        holder.service.setText(bookingItem.getSummary());
        holder.amount.setText(bookingItem.getAmount()+"");
        holder.time.setText(bookingItem.getDate()+" "+bookingItem.getTime()+":00");
        holder.distance.setText(bookingItem.getDistance()+"");

        holder.start.setOnClickListener(v -> {
            if(bookingItem.getStatus().equals("pending")){
                String otpentered=holder.enterOtp.getText().toString().trim();
                if(otpentered.length()!=6){
                    holder.enterOtp.setError("Wrong otp entered");
                }
                else{
                    ProgressDialog progressDialog=new ProgressDialog(context);
                    progressDialog.show();
                    Call<Success> call = jsonPlaceHolderApi.confirmStartOtp(new OtpItem(otpentered, bookingItem.getSidlist(), bookingItem.getUserId()), "Bearer " + token);
                    call.enqueue(new Callback<Success>() {
                        @Override
                        public void onResponse(Call<Success> call, Response<Success> response) {
                            if (response.code() == 200) {
                                holder.enterOtp.setVisibility(View.VISIBLE);
                                holder.enterOtp.getText().clear();
                                holder.enterOtp.setHint("Enter end otp");
                                holder.status.setVisibility(View.VISIBLE);
                                holder.status.setText("Ongoing");
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Wrong otp entered", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Success> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            else{
                String otp2 = holder.enterOtp.getText().toString().trim();
                //Log.d("len",otp2.length()+"");
                if(otp2.length()!=6){
                    holder.enterOtp.setError("Wrong otp entered");
                }
                else {
                    ProgressDialog progressDialog=new ProgressDialog(context);
                    progressDialog.show();
                    Call<Success> call = jsonPlaceHolderApi.confirmEndOtp(new OtpItem(otp2,bookingItem.getSidlist(), bookingItem.getUserId()), "Bearer " + token);
                    call.enqueue(new Callback<Success>() {
                        @Override
                        public void onResponse(Call<Success> call, Response<Success> response) {
                            if(response.code()==200){
                                holder.start.setVisibility(View.INVISIBLE);
                                holder.enterOtp.setVisibility(View.INVISIBLE);
                                holder.barberLeft.setVisibility(View.INVISIBLE);
                                holder.status.setText("Completed");
                                progressDialog.dismiss();
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Wrong otp entered", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Success> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        holder.direction.setOnClickListener(v -> {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q="+bookingItem.getAddress()));
            context.startActivity(intent);
        });

        holder.barberLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Void> call=jsonPlaceHolderApi.barberLeft(new OtpItem(null,null,bookingItem.getUserId()),"Bearer "+token);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        holder.barberLeft.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
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
        private TextView time,status;
        private TextView distance;
        private EditText enterOtp;
        private Button start,barberLeft;
        private ImageView direction;
        public BookingItemHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.add);
            service = itemView.findViewById(R.id.service);
            amount = itemView.findViewById(R.id.amt);
            time = itemView.findViewById(R.id.Time1);
            distance=itemView.findViewById(R.id.distance);
            start=itemView.findViewById(R.id.start);
            direction=itemView.findViewById(R.id.direction);
            barberLeft=itemView.findViewById(R.id.barberLeft);
            enterOtp=itemView.findViewById(R.id.otp_edit);
            status=itemView.findViewById(R.id.status);
        }
    }
}
