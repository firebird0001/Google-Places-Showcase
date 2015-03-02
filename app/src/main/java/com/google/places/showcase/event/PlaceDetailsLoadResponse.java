package com.google.places.showcase.event;

import com.google.places.showcase.entity.Place;

/**
 * Place detail load response with filled {@link Place}
 */
public class PlaceDetailsLoadResponse extends LoadResponse {
    private Place mPlace;

    public PlaceDetailsLoadResponse(Place place) {
        mPlace = place;
    }

    public Place getPlace() {
        return mPlace;
    }
}
