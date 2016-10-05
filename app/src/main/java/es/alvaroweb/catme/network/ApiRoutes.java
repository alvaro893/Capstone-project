package es.alvaroweb.catme.network;

import es.alvaroweb.catme.model.GeneralResponse;
import es.alvaroweb.catme.model.ResponseImages;
import es.alvaroweb.catme.model.Categories;
import es.alvaroweb.catme.model.VoteResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */

public interface ApiRoutes {
    @GET("/api/images/get")
    Call<ResponseImages> getImages(@Query("api_key") String apiKey, @Query("format") String format,
                                   @Query("category") String category, @Query("sub_id") String subId,
                                   @Query("size") String size,@Query("results_per_page") int results);

    @GET("api/images/getvotes")
    Call<ResponseImages> getVotedImages(@Query("api_key") String apiKey,
                                        @Query("sub_id") String subId);

    @GET("api/images/getfavourite")
    Call<ResponseImages> getFavoriteImages(@Query("api_key") String apiKey, @Query("sub_id") String subId);

    @GET("api/images/vote")
    Call<VoteResponse> sendVoteImage(@Query("api_key") String apiKey, @Query("image_id") String imageId,
                                     @Query("sub_id") String subId, @Query("score") int score);
    @GET("/api/images/favourite")
    Call<GeneralResponse> setFavoriteImage(@Query("api_key") String apiKey, @Query("sub_id") String subId,
                                           @Query("action") String action, @Query("image_id") String imageId);

    @GET("/api/images/report")
    Call<GeneralResponse> sendReport();

    @GET("/api/categories/list")
    Call<Categories> getCategories();
}
