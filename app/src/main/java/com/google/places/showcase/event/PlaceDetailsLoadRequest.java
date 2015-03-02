package com.google.places.showcase.event;

/**
 * Request for loading place details with placeId
 */
public class PlaceDetailsLoadRequest extends LoadRequest {
    private String mPlaceId;

    public PlaceDetailsLoadRequest(String placeId) {
        super(LoadRequestType.PLACE_DETAILS);
        mPlaceId = placeId;
    }

    public String getPlaceId() {
        return mPlaceId;
    }
}
