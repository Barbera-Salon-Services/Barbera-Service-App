package com.barbera.barberaserviceapp.ui.service;

import com.google.gson.annotations.SerializedName;

public class Success {
    @SerializedName("success")
    private boolean success;

    public Success(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
