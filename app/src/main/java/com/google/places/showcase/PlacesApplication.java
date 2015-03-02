package com.google.places.showcase;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.places.showcase.event.ApiErrorEvent;
import com.google.places.showcase.provider.DataProvider;
import com.google.places.showcase.provider.LocationProvider;
import com.google.places.showcase.provider.PlacesProvider;
import com.google.places.showcase.utils.BusProvider;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Main application
 */
public class PlacesApplication extends Application {

    private static final String TAG = "PlacesApplication";
    private static final String API_URL = "https://maps.googleapis.com/maps/api/place";

    private static final int HTTP_CACHE_SIZE = 4 * 1024;

    private PlacesProvider mPlacesProvider;
    private LocationProvider mLocationProvider;

    @Override
    public void onCreate() {
        super.onCreate();

        BusProvider.getInstance().register(this);

        mPlacesProvider = new PlacesProvider(buildProvider());
        BusProvider.getInstance().register(mPlacesProvider);

        mLocationProvider = LocationProvider.init(this);
        BusProvider.getInstance().register(mLocationProvider);
    }

    private DataProvider buildProvider() {
        // create an HTTP client that uses a cache on the file system.
        OkHttpClient okHttpClient = new OkHttpClient();
        File cacheDir = new File(getCacheDir(), UUID.randomUUID().toString());
        Cache cache = null;
        try {
            cache = new Cache(cacheDir, HTTP_CACHE_SIZE);
        } catch (IOException e) {
            Log.e(TAG, "Error creating HTTP cache", e);
        }
        if (cache != null) {
            okHttpClient.setCache(cache);
        }

        // create rest adapter
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(API_URL)
                .setConverter(new GsonConverter(new Gson()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        return restAdapter.create(DataProvider.class);
    }

    /** Visible for test purposes */
    public PlacesProvider getPlacesProvider() {
        return mPlacesProvider;
    }

    /** Visible for test purposes */
    public LocationProvider getLocationProvider() {
        return mLocationProvider;
    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {
        Toast.makeText(this, getString(R.string.request_error), Toast.LENGTH_LONG).show();
        Log.e(TAG, event.getErrorMessage());
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }
}
