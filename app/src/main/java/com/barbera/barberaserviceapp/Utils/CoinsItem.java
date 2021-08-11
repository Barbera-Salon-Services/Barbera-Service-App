package com.barbera.barberaserviceapp.Utils;

import com.google.gson.annotations.SerializedName;

public class CoinsItem {
    @SerializedName("coins")
    private double coins;

    public CoinsItem(double coins) {
        this.coins = coins;
    }

    public double getCoins() {
        return coins;
    }
}
