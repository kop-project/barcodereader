package com.example.barcodereader.model;

public class QRGeoModel {

    private String lat;

    private String lng;

    private String geoPlace;

    public QRGeoModel() {
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getGeoPlace() {
        return geoPlace;
    }

    public void setGeoPlace(String geoPlace) {
        this.geoPlace = geoPlace;
    }
}
