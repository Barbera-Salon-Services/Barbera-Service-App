package com.barbera.barberaserviceapp.network;

import com.google.gson.annotations.SerializedName;

public class Register {
    @SerializedName("phone")
    private String phone;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String token;

    @SerializedName("otp")
    private String otp;

    @SerializedName("gender")
    private String gender;

    @SerializedName("name")
    private String name;

    @SerializedName("password")
    private String password;
    @SerializedName("address")
    private String address;
    @SerializedName("role")
    private String role;
    @SerializedName("first")
    private boolean first;

    public Register(String phone, String otp, String gender, String name, String password, String address, String role,
                    String message, double latitude, double longitude,boolean first) {
        this.phone = phone;
        this.otp = otp;
        this.gender = gender;
        this.name = name;
        this.password = password;
        this.address = address;
        this.role = role;
        this.message=message;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public boolean isFirst() {
        return first;
    }

    public Register(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getMessage() {
        return message;
    }

    public String getAddress() {
        return address;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }

    public String getOtp() {
        return otp;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
