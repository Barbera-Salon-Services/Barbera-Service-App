package com.barbera.barberaserviceapp.ui.mybookings;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.barbera.barberaserviceapp.R;
import com.barbera.barberaserviceapp.network.JsonPlaceHolderApi;
import com.barbera.barberaserviceapp.network.RetrofitClientInstance;
import com.barbera.barberaserviceapp.ui.bookings.BookingItem;
import com.barbera.barberaserviceapp.ui.bookings.BookingList;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.barbera.barberaserviceapp.MainActivity.myBookingItemList;

public class MyBookingFragment extends Fragment {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private Retrofit retrofit;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_my_booking, container, false);

        toolbar= view.findViewById(R.id.tool1);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        recyclerView = view.findViewById(R.id.recycler_mybooking);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        if(myBookingItemList.size() != 0){
            attachadapter();
        }else{
            getMyBookings();
        }
        return  view;
    }

    private void getMyBookings() {
        retrofit = RetrofitClientInstance.getRetrofitInstance();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching Today's Bookings");
        progressDialog.show();
        progressDialog.setCancelable(true);
        Call<BookingList> call =jsonPlaceHolderApi.getBookings();
        call.enqueue(new Callback<BookingList>() {
            @Override
            public void onResponse(Call<BookingList> call,Response<BookingList> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(),"Cannot find Bookings",Toast.LENGTH_SHORT).show();
                }
                List<BookingItem> bookingLists = response.body().getBookingItemList();
                if(bookingLists == null){
                    Toast.makeText(getContext(),"Cannot find any bookings",Toast.LENGTH_SHORT).show();
                }else{
                    for(BookingItem bookingItem: bookingLists){
                        if(bookingItem.getStatus() == 1 && bookingItem.getAssignee().equals("debarghya")){
                            myBookingItemList.add(new BookingItem(bookingItem.getName(),bookingItem.getService(),bookingItem.getDate(),bookingItem.getTime(),
                                    bookingItem.getAddress(),bookingItem.getAmount(),bookingItem.getAssignee(),bookingItem.getStatus(),bookingItem.getContact()));
                        }
                    }
                    progressDialog.dismiss();
                }


            }

            @Override
            public void onFailure(@NotNull Call<BookingList> call, @NotNull Throwable t) {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.refresh_menu,menu);
    }

    private void attachadapter() {

    }
}