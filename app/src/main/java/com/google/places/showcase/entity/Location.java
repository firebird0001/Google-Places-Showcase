package com.google.places.showcase.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Location with latitude and longitude
 */
public class Location {
    @SerializedName("lat")
    private double mLatitude;

    @SerializedName("lng")
    private double mLongitude;

    public Location() {}

    public Location(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public Location(android.location.Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    // getters and setters
    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }
}
