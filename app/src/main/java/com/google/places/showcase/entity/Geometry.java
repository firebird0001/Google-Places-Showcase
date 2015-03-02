package com.google.places.showcase.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Basic geometry class used in Google Places API
 */
public class Geometry {
    @SerializedName("location")
    PlaceLocation location;
}
