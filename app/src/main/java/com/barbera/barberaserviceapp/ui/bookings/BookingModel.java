package com.barbera.barberaserviceapp.ui.bookings;

public class BookingModel {

    private String summary;
    private int amount;
    private String date;
    private String time;
    private String address;
    private double distance;

    public BookingModel(String summary, int amount, String date, String time, String address, double distance) {
        this.summary = summary;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.address = address;
        this.distance = distance;
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
