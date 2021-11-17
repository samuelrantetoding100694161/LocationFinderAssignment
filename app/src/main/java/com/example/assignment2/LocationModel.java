package com.example.assignment2;

import java.io.Serializable;

public class LocationModel implements Serializable {
    private int id;
    private String address;
    private double latitude;
    private double longitude;

    public LocationModel(int id, double latitude, double longitude, String address) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationModel() {
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "id=" + id +
                ", address=" + address +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
