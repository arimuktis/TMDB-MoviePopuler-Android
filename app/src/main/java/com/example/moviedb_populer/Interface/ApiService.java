package com.example.moviedb_populer.Interface;

import com.example.moviedb_populer.BuildConfig;
import com.example.moviedb_populer.Model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("search/movie?api_key=" + BuildConfig.API_KEY)
    Call<MovieResponse> getItemSearch(@Query("query") String movie_name);

    @GET("movie/{category}")
    Call<MovieResponse> getPopular(
            @Path("category") String category,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page);

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcoming(@Query("api_key") String apiKey);

//    @GET("movie/top_rated")
//    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);
//    @GET("movie/{id}")
//    Call<MovieResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

}