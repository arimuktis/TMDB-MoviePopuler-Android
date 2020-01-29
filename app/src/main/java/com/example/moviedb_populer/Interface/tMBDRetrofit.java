package com.example.moviedb_populer.Interface;

import com.example.moviedb_populer.Resource.MovieResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface tMBDRetrofit {


    @GET("3/movie/{category}")

    Call<MovieResults> getMovies(

            @Path("Category") String category,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );
}
