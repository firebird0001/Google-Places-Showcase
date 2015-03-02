package com.google.places.showcase.event;

import com.google.places.showcase.entity.PlaceLocation;

/**
 * Place list load request. Can contain text query and/or location.
 */
public class PlacesLoadRequest extends LoadRequest {
    public String mQuery;
    public PlaceLocation mLocation;

    public PlacesLoadRequest(String query, PlaceLocation location) {
        super(LoadRequestType.PLACE_LIST);
        mQuery = query;
        mLocation = location;
    }

    public String getQuery() {
        return mQuery;
    }

    public PlaceLocation getLocation() {
        return mLocation;
    }
}
