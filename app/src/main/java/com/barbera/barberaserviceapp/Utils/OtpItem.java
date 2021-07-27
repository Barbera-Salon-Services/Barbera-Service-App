package com.barbera.barberaserviceapp.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OtpItem {
    @SerializedName("otp")
    private String otp;
    @SerializedName("serviceId")
    private List<String> serviceList;
    @SerializedName("userId")
    private String userId;

    public OtpItem(String otp, List<String> serviceList, String userId) {
        this.otp = otp;
        this.serviceList = serviceList;
        this.userId = userId;
    }

    public String getOtp() {
        return otp;
    }

    public List<String> getServiceList() {
        return serviceList;
    }

    public String getUserId() {
        return userId;
    }
}
