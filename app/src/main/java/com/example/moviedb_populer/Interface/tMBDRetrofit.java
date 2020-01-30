package com.example.moviedb_populer.Interface;

import com.example.moviedb_populer.Model.MovieResults;
import com.example.moviedb_populer.Model.OurDataSet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface tMBDRetrofit {

//    3/discover/movie?api_key=f7b67d9afdb3c971d4419fa4cb667fbf
    @GET("3/movie/{category}")

    Call<MovieResults> listOfMovies(

            @Path("category") String category,
            @Query("api_key") String apikey,
            @Query("language") String language,
            @Query("page") int page
    );
}
