package com.google.places.showcase.entity;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

/**
 * Location with latitude and longitude
 */
public class PlaceLocation {
    @SerializedName("lat")
    private double mLatitude;

    @SerializedName("lng")
    private double mLongitude;

    public PlaceLocation() {}

    public PlaceLocation(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public PlaceLocation(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(getLatitude())
                .append(',')
                .append(getLongitude())
                .toString();
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
