package com.google.places.showcase.provider;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.places.showcase.R;
import com.google.places.showcase.event.LocationUpdateRequest;
import com.google.places.showcase.event.LocationUpdateResponse;
import com.google.places.showcase.entity.Place;
import com.google.places.showcase.utils.BusProvider;
import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;

/**
 * Default device location provider
 */
public class LocationProvider implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LocationProvider";
    private static final int KILO = 1000;

    private static LocationProvider INSTANCE;
    private GoogleApiClient mGoogleApiClient;
    private Context mContext;
    private boolean mLocationRequested;
    private Location mLastLocation;

    private LocationProvider(Context context) {
        mContext = context;
        // using location from Google services
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Use this method to initialize location provider before use
     * @param context any context
     * @return {@link com.google.places.showcase.provider.LocationProvider} instance
     */
    public static LocationProvider init(Context context) {
        INSTANCE = new LocationProvider(context);
        return INSTANCE;
    }

    /**
     * Get default location provider. Should be initialized before use with
     * {@link com.google.places.showcase.provider.LocationProvider#init(android.content.Context)}
     * @return singleton instance
     */
    public static LocationProvider getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Location provider should be initialized before use");
        }
        return INSTANCE;
    }

    /**
     * Get last known device location. Can be null if location services are not ready.
     */
    public Location getLastLocation() {
        return mLastLocation;
    }

    /**
     * Get formatted distance from current location to target
     * @param targetPlace target {@link Place}
     * @return formatted string with distance
     */
    public String distanceToPlace(Place targetPlace) {
        if (mLastLocation == null) {
            // current location not available
            return "";
        }

        Location targetLocation = new Location("target");
        targetLocation.setLatitude(targetPlace.getLocation().getLatitude());
        targetLocation.setLongitude(targetPlace.getLocation().getLongitude());
        float distance =  mLastLocation.distanceTo(targetLocation);

        // using metric system for now
        DecimalFormat df;
        if (distance >= KILO) {
            String kilometres = mContext.getString(R.string.kilometres);
            df = new DecimalFormat("#.#");
            return df.format(distance / KILO) + " " + kilometres;
        } else {
            String metres = mContext.getString(R.string.metres);
            df = new DecimalFormat("#.");
            return df.format(distance) + " " + metres;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLocationRequested) {
            reportNewLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Google APIs connection suspended, cause : " + cause);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Google APIs connection failed, error : " + connectionResult.getErrorCode());
    }

    @Subscribe
    public void onLocationUpdateRequested(LocationUpdateRequest request) {
        updateLocation();
    }

    private void updateLocation() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mGoogleApiClient.isConnected()) {
            reportNewLocation();
        } else {
            mLocationRequested = true;
            mGoogleApiClient.connect();
        }
    }

    private void reportNewLocation() {
        if (mLastLocation != null) {
            BusProvider.getInstance().post(new LocationUpdateResponse(mLastLocation));
            mLocationRequested = false;
        } else {
            Log.e(TAG, "Got empty location from service");
        }
    }
}
