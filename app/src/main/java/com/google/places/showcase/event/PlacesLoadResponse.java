package com.google.places.showcase.event;

import com.google.places.showcase.entity.Place;

import java.util.List;

/**
 * Place list load response. Contains corresponding  list of {@link Place}
 */
public class PlacesLoadResponse extends LoadResponse {
    private List<Place> mPlaces;

    public PlacesLoadResponse(List<Place> places) {
        mPlaces = places;
    }

    public List<Place> getPlaces() {
        return mPlaces;
    }

    public void setPlaces(List<Place> places) {
        mPlaces = places;
    }
}
