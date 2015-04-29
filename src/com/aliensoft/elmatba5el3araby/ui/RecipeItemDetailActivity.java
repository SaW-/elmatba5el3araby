package com.aliensoft.elmatba5el3araby.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.aliensoft.elmatba5el3araby.R;
import com.aliensoft.elmatba5el3araby.fragment.RecipeItemDetailFragment;

/**
 * An activity representing a single RecipeItem detail screen. This activity is only used on handset
 * devices. On tablet-size devices, item details are presented side-by-side with a list of items in
 * a {@link RecipeItemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than a
 * {@link RecipeItemDetailFragment}.
 */
public class RecipeItemDetailActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipeitem_detail);

		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			long itemId = getIntent().getLongExtra(RecipeItemDetailFragment.ARG_ITEM_ID, -1);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.recipeitem_detail_container, RecipeItemDetailFragment.create(itemId))
					.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this, RecipeItemListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
