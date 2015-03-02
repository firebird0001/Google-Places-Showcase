package com.google.places.showcase.test;

import com.google.places.showcase.event.LoadRequestType;
import com.google.places.showcase.event.PlacesLoadRequest;
import com.google.places.showcase.provider.DataProvider;
import com.google.places.showcase.provider.PlacesProvider;
import com.google.places.showcase.test.mock.MockClient;
import com.google.places.showcase.utils.CancellableJsonCallback;

import junit.framework.TestCase;

import retrofit.RestAdapter;

/**
 * Places provider test
 */
public class PlacesProviderTest extends TestCase {

    private static final String TEST_QUERY1 = "query";
    private static final String TEST_QUERY2 = "some other query";
    private static final String TEST_URL = "http://google.com";

    /** New requests of same type should cancel previous */
    public void testMultipleRequests() {
        // init places provider
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new MockClient())
                .setEndpoint(TEST_URL)
                .build();
        PlacesProvider placesProvider = new PlacesProvider(restAdapter.create(DataProvider.class));

        // send some requests and check cancellation status
        PlacesLoadRequest request1 = new PlacesLoadRequest(TEST_QUERY1, null);
        PlacesLoadRequest request2 = new PlacesLoadRequest(TEST_QUERY2, null);
        PlacesLoadRequest request3 = new PlacesLoadRequest(TEST_QUERY2, null);

        placesProvider.onLoadPlaces(request1);
        CancellableJsonCallback callback1 = placesProvider.getPendingRequests()
                .get(LoadRequestType.PLACE_LIST);
        assertNotNull(callback1);
        assertFalse(callback1.isCancelled());

        placesProvider.onLoadPlaces(request2);
        CancellableJsonCallback callback2 = placesProvider.getPendingRequests()
                .get(LoadRequestType.PLACE_LIST);
        assertNotNull(callback2);
        assertTrue(callback1.isCancelled());
        assertFalse(callback2.isCancelled());

        placesProvider.onLoadPlaces(request3);
        CancellableJsonCallback callback3 = placesProvider.getPendingRequests()
                .get(LoadRequestType.PLACE_LIST);
        assertNotNull(callback3);
        assertTrue(callback1.isCancelled());
        assertTrue(callback2.isCancelled());
        assertFalse(callback3.isCancelled());
    }
}
