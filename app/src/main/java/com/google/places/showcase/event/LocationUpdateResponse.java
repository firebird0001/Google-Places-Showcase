package com.google.places.showcase.event;

import android.location.Location;

/**
 * Location update response. Contains last known location.
 */
public class LocationUpdateResponse extends LoadResponse {
    Location mLocation;

    public LocationUpdateResponse(Location location) {
        mLocation = location;
    }

    public Location getLocation() {
        return mLocation;
    }
}
