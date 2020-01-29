package com.example.moviedb_populer.Interface;

import com.example.moviedb_populer.Resource.OurDataSet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface OurRetrofit {
    @GET("1a6v5q")

        Call<List<OurDataSet>> getDataSet();

}
