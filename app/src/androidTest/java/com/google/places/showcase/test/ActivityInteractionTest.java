package com.google.places.showcase.test;

import android.app.Instrumentation;
import android.content.IntentFilter;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.google.places.showcase.R;
import com.google.places.showcase.activity.PlaceDetailsActivity;
import com.google.places.showcase.activity.PlacesListActivity;
import com.google.places.showcase.entity.Place;
import com.google.places.showcase.event.PlacesLoadResponse;
import com.google.places.showcase.fragment.PlacesListFragment;
import com.google.places.showcase.utils.CommonUtil;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests that interact with activity
 */
public class ActivityInteractionTest extends ActivityInstrumentationTestCase2<PlacesListActivity> {

    private static final String TEST_ID = "test_id";
    private static final int LOAD_TIMEOUT = 5000;

    public ActivityInteractionTest() {
        super(PlacesListActivity.class);
    }

    /**
     * Test place list load and item click
     */
    public void testPlacesLoaded() {
        PlacesListActivity activity = getActivity();
        final PlacesListFragment fragment = (PlacesListFragment) activity.getSupportFragmentManager()
                .findFragmentById(R.id.places_list_fragment);

        // prepare list of places
        Place place = new Place();
        place.setPlaceId(TEST_ID);
        List<Place> placeList = new ArrayList<Place>(3);
        placeList.add(place);
        placeList.add(place);
        placeList.add(place);

        final PlacesLoadResponse placesResponse = new PlacesLoadResponse(placeList);
        placesResponse.setStatus(CommonUtil.STATUS_OK);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                // simulate place list load
                fragment.onPlacesLoaded(placesResponse);
            }
        });

        getInstrumentation().waitForIdleSync();
        GridView gridView = (GridView) fragment.getView().findViewById(R.id.places_grid);
        ListAdapter adapter = gridView.getAdapter();

        // check if all items were sent
        Assert.assertEquals(placeList.size(), adapter.getCount());

        // build intent filter for explicit intent
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PlaceDetailsActivity.class.getName());
        intentFilter.addCategory("android.intent.category.DEFAULT");

        // setup activity monitor
        Instrumentation.ActivityMonitor monitor = getInstrumentation()
                .addMonitor(intentFilter, null, false);

        // check if monitor is clear
        assertEquals(0, monitor.getHits());

        //  perform click on list
        TouchUtils.clickView(this, gridView.getChildAt(1));
        monitor.waitForActivityWithTimeout(LOAD_TIMEOUT);

        // check if monitor caught the intent
        assertEquals(1, monitor.getHits());
        getInstrumentation().removeMonitor(monitor);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        getInstrumentation().setInTouchMode(true);
    }
}
