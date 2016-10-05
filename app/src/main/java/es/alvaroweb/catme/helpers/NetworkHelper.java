/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import es.alvaroweb.catme.data.CatmeDatabase;
import es.alvaroweb.catme.data.CatmeProvider;
import es.alvaroweb.catme.model.Categories;
import es.alvaroweb.catme.model.Category;
import es.alvaroweb.catme.model.Image;
import es.alvaroweb.catme.model.ResponseImages;
import es.alvaroweb.catme.model.VoteResponse;
import es.alvaroweb.catme.network.ApiConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * TODO: Create JavaDoc
 */
public class NetworkHelper {
    private static final String DEBUG_TAG = NetworkHelper.class.getSimpleName();
    private static final ApiConnection api = ApiConnection.getInstance();

    public static void loadCategories(final Context context){
        final Uri uri = CatmeProvider.Categories.CONTENT_URI;
        api.fetchCategories(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                ContentValues values = new ContentValues();
                context.getContentResolver().delete(uri, null, null);
                for(Category c : response.body().getCategories()){
                    values.put(CatmeDatabase.CategoriesColumns._ID, c.getId());
                    values.put(CatmeDatabase.CategoriesColumns.NAME, c.getName());
                    context.getContentResolver().insert(uri, values);
                }
                Log.d(DEBUG_TAG, "updated categories");
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                logError(t);
            }
        });
    }

    public static void loadImagesVoteData(final Context context){
        api.fetchImagesVotedData(new Callback<ResponseImages>() {
            @Override
            public void onResponse(Call<ResponseImages> call, Response<ResponseImages> response) {
                for(Image i : response.body().getImageList()){
                    ContentHelper.updateOrInsertImage(context.getContentResolver(), i);
                }
            }

            @Override
            public void onFailure(Call<ResponseImages> call, Throwable t) {
                logError(t);
            }
        });
    }

    public static void loadImagesFavoriteData(final Context context){
        api.fetchImagesFavoritedData(new Callback<ResponseImages>() {
            @Override
            public void onResponse(Call<ResponseImages> call, Response<ResponseImages> response) {
                ContentValues values = new ContentValues();
                for(Image i : response.body().getImageList()){
                    ContentHelper.updateOrInsertFavorite(context.getContentResolver(), i);
                }
            }

            @Override
            public void onFailure(Call<ResponseImages> call, Throwable t) {
                logError(t);
            }
        });
    }

    private static void logError(Throwable t){
        Log.d(DEBUG_TAG, "network failure: " + t.getMessage());
    }


    public static void voteImage(final Context context, String imageId, int score){
        api.sendVotedImage(new Callback<VoteResponse>() {
            @Override
            public void onResponse(Call<VoteResponse> call, Response<VoteResponse> response) {
                ContentHelper.updateOrInsertVote(context.getContentResolver(), response.body());
                Log.d(DEBUG_TAG, "vote image: " + response.body());
            }

            @Override
            public void onFailure(Call<VoteResponse> call, Throwable t) {
                logError(t);
            }
        }, imageId, score);
    }

    public static void loadImage(final Context context, String category, final ImageView mainImage) {
        api.getImage(category, new Callback<ResponseImages>() {
            @Override
            public void onResponse(Call<ResponseImages> call, Response<ResponseImages> response) {
                String url = "";
                if(!(response.body().getImageList().isEmpty())){
                    url = response.body().getImage(0).getUrl();
                }
                ImageHelper.setImage(context, mainImage, url, false);
            }

            @Override
            public void onFailure(Call<ResponseImages> call, Throwable t) {
                logError(t);
            }
        });
    }
}
