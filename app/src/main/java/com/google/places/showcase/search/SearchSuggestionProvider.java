package com.google.places.showcase.search;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Content provider to save search history
 */
public class SearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY
            = "com.google.places.showcase.search.SearchSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
