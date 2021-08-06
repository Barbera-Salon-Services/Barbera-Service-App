package com.barbera.barberaserviceapp.network;

import com.barbera.barberaserviceapp.ScheduleList;
import com.barbera.barberaserviceapp.Utils.CoinsItem;
import com.barbera.barberaserviceapp.Utils.OtpItem;
import com.barbera.barberaserviceapp.ui.bookings.BookingList;
import com.barbera.barberaserviceapp.ui.service.ServiceItem;
import com.barbera.barberaserviceapp.ui.service.ServiceList;
import com.barbera.barberaserviceapp.ui.service.Success;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {
    @POST("loginphone")
    Call<Register> getToken(@Body Register register);

    @POST("loginotp")
    Call<Register> checkOtp(@Body Register register, @Header("Authorization") String token);

    @POST("address")
    Call<Void> updateAddress(@Body Register register,@Header("Authorization") String token);

    @GET("getbookbarb")
    Call<BookingList> getBookings(@Header("Authorization") String token);

    @POST("acceptstartserv")
    Call<Success> confirmStartOtp(@Body OtpItem otpItem, @Header("Authorization") String token);

    @POST("acceptendserv")
    Call<Success> confirmEndOtp(@Body OtpItem otpItem, @Header("Authorization") String token);

    @POST("newlog")
    Call<Void> newLog(@Header("Authorization") String token); //1 am

    @GET("updlog")
    Call<ScheduleList> getSchedule(@Header("Authorization") String token);
    //data: "6"-"18":"n"/"b"
    @GET("getcoins")
    Call<CoinsItem> getCoins(@Header("Authorization") String token);

}
