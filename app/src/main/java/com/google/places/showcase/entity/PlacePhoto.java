package com.google.places.showcase.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Basic photo container to use with Google Places API
 */
public class PlacePhoto {
    @SerializedName("photo_reference")
    private String mPhotoReference;

    public PlacePhoto(String photoReference) {
        mPhotoReference = photoReference;
    }

    public String getPhotoReference() {
        return mPhotoReference;
    }
}

