package com.google.places.showcase.utils;

import com.google.gson.JsonElement;
import com.google.places.showcase.event.ApiErrorEvent;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Retrofit extension to be able to cancel posted callbacks
 */
public abstract class CancellableJsonCallback implements Callback<JsonElement> {
    private volatile boolean mCancelled;

    public void cancel() {
        mCancelled = true;
    }

    public boolean isCancelled() {
        return mCancelled;
    }

    @Override
    public void failure(RetrofitError error) {
        BusProvider.getInstance().post(new ApiErrorEvent(error));
    }
}
