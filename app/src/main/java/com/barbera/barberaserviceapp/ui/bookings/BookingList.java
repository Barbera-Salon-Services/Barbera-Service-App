package com.barbera.barberaserviceapp.ui.bookings;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingList {
    @SerializedName("done")
    List<BookingItem> list;
    @SerializedName("not_done")
    List<BookingItem> list1;
    @SerializedName("mode")
    private String mode;

    public BookingList(List<BookingItem> list, List<BookingItem> list1, String mode) {
        this.list = list;
        this.list1 = list1;
        this.mode = mode;
    }

    public List<BookingItem> getList1() {
        return list1;
    }

    public String isMode() {
        return mode;
    }

    public List<BookingItem> getList() {
        return list;
    }
}
