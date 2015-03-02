package com.google.places.showcase.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Place class to use with Google Places APIs
 */
public class Place {

    @SerializedName("formatted_address")
    private String address;

    @SerializedName("icon")
    private String icon;

    @SerializedName("id")
    private String id;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("name")
    private String name;

    @SerializedName("types")
    private List<String> types;

    @SerializedName("photos")
    private List<PlacePhoto> photos;

    @SerializedName("rating")
    private double rating;

    @SerializedName("international_phone_number")
    private String phone;

    @SerializedName("website")
    private String website;

    @SerializedName("url")
    private String url;

    @SerializedName("geometry")
    private Geometry geometry;

    // getters and setters
    public List<PlacePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PlacePhoto> photos) {
        this.photos = photos;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public PlaceLocation getLocation() {
        return geometry.location;
    }

    public void setLocation(PlaceLocation location) {
        if (this.geometry == null) {
            this.geometry = new Geometry();
        }
        this.geometry.location = location;
    }
}
