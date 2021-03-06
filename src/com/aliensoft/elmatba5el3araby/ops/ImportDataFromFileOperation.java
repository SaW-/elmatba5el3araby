/*
 * Generated by Robotoworks Mechanoid
 */

package com.aliensoft.elmatba5el3araby.ops;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.aliensoft.elmatba5el3araby.Constants;
import com.aliensoft.elmatba5el3araby.content.RecipesRecord;
import com.aliensoft.elmatba5el3araby.content.RecipesDBContract.Ingredients;
import com.aliensoft.elmatba5el3araby.content.RecipesDBContract.Recipes;
import com.aliensoft.elmatba5el3araby.content.RecipesDBContract.Recipes.Builder;
import com.robotoworks.mechanoid.db.SQuery;
import com.robotoworks.mechanoid.ops.OperationContext;
import com.robotoworks.mechanoid.ops.OperationResult;

public class ImportDataFromFileOperation extends AbstractImportDataFromFileOperation {
	private static final String INGREDIENTS = "ingredients";
	private static final String TAG = "ImportDataFromFileOperation";

	@Override
	protected OperationResult onExecute(OperationContext context, Args args) {

		try {
			String rawJson = readFileAsText(context.getApplicationContext(), args.filename);

			JSONArray recipesArray = new JSONArray(rawJson);
			for (int i = 0; i < recipesArray.length(); i++) {
				JSONObject jsonObject = recipesArray.getJSONObject(i);
				Builder recipeBuilder = Recipes.newBuilder();
				recipeBuilder.getValues().putAll(mapRecipetoContentValues(jsonObject));
				Uri recordUri = recipeBuilder.insert(true);

				long recipeId = Long.parseLong(recordUri.getLastPathSegment());
				insertIngredients(recipeId, jsonObject.getJSONArray(INGREDIENTS));
			}

		} catch (Throwable e) {
			OperationResult.error(e);
			e.printStackTrace();
		}

		Log.d(TAG, "Total inserted: " + SQuery.newQuery().count(Recipes.CONTENT_URI));

		return OperationResult.ok();
	}

	private void insertIngredients(long recipeId, JSONArray ingredientsJson) throws JSONException {
		for (int i = 0; i < ingredientsJson.length(); i++) {
			Ingredients.Builder ingredientBuilder = Ingredients.newBuilder();
			ingredientBuilder.getValues().putAll(
					mapIngredientToContentValues(recipeId, ingredientsJson.getString(i)));
			ingredientBuilder.insert();
		}
	}

	private ContentValues mapIngredientToContentValues(long recipeId, String name)
			throws JSONException {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Ingredients.INGREDIENT, name);
		contentValues.put(Ingredients.RECIPE_ID, recipeId);
		return contentValues;
	}

	private ContentValues mapRecipetoContentValues(JSONObject recipe) throws JSONException {
		ContentValues contentValues = new ContentValues();
		for (String field : RecipesRecord.PROJECTION) {
			if (recipe.has(field)) {
				contentValues.put(field, recipe.getString(field));
			}
		}
		if (contentValues.containsKey(Recipes.IMAGE)) {
			contentValues.put(Recipes.IMAGE,
					normalizeImageUrl(contentValues.getAsString(Recipes.IMAGE)));
		}
		if ((contentValues.containsKey(Recipes.COOK_TIME) || contentValues
				.containsKey(Recipes.PREP_TIME)) && !contentValues.containsKey(Recipes.TOTAL_TIME)) {
			int totalTime = 0;
			totalTime += contentValues.containsKey(Recipes.COOK_TIME) ? contentValues
					.getAsInteger(Recipes.COOK_TIME) : 0;
			totalTime += contentValues.containsKey(Recipes.PREP_TIME) ? contentValues
					.getAsInteger(Recipes.PREP_TIME) : 0;
			contentValues.put(Recipes.TOTAL_TIME, totalTime);
		}
		return contentValues;
	}

	private String normalizeImageUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		int idx = url.indexOf("://");
		if (idx != -1) {
			return url;
		}
		return "assets://" + Constants.IMAGES_DIRECTORY_NAME + url;
	}

	private String readFileAsText(Context context, String filename) throws IOException {
		InputStream json = null;
		StringBuilder buf = new StringBuilder();
		BufferedReader in = null;
		try {
			json = context.getAssets().open(filename);
			in = new BufferedReader(new InputStreamReader(json));
			String str;

			while ((str = in.readLine()) != null) {
				buf.append(str);
			}

		} finally {
			if (in != null) {
				in.close();
			}
			if (json != null) {
				json.close();
			}
		}
		return buf.toString();
	}
}
