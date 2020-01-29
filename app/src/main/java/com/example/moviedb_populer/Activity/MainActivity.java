package com.example.moviedb_populer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.moviedb_populer.Adapter.DataAdapter;
import com.example.moviedb_populer.Interface.OurRetrofit;
import com.example.moviedb_populer.R;
import com.example.moviedb_populer.Resource.OurDataSet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static String BASE_URL = "https://api.themoviedb.org/";
    public static String baseURL = "https://api.myjson.com/bins/";

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rc_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OurRetrofit ourRetrofit = retrofit.create(OurRetrofit.class);

        Call<List<OurDataSet>> listCall = ourRetrofit.getDataSet();
        listCall.enqueue(new Callback<List<OurDataSet>>() {
            @Override
            public void onResponse(Call<List<OurDataSet>> call, Response<List<OurDataSet>> response) {

                ShowIt(response.body());
            }

            @Override
            public void onFailure(Call<List<OurDataSet>> call, Throwable t) {

            }
        });



    }

    private void ShowIt(List<OurDataSet> response) {

        DataAdapter dataAdapter = new DataAdapter(response,getApplicationContext());

        recyclerView.setAdapter(dataAdapter);
    }
}
