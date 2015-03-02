package com.google.places.showcase.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.places.showcase.R;
import com.google.places.showcase.event.LocationUpdateRequest;
import com.google.places.showcase.event.LocationUpdateResponse;
import com.google.places.showcase.event.PlacesLoadRequest;
import com.google.places.showcase.entity.Location;
import com.google.places.showcase.provider.LocationProvider;
import com.google.places.showcase.search.SearchSuggestionProvider;
import com.google.places.showcase.utils.BusProvider;
import com.squareup.otto.Subscribe;

/**
 * Activity to display place grid
 */
public class PlacesListActivity extends ActionBarActivity implements SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener {

    private static final String TAG = "PlacesActivity";
    private static final String KEY_FIRST_SEARCH = "firstSearch";
    private static final String KEY_QUERY = "query";
    private static final String KEY_SEARCH_OPEN = "searchOpen";

    private SearchView mSearchView;
    private CharSequence mCurrentQuery;
    private boolean mSearchOpen;
    private boolean mFirstSearchDone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // don't show keyboard on activity resume
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // restore instance state
        if (savedInstanceState != null) {
            mCurrentQuery = savedInstanceState.getCharSequence(KEY_QUERY);
            mFirstSearchDone = savedInstanceState.getBoolean(KEY_FIRST_SEARCH);
            mSearchOpen = savedInstanceState.getBoolean(KEY_SEARCH_OPEN);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // handle new search
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (!TextUtils.isEmpty(query)) {
                // start new request
                performSearch(query, null);

                // update search view in case search history entry was selected
                mSearchView.setQuery(query, false);
                mSearchView.clearFocus();

                // save query
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
                        getApplicationContext(), SearchSuggestionProvider.AUTHORITY,
                        SearchSuggestionProvider.MODE);
                suggestions.saveRecentQuery(query, null);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_places_list, menu);

        // associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, this);

        // restore search view state
        if (mSearchOpen) {
            searchMenuItem.expandActionView();
            mSearchView.onActionViewExpanded();
            mSearchView.clearFocus();
            if (!TextUtils.isEmpty(mCurrentQuery)) {
                mSearchView.setQuery(mCurrentQuery, false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                item.expandActionView();
                return true;
            case R.id.action_nearby:
                android.location.Location location = LocationProvider.getInstance().getLastLocation();
                if (location != null) {
                    performSearch(null, new com.google.places.showcase.entity.Location(location));
                } else {
                    Toast.makeText(this, getString(R.string.location_not_available),
                            Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_about:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_QUERY, mSearchView.getQuery());
        outState.putBoolean(KEY_FIRST_SEARCH, mFirstSearchDone);
        outState.putBoolean(KEY_SEARCH_OPEN, mSearchOpen);
    }


    @Override
    public void onStart() {
        super.onStart();

        BusProvider.getInstance().register(this);

        // request location update
        BusProvider.getInstance().post(new LocationUpdateRequest());
    }

    @Override
    public void onStop() {
        super.onStop();

        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onLocationUpdated(LocationUpdateResponse event) {
        // if no text query is entered - show nearby places
        if (!mFirstSearchDone) {
            performSearch(null,
                    new com.google.places.showcase.entity.Location(event.getLocation()));
        }
    }

    private void performSearch(String query, Location location) {
        // support only separate requests for now
        PlacesLoadRequest loadRequest = (query != null)
                ? new PlacesLoadRequest(query) : new PlacesLoadRequest(location);
        BusProvider.getInstance().post(loadRequest);
        mFirstSearchDone = true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        hideKeyboard();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void hideKeyboard() {
        try {
            InputMethodManager inputManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException exception) {
            // TODO on some devices this causes NPE O_o
            Log.e(TAG, "Failed to hide keyboard", exception);
        }
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        mSearchView.onActionViewExpanded();
        mSearchOpen = true;
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        mSearchOpen = false;
        hideKeyboard();
        return true;
    }
}
