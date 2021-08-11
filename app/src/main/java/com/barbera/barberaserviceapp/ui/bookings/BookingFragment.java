package com.barbera.barberaserviceapp.ui.bookings;

import android.app.PictureInPictureParams;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaserviceapp.MainActivity;
import com.barbera.barberaserviceapp.R;
import com.barbera.barberaserviceapp.network.JsonPlaceHolderApi;
import com.barbera.barberaserviceapp.network.RetrofitClientInstance;
import com.barbera.barberaserviceapp.network.RetrofitClientInstanceBooking;

import java.util.ArrayList;
import java.util.List;


import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
//
//import static com.barbera.barberaserviceapp.MainActivity.itemList;

public class BookingFragment extends Fragment {
    private Retrofit retrofit;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    public static BookingItemAdapter adapter;
    public static List<BookingModel> itemList=new ArrayList<>();
    public static List<BookingItem> myBookingItemList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new BookingItemAdapter(itemList,getActivity());
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bookings,container,false);
        toolbar= view.findViewById(R.id.tool);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        recyclerView = view.findViewById(R.id.recycler_booking);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        MainActivity.trig=true;

        if(itemList.size() != 0 ){
            attach_adapter();
        }
        else {
            getBookingList();
        }
        return view;
    }

    private void getBookingList() {
        retrofit = RetrofitClientInstanceBooking.getRetrofitInstance();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        SharedPreferences preferences= getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
        String token=preferences.getString("token","no");
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching Today's Bookings");
        progressDialog.show();
        progressDialog.setCancelable(true);
        if(itemList!=null){
            itemList.clear();
            adapter.notifyDataSetChanged();
        }
        Call<BookingList> call =jsonPlaceHolderApi.getBookings("Bearer "+token);
        call.enqueue(new Callback<BookingList>() {
            @Override
            public void onResponse(Call<BookingList> call, Response<BookingList> response) {
                if(!(response.code()==200)){
                    Toast.makeText(getContext(),"Cannot find Bookings",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
                List<BookingItem> bookingItems = response.body().getList();
                List<BookingItem> bookingList=response.body().getList1();
                String mode= response.body().isMode();
                //Log.d("mode",mode);
                if(bookingList.size()==0){
                    Toast.makeText(getContext(),"No bookings made",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else{
                    int i = 0, amount = 0,totalTime=0;
                    String summary = "", date = "", slot = "", timestamp = "",address="",userId="",serviceId="",status="";
                    double distance=0;
                    List<String> sidlist=new ArrayList<>();
                    for(BookingItem bookingItem: bookingList){
                        if (i == 0) {
                            String name = bookingItem.getService().getName();
                            String gender = bookingItem.getService().getGender();
                            int price = bookingItem.getService().getPrice();
                            int quantity=bookingItem.getQuantity();
                            summary += "(" + gender + ") " + name + "   Rs: " + price + "  ("+quantity+")"+"\n";
                            amount += (bookingItem.getQuantity()*bookingItem.getService().getPrice());
                            timestamp += bookingItem.getTimestamp();
                            sidlist.add(bookingItem.getServiceId());
                            date = bookingItem.getDate();
                            slot = bookingItem.getSlot();
                            distance=bookingItem.getDistance();
                            address=bookingItem.getAdd();
                            userId=bookingItem.getUserId();
                            totalTime+=bookingItem.getService().getTime()*bookingItem.getQuantity();
                            serviceId=bookingItem.getServiceId();
                            status=bookingItem.getStatus();
                            i++;
                        } else {
                            if (bookingItem.getTimestamp().equals(timestamp)) {
                                String name = bookingItem.getService().getName();
                                String gender = bookingItem.getService().getGender();
                                int price = bookingItem.getService().getPrice();
                                int quantity = bookingItem.getQuantity();
                                summary += "(" + gender + ") " + name + "   Rs: " + price + "  (" + quantity + ")" + "\n";
                                amount += (bookingItem.getQuantity() * bookingItem.getService().getPrice());
                                date = bookingItem.getDate();
                                slot = bookingItem.getSlot();
                                timestamp = "";
                                timestamp += bookingItem.getTimestamp();
                                distance=bookingItem.getDistance();
                                address=bookingItem.getAdd();
                                sidlist.add(bookingItem.getServiceId());
                                totalTime+=bookingItem.getService().getTime()*bookingItem.getQuantity();
                            } else {
                                //Log.d("timestamp",timestamp);
                                itemList.add(new BookingModel(summary, amount, date, slot,address,distance,userId,sidlist,totalTime,serviceId,status,mode));
                                date = bookingItem.getDate();
                                slot = bookingItem.getSlot();
                                summary = "";
                                String name = bookingItem.getService().getName();
                                String gender = bookingItem.getService().getGender();
                                int price = bookingItem.getService().getPrice();
                                int quantity = bookingItem.getQuantity();
                                summary += "(" + gender + ") " + name + "   Rs: " + price + "  (" + quantity + ")" + "\n";
                                amount = 0;
                                amount += (bookingItem.getQuantity() * bookingItem.getService().getPrice());
                                timestamp = "";
                                timestamp += bookingItem.getTimestamp();
                                distance=bookingItem.getDistance();
                                address=bookingItem.getAdd();
                                userId=bookingItem.getUserId();
                                status=bookingItem.getStatus();
                                sidlist=new ArrayList<>();
                                sidlist.add(bookingItem.getServiceId());
                                totalTime=0;
                                totalTime+=bookingItem.getService().getTime()*bookingItem.getQuantity();
                                serviceId=bookingItem.getServiceId();
                            }
                        }
                    }
                    itemList.add(new BookingModel(summary, amount, date, slot,address,distance,userId,sidlist,totalTime,serviceId,status,mode));
                    if(bookingItems.size()!=0){
                        i = 0;
                        amount = 0;
                        totalTime=0;summary = "";date = ""; slot = ""; timestamp = "";address="";userId="";serviceId="";status="";
                        distance=0;
                        sidlist=new ArrayList<>();
                        for(BookingItem bookingItem: bookingItems){
                            if (i == 0) {
                                String name = bookingItem.getService().getName();
                                String gender = bookingItem.getService().getGender();
                                int price = bookingItem.getService().getPrice();
                                int quantity=bookingItem.getQuantity();
                                summary += "(" + gender + ") " + name + "   Rs: " + price + "  ("+quantity+")"+"\n";
                                amount += (bookingItem.getQuantity()*bookingItem.getService().getPrice());
                                timestamp += bookingItem.getTimestamp();
                                sidlist.add(bookingItem.getServiceId());
                                date = bookingItem.getDate();
                                slot = bookingItem.getSlot();
                                distance=bookingItem.getDistance();
                                address=bookingItem.getAdd();
                                userId=bookingItem.getUserId();
                                totalTime+=bookingItem.getService().getTime()*bookingItem.getQuantity();
                                serviceId=bookingItem.getServiceId();
                                status=bookingItem.getStatus();
                                i++;
                            } else {
                                if (bookingItem.getTimestamp().equals(timestamp)) {
                                    String name = bookingItem.getService().getName();
                                    String gender = bookingItem.getService().getGender();
                                    int price = bookingItem.getService().getPrice();
                                    int quantity = bookingItem.getQuantity();
                                    summary += "(" + gender + ") " + name + "   Rs: " + price + "  (" + quantity + ")" + "\n";
                                    amount += (bookingItem.getQuantity() * bookingItem.getService().getPrice());
                                    date = bookingItem.getDate();
                                    slot = bookingItem.getSlot();
                                    timestamp = "";
                                    timestamp += bookingItem.getTimestamp();
                                    distance=bookingItem.getDistance();
                                    address=bookingItem.getAdd();
                                    sidlist.add(bookingItem.getServiceId());
                                    totalTime+=bookingItem.getService().getTime()*bookingItem.getQuantity();
                                } else {
                                    //Log.d("timestamp",timestamp);
                                    itemList.add(new BookingModel(summary, amount, date, slot,address,distance,userId,sidlist,totalTime,serviceId,status,mode));
                                    date = bookingItem.getDate();
                                    slot = bookingItem.getSlot();
                                    summary = "";
                                    String name = bookingItem.getService().getName();
                                    String gender = bookingItem.getService().getGender();
                                    int price = bookingItem.getService().getPrice();
                                    int quantity = bookingItem.getQuantity();
                                    summary += "(" + gender + ") " + name + "   Rs: " + price + "  (" + quantity + ")" + "\n";
                                    amount = 0;
                                    amount += (bookingItem.getQuantity() * bookingItem.getService().getPrice());
                                    timestamp = "";
                                    timestamp += bookingItem.getTimestamp();
                                    distance=bookingItem.getDistance();
                                    address=bookingItem.getAdd();
                                    userId=bookingItem.getUserId();
                                    status=bookingItem.getStatus();
                                    sidlist=new ArrayList<>();
                                    sidlist.add(bookingItem.getServiceId());
                                    totalTime=0;
                                    totalTime+=bookingItem.getService().getTime()*bookingItem.getQuantity();
                                    serviceId=bookingItem.getServiceId();
                                }
                            }
                        }
                        itemList.add(new BookingModel(summary, amount, date, slot,address,distance,userId,sidlist,totalTime,serviceId,status,mode));
                    }
                    progressDialog.dismiss();
                    attach_adapter();
//                    addToLocalDb();
                }
            }

            @Override
            public void onFailure(Call<BookingList> call, Throwable t) {
                Toast.makeText(getContext(),"Cannot find any bookings",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.refresh_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            getBookingList();
        }
        return super.onOptionsItemSelected(item);
    }

    private void attach_adapter() {
        recyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPause() {
        super.onPause();
        if(MainActivity.trig){
            Log.d("pip","made pip");
            triggerPiP();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void triggerPiP() {
        Log.d("pip","triggered");
        Display d = getActivity().getWindowManager()
                .getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);
        int width = p.x;
        int height = p.y;

        Rational ratio
                = new Rational(width, height);
        PictureInPictureParams.Builder
                pip_Builder
                = new PictureInPictureParams
                .Builder();
        pip_Builder.setAspectRatio(ratio).build();
        getActivity().enterPictureInPictureMode(pip_Builder.build());
    }
}
