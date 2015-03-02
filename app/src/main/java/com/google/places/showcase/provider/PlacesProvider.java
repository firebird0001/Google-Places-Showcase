package com.google.places.showcase.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.places.showcase.event.ApiErrorEvent;
import com.google.places.showcase.event.LoadRequest;
import com.google.places.showcase.event.LoadRequestType;
import com.google.places.showcase.event.PlaceDetailsLoadRequest;
import com.google.places.showcase.event.PlaceDetailsLoadResponse;
import com.google.places.showcase.event.PlacesLoadRequest;
import com.google.places.showcase.event.PlacesLoadResponse;
import com.google.places.showcase.utils.BusProvider;
import com.google.places.showcase.utils.CancellableJsonCallback;
import com.google.places.showcase.utils.CommonUtil;
import com.google.places.showcase.utils.PlaceDetailsDeserializer;
import com.google.places.showcase.utils.PlaceListDeserializer;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

import retrofit.client.Response;

/**
 * Default provider to use with Google Places API
 */
public class PlacesProvider {
    private static final String TAG = "PlacesService";

    private DataProvider mApi;
    // store all pending callbacks so we can ignore their results
    // if new similar request will be posted
    private Map<LoadRequestType, CancellableJsonCallback> mPendingCallbacks;

    public PlacesProvider(DataProvider api) {
        mApi = api;
        mPendingCallbacks = new HashMap<LoadRequestType, CancellableJsonCallback>();
    }

    @Subscribe
    public void onLoadPlaces(PlacesLoadRequest request) {
        // cancel already submitted request callback
        cancelSameRequests(request);

        // place new request
        PlacesCallback callback = new PlacesCallback(PlacesLoadResponse.class, new PlaceListDeserializer());
        if (request.getQuery() != null) {
            if (request.getLocation() != null) {
                mApi.getPlacesWithTextAndLocation(CommonUtil.API_KEY, request.getQuery(),
                        request.getLocation().toString(), callback);
            } else {
                mApi.getPlacesWithText(CommonUtil.API_KEY, request.getQuery(), callback);
            }
        } else if (request.getLocation() != null) {
            mApi.getNearbyPlaces(CommonUtil.API_KEY, request.getLocation().toString(), callback);
        } else {
            throw new IllegalArgumentException("No params specified in load places query");
        }

        // save callback
        mPendingCallbacks.put(LoadRequestType.PLACE_LIST, callback);
    }

    @Subscribe
    public void onLoadPlaceDetails(PlaceDetailsLoadRequest request) {
        // cancel already submitted request callback
        cancelSameRequests(request);

        // place new request
        PlacesCallback callback = new PlacesCallback(PlaceDetailsLoadResponse.class,
                new PlaceDetailsDeserializer());
        mApi.getPlaceDetails(CommonUtil.API_KEY, request.getPlaceId(), callback);

        // save callback
        mPendingCallbacks.put(LoadRequestType.PLACE_LIST, callback);
    }

    /** Check if similar requests are already posted and cancel them */
    private void cancelSameRequests(LoadRequest request) {
        CancellableJsonCallback callback = mPendingCallbacks.get(request.getType());
        if (callback != null) {
            callback.cancel();
            mPendingCallbacks.remove(request.getType());
        }
    }

    /** Get all pending requests, currently used for tests only */
    public Map<LoadRequestType, CancellableJsonCallback> getPendingRequests() {
        return mPendingCallbacks;
    }

    private static class PlacesCallback extends CancellableJsonCallback {

        private Class mResponseClass;
        private JsonDeserializer mDeserializer;

        PlacesCallback(Class responseClass, JsonDeserializer deserializer) {
            mResponseClass = responseClass;
            mDeserializer = deserializer;
        }

        @Override
        public void success(JsonElement response, Response rawResponse) {
            if (isCancelled()) {
                return;
            }

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(mResponseClass, mDeserializer)
                    .create();

            try {
                BusProvider.getInstance().post(gson.fromJson(response, mResponseClass));
            } catch (JsonSyntaxException syntaxException) {
                BusProvider.getInstance().post(new ApiErrorEvent(syntaxException));
            }
        }
    }
}
