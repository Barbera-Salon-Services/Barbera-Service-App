package com.barbera.barberaserviceapp.network;

import com.barbera.barberaserviceapp.ScheduleList;
import com.barbera.barberaserviceapp.Utils.CoinsItem;
import com.barbera.barberaserviceapp.Utils.OtpItem;
import com.barbera.barberaserviceapp.ui.bookings.BookingList;
import com.barbera.barberaserviceapp.ui.profile.ItemList;
import com.barbera.barberaserviceapp.ui.profile.ItemsActivity;
import com.barbera.barberaserviceapp.ui.service.Success;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

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
    //current date dd-mm-yyyy
    @POST("acceptendserv")
    Call<Success> confirmEndOtp(@Body OtpItem otpItem, @Header("Authorization") String token);

    @POST("newlog")
    Call<Void> newLog(@Header("Authorization") String token); //1 am

    @GET("updlog")
    Call<ScheduleList> getSchedule(@Header("Authorization") String token);
    //data: "6"-"18":"n"/"b"
    @GET("getcoins")
    Call<CoinsItem> getCoins(@Header("Authorization") String token);

    @POST("dismode")
    Call<Void> barberLeft(@Body OtpItem userId,@Header("Authorization") String token);

    @POST("getitems")
    Call<ItemList> getItems(@Header("Authorization") String token);
}
