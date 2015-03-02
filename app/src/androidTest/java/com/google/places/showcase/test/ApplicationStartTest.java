package com.google.places.showcase.test;

import android.test.ApplicationTestCase;
import android.test.UiThreadTest;

import com.google.places.showcase.PlacesApplication;

/**
 * Application start test
 */
public class ApplicationStartTest extends ApplicationTestCase <PlacesApplication> {

    public ApplicationStartTest() {
        super(PlacesApplication.class);
    }

    @UiThreadTest
    public void testInit() {
        createApplication();

        // check if application created correctly
        assertNotNull(getApplication());

        // check if providers initialize correctly
        assertNotNull(getApplication().getLocationProvider());
        assertNotNull(getApplication().getPlacesProvider());
    }
}
