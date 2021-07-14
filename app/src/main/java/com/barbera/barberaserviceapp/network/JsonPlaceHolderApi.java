package com.barbera.barberaserviceapp.network;

import com.barbera.barberaserviceapp.ui.bookings.BookingList;
import com.barbera.barberaserviceapp.ui.service.ServiceItem;
import com.barbera.barberaserviceapp.ui.service.ServiceList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {
    @POST("exec")
    Call<ServiceItem> updateService(@Body ServiceItem  serviceItem);

    @GET("exec")
    Call<BookingList> getBookings();

    @POST("exec")
    Call<String> updateAssignee(@Query("name") String name, @Query("service")String service, @Query("time")int Time, @Query("address") String address
    , @Query("amount")String amount,@Query("assignedTo") String assignee,@Query("action")String action,@Query("status")int status,@Query("id")int id, @Query("date")String date, @Query("contact")String contact);

    @POST("loginphone")
    Call<Register> getToken(@Body Register register);

    @POST("loginotp")
    Call<Register> checkOtp(@Body Register register, @Header("Authorization") String token);

    @POST("address")
    Call<Void> updateAddress(@Body Register register,@Header("Authorization") String token);

    @GET("getbookbarb")
    Call<BookingList> getBookings(@Header("Authorization") String token);
}
