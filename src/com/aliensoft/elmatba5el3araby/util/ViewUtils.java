package com.aliensoft.elmatba5el3araby.util;

import android.app.Activity;
import android.widget.ImageView;

import com.aliensoft.elmatba5el3araby.Constants;
import com.aliensoft.elmatba5el3araby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ViewUtils implements Constants {

	private static final DisplayImageOptions options = new DisplayImageOptions.Builder()
	.cacheInMemory(true).cacheOnDisc(true).resetViewBeforeLoading(true)
	.showImageForEmptyUri(R.drawable.empty).build();

	public static void displayImageFromAssets(final Activity context, final ImageView imageView,
			final String imageUrl) {
		ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
	}

}
