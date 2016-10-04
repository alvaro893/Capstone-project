/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.network;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import es.alvaroweb.catme.BuildConfig;
import es.alvaroweb.catme.model.Categories;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/*
 * TODO: Create JavaDoc
 */
public class ApiConnection {
    private static final String URL_BASE = "http://thecatapi.com/";
    private static final String API_KEY = BuildConfig.CAT_API_KEY;
    private static final String USER_ID = Settings.Secure.ANDROID_ID;
    private Context mContext;
    private ApiRoutes mApiRoutes;

    public ApiConnection(Context mContext) {
        this.mContext = mContext;
        setRetrofit();
    }

    public ApiConnection() {
        setRetrofit();
    }

    private void setRetrofit() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        mApiRoutes = mRetrofit.create(ApiRoutes.class);
    }

    public void getCategories(Callback<Categories> callback) {
        mApiRoutes.getCategories()
                .enqueue(callback);

    }
}
