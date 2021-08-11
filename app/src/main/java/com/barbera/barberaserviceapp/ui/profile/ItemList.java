package com.barbera.barberaserviceapp.ui.profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemList {
    @SerializedName("data")
    private List<ItemModel> list;

    public ItemList(List<ItemModel> list) {
        this.list = list;
    }

    public List<ItemModel> getList() {
        return list;
    }
}
