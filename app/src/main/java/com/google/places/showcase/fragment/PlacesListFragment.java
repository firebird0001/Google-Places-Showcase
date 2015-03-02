package com.google.places.showcase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.places.showcase.activity.PlaceDetailsActivity;
import com.google.places.showcase.adapter.PlacesGridAdapter;
import com.google.places.showcase.R;
import com.google.places.showcase.event.ApiErrorEvent;
import com.google.places.showcase.event.PlacesLoadRequest;
import com.google.places.showcase.event.PlacesLoadResponse;
import com.google.places.showcase.entity.Place;
import com.google.places.showcase.provider.LocationProvider;
import com.google.places.showcase.utils.BusProvider;
import com.google.places.showcase.utils.CommonUtil;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Fragment used to display a grid of place items.
 */
public class PlacesListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "PlacesListFragment";

    private GridView mPlacesGridView;
    private PlacesGridAdapter mGridAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain fragment during configuration changes
        setRetainInstance(true);

        mGridAdapter = new PlacesGridAdapter(this, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_places_grid, null);

        mPlacesGridView = (GridView) rootView.findViewById(R.id.places_grid);
        mPlacesGridView.setAdapter(mGridAdapter);
        mPlacesGridView.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        BusProvider.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        BusProvider.getInstance().unregister(this);
        CommonUtil.hideProgressDialog(getFragmentManager());
    }

    @Subscribe
    public void onLoadPlaces(PlacesLoadRequest request) {
        CommonUtil.showProgressDialog(getFragmentManager());
    }

    @Subscribe
    public void onPlacesLoaded(PlacesLoadResponse response) {
        if (!response.isBadResponse()) {
            CommonUtil.sortNearestFirst(response.getPlaces(),
                    LocationProvider.getInstance().getLastLocation());
            mGridAdapter.updateData(response.getPlaces());
        } else {
            CommonUtil.reportBadResponse(this.getActivity(), response);
        }
        CommonUtil.hideProgressDialog(getFragmentManager());
    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {
        CommonUtil.hideProgressDialog(getFragmentManager());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailsIntent = new Intent(getActivity(), PlaceDetailsActivity.class);
        detailsIntent.putExtra(CommonUtil.KEY_PLACE_ID, ((Place) mGridAdapter.getItem(position)).getPlaceId());
        startActivity(detailsIntent);
    }
}
