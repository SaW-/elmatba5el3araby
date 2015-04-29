// Generated on 2014-01-21 using generator-android 0.1.0
package com.aliensoft.elmatba5el3araby;

import java.io.File;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.aliensoft.elmatba5el3araby.content.RecipesDBContract.Recipes;
import com.aliensoft.elmatba5el3araby.ops.ImportDataFromFileOperation;
import com.aliensoft.elmatba5el3araby.prefs.RecipesPreferences;
import com.aliensoft.elmatba5el3araby.service.NotificationService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.robotoworks.mechanoid.Mechanoid;
import com.robotoworks.mechanoid.ops.Ops;

public class RecipesApplication extends Application {

	public static RecipesApplication INSTANCE = null;

	public static final String TAG = "Recipes";
	public static final DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
			.cacheInMemory(true).cacheOnDisc(true).build();

	public RecipesApplication() {
		if (INSTANCE == null) {
			INSTANCE = this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Mechanoid.init(this);

		ImageLoader.getInstance().init(
				new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(
						defaultDisplayImageOptions).build());

		startService(NotificationService.getIntentForAlarm(this));

		boolean shouldSync = true;
		try {
			PackageManager pm = getPackageManager();
			ApplicationInfo appInfo = pm.getApplicationInfo(getPackageName(), 0);
			String appFile = appInfo.sourceDir;
			long installed = new File(appFile).lastModified(); // Epoch Time

			if (RecipesPreferences.getInstance().getFirstLaunchTimestamp() != installed) {
				Log.d(TAG, "First launch after installation. Should sync recipes");
				RecipesPreferences.getInstance().updateFirstLaunchTimestamp(installed);
			} else {
				Log.d(TAG, "Not first launch after installation. Skip sync recipes");
				shouldSync = false;
			}
		} catch (Exception e) {
			Log.e(TAG, "Error while reading install date", e);
		}

		if (shouldSync) {
			Recipes.delete();
			Ops.execute(ImportDataFromFileOperation.newIntent("recipes.json"));
		}
	}
}