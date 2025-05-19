package com.jonathan.coordinapp.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class ScanModel {

    @SerializedName("Correcto")
    public String correcto;

    @SerializedName("data")
    public String rawData;
}
