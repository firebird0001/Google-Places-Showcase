package com.google.places.showcase.event;

import com.google.places.showcase.entity.Location;

/**
 * Place list load request. Can contain text query and/or location.
 */
public class PlacesLoadRequest extends LoadRequest {
    public String mQuery;
    public Location mLocation;

    public PlacesLoadRequest(String query) {
        super(LoadRequestType.PLACE_LIST);
        mQuery = query;
    }

    public PlacesLoadRequest(Location location) {
        super(LoadRequestType.PLACE_LIST);
        mLocation = location;
    }

    public String getQuery() {
        return mQuery;
    }

    public Location getLocation() {
        return mLocation;
    }
}
