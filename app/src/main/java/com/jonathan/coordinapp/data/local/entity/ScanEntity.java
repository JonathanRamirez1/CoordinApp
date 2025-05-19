package com.jonathan.coordinapp.data.local.entity;

public class ScanEntity {
    public long id;
    public String etiqueta;
    public double lat;
    public double lng;
    public String obs;
    public long timestamp;

    public ScanEntity(long id, String etiqueta, double lat, double lng,
                      String obs, long timestamp) {
        this.id = id;
        this.etiqueta = etiqueta;
        this.lat = lat;
        this.lng = lng;
        this.obs = obs;
        this.timestamp = timestamp;
    }
}
