package com.barbera.barberaserviceapp.network;

import com.barbera.barberaserviceapp.Utils.OtpItem;
import com.barbera.barberaserviceapp.ui.bookings.BookingList;
import com.barbera.barberaserviceapp.ui.service.ServiceItem;
import com.barbera.barberaserviceapp.ui.service.ServiceList;
import com.barbera.barberaserviceapp.ui.service.Success;

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

    //when notification comes, ill call the api for schedule and send a notification if within 1 hour
}
