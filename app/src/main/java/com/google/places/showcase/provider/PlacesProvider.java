package com.google.places.showcase.provider;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.places.showcase.BuildConfig;
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
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Default provider to use with Google Places API
 */
public class PlacesProvider {
    private static final String TAG = "PlacesService";
    private static final String API_URL = "https://maps.googleapis.com/maps/api/place";
    private static final String HTTP_CACHE_DIR = "http_cache";
    private static final int HTTP_CACHE_SIZE = 4 * 1024;

    private static PlacesProvider INSTANCE;
    private DataProvider mApi;
    // store all pending callbacks so we can ignore their results
    // if new similar request will be posted
    private Map<LoadRequestType, CancellableJsonCallback> mPendingCallbacks;

    private PlacesProvider(DataProvider api) {
        mApi = api;
        mPendingCallbacks = new HashMap<LoadRequestType, CancellableJsonCallback>();
    }

    /**
     * Use this method to initialize location provider before use
     * @param context any context
     * @return {@link com.google.places.showcase.provider.PlacesProvider} instance
     */
    public static PlacesProvider init(Context context) {
        INSTANCE = new PlacesProvider(buildProvider(context));
        return INSTANCE;
    }

    /**
     * Use this method to initialize location provider before use
     * @param api retrofit interface
     * @return {@link com.google.places.showcase.provider.PlacesProvider} instance
     */
    public static PlacesProvider init(DataProvider api) {
        INSTANCE = new PlacesProvider(api);
        return INSTANCE;
    }

    /**
     * Get default location provider. Should be initialized before use with
     * {@link com.google.places.showcase.provider.PlacesProvider#init(Context)}
     * @return singleton instance
     */
    public static PlacesProvider getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Places provider should be initialized before use");
        }
        return INSTANCE;
    }

    public void loadPlaces(PlacesLoadRequest request) {
        // cancel already submitted request callback
        cancelSimilarRequests(request);

        // place new request
        PlacesCallback callback = new PlacesCallback<PlacesLoadResponse>();
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

    public void loadPlaceDetails(PlaceDetailsLoadRequest request) {
        // cancel already submitted request callback
        cancelSimilarRequests(request);

        // place new request
        PlacesCallback callback = new PlacesCallback<PlaceDetailsLoadResponse>();
        mApi.getPlaceDetails(CommonUtil.API_KEY, request.getPlaceId(), callback);

        // save callback
        mPendingCallbacks.put(LoadRequestType.PLACE_LIST, callback);
    }

    /** Check if similar requests are already posted and cancel them */
    private void cancelSimilarRequests(LoadRequest request) {
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

    private static class PlacesCallback<T> extends CancellableJsonCallback<T> {

        PlacesCallback() {
        }

        @Override
        public void success(T response, Response rawResponse) {
            if (isCancelled()) {
                return;
            }
            BusProvider.getInstance().post(response);
        }
    }

    private static DataProvider buildProvider(Context context) {
        // create an HTTP client that uses a cache on the file system.
        OkHttpClient okHttpClient = new OkHttpClient();
        File cacheDir = new File(context.getCacheDir(), HTTP_CACHE_DIR);
        Cache cache = null;
        try {
            cache = new Cache(cacheDir, HTTP_CACHE_SIZE);
        } catch (IOException e) {
            Log.e(TAG, "Error creating HTTP cache", e);
        }
        if (cache != null) {
            okHttpClient.setCache(cache);
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(PlaceDetailsLoadResponse.class, new PlaceDetailsDeserializer())
                .registerTypeAdapter(PlacesLoadResponse.class, new PlaceListDeserializer())
                .create();

        // create rest adapter
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(API_URL)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL
                        : RestAdapter.LogLevel.BASIC)
                .build();

        return restAdapter.create(DataProvider.class);
    }
}
