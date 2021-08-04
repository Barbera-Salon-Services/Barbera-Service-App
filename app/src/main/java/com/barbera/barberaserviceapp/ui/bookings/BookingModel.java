package com.barbera.barberaserviceapp.ui.bookings;

import java.util.List;

public class BookingModel {

    private String summary;
    private int amount,totalTime;
    private String date;
    private String time;
    private String address;
    private double distance;
    private String userId;
    private List<String> sidlist;
    private String serviceId;

    public BookingModel(String summary, int amount, String date, String time, String address,
                        double distance, String userId,List<String> sidlist,int totalTime,String serviceId) {
        this.summary = summary;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.address = address;
        this.distance = distance;
        this.userId=userId;
        this.sidlist=sidlist;
        this.totalTime=totalTime;
        this.serviceId=serviceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public List<String> getSidlist() {
        return sidlist;
    }

    public String getUserId() {
        return userId;
    }

    public double getDistance() {
        return distance;
    }

    public String getAddress() {
        return address;
    }

    public String getTime() {
        return time;
    }

    public String getSummary() {
        return summary;
    }

    public int getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

}
