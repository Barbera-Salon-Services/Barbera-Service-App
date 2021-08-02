package com.barbera.barberaserviceapp;

import com.google.gson.annotations.SerializedName;

public class ScheduleList {
    @SerializedName("data")
    private ScheduleItem scheduleItem;

    public ScheduleList(ScheduleItem scheduleItem) {
        this.scheduleItem = scheduleItem;
    }

    public ScheduleItem getScheduleItem() {
        return scheduleItem;
    }
}
