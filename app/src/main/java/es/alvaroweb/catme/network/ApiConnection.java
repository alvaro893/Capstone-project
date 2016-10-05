/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.network;

import android.provider.Settings;

import es.alvaroweb.catme.BuildConfig;
import es.alvaroweb.catme.model.Categories;
import es.alvaroweb.catme.model.GeneralResponse;
import es.alvaroweb.catme.model.ResponseImages;
import es.alvaroweb.catme.model.VoteResponse;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Singleton which holds the access to the Api Routes
 */
public class ApiConnection {
    private static final String URL_BASE = "http://thecatapi.com/";
    private static final String API_KEY = BuildConfig.CAT_API_KEY;
    private static final String USER_ID = Settings.Secure.ANDROID_ID;
    private static final ApiConnection INSTANCE = new ApiConnection();
    private ApiRoutes mApiRoutes;

    private ApiConnection() {
        setRetrofit();
    }

    private void setRetrofit() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        mApiRoutes = mRetrofit.create(ApiRoutes.class);
    }

    public static ApiConnection getInstance(){
        return INSTANCE;
    }

    public void fetchCategories(Callback<Categories> callback) {
        mApiRoutes.getCategories().enqueue(callback);
    }

    public void fetchImagesVotedData(Callback<ResponseImages> callback) {
        mApiRoutes.getVotedImages(API_KEY, USER_ID).enqueue(callback);
    }

    public void fetchImagesFavoritedData(Callback<ResponseImages> callback) {
        mApiRoutes.getFavoriteImages(API_KEY, USER_ID).enqueue(callback);
    }

    public void sendVotedImage(Callback<VoteResponse> callback, String imageId, int score) {
        mApiRoutes.sendVoteImage(API_KEY, imageId, USER_ID, score).enqueue(callback);
    }
}
