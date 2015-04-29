package com.aliensoft.elmatba5el3araby.content;

import android.content.SearchRecentSuggestionsProvider;

import com.aliensoft.elmatba5el3araby.R;
import com.aliensoft.elmatba5el3araby.RecipesApplication;

public class SearchRecipeSuggestionsProvider extends SearchRecentSuggestionsProvider {

	public final static String AUTHORITY = getAuthority();
	public final static int MODE = DATABASE_MODE_QUERIES;

	private static String getAuthority() {
		return RecipesApplication.INSTANCE.getString(R.string.search_suggestions_authority);
	}

	public SearchRecipeSuggestionsProvider() {
		setupSuggestions(AUTHORITY, MODE);
	}
}
