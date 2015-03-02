package com.google.places.showcase.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.google.places.showcase.R;
import com.google.places.showcase.fragment.PlaceDetailsFragment;

/**
 * Activity to display place details
 */
public class PlaceDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_place_details);

        if (savedInstanceState == null) {
            // First-time init; create fragment to embed in activity.
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment newFragment = PlaceDetailsFragment.newInstance(getIntent().getExtras());
            transaction.replace(R.id.place_details_fragment, newFragment);
            transaction.commit();
        }
    }
}
