package com.barbera.barberaserviceapp.ui.bookings;

import java.util.List;

public class BookingModel {

    private String summary;
    private int amount;
    private String date;
    private String time;
    private String address;
    private double distance;
    private String userId;
    private List<String> sidlist;

    public BookingModel(String summary, int amount, String date, String time, String address,
                        double distance, String userId,List<String> sidlist) {
        this.summary = summary;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.address = address;
        this.distance = distance;
        this.userId=userId;
        this.sidlist=sidlist;
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
