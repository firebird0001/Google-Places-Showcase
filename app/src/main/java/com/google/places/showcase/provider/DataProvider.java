package com.google.places.showcase.provider;

import com.google.places.showcase.event.PlaceDetailsLoadResponse;
import com.google.places.showcase.event.PlacesLoadResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Places API interface
 */
public interface DataProvider {
    @GET("/nearbysearch/json?radius=10000")
    void getNearbyPlaces(
            @Query("key") String apiKey,
            @Query("location") String location,
            Callback<PlacesLoadResponse> places
    );

    @GET("/textsearch/json")
    void getPlacesWithText(
            @Query("key") String apiKey,
            @Query("query") String query,
            Callback<PlacesLoadResponse> places
    );

    @GET("/textsearch/json?radius=10000")
    void getPlacesWithTextAndLocation(
            @Query("key") String apiKey,
            @Query("query") String query,
            @Query("location") String location,
            Callback<PlacesLoadResponse> places
    );

    @GET("/details/json")
    void getPlaceDetails(
            @Query("key") String apiKey,
            @Query("placeid") String placeId,
            Callback<PlaceDetailsLoadResponse> placesDetails
    );
}
