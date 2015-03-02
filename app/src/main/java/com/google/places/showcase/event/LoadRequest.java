package com.google.places.showcase.event;

/**
 * Base class for all load requests
 */
public class LoadRequest {
    protected LoadRequestType mType;

    public LoadRequest(LoadRequestType type) {
        mType = type;
    }

    public LoadRequestType getType() {
        return mType;
    }
}
