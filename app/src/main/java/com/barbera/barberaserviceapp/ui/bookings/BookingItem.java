package com.barbera.barberaserviceapp.ui.bookings;

import com.barbera.barberaserviceapp.Utils.ServiceItem;
import com.google.gson.annotations.SerializedName;

public class BookingItem {
    @SerializedName("Timestamp")
    private String timestamp;
    @SerializedName("user_lat")
    private double lat;
    @SerializedName("userId")
    private String userId;
    @SerializedName("user_lon")
    private double lon;
    @SerializedName("user_add")
    private String add;
    @SerializedName("distance")
    private double distance;
    @SerializedName("service")
    private ServiceItem service;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("date")
    private String date;
    @SerializedName("slot")
    private String slot;
    @SerializedName("serviceId")
    private String serviceId;
    @SerializedName("service_status")
    private String status;
    @SerializedName("userphone")
    private String phone;

    public BookingItem(double lat, double lon, String add, double distance, ServiceItem service,String timestamp,
                       int quantity,String date,String slot,String userId,String serviceId,String status,String phone) {
        this.lat = lat;
        this.lon = lon;
        this.add = add;
        this.distance = distance;
        this.service = service;
        this.timestamp=timestamp;
        this.quantity=quantity;
        this.date=date;
        this.slot=slot;
        this.userId=userId;
        this.serviceId=serviceId;
        this.status=status;
        this.phone=phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSlot() {
        return slot;
    }

    public String getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getAdd() {
        return add;
    }

    public double getDistance() {
        return distance;
    }

    public ServiceItem getService() {
        return service;
    }
}
