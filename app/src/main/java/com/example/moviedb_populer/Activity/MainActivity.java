package com.example.moviedb_populer.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviedb_populer.Adapter.MoviesAdapter;
import com.example.moviedb_populer.BuildConfig;
import com.example.moviedb_populer.Interface.ApiBuilder;
import com.example.moviedb_populer.Interface.ApiService;
import com.example.moviedb_populer.Interface.OurRetrofit;
import com.example.moviedb_populer.Interface.tMBDRetrofit;
import com.example.moviedb_populer.Model.MovieModel;
import com.example.moviedb_populer.Model.MovieResponse;
import com.example.moviedb_populer.Model.MovieResults;
import com.example.moviedb_populer.R;
import com.example.moviedb_populer.Model.OurDataSet;
import com.example.moviedb_populer.Service.TimerService;
import com.example.moviedb_populer.values.Values;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {

    private final int SECONDS = 1000;
    private int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadMovie();


        final Handler handler = new Handler();
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        startService(new Intent(MainActivity.this, TimerService.class));
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timertask, 0, 10*SECONDS);

    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        ApiService apiService = ApiBuilder.getClient(MainActivity.this).create(ApiService.class);
        @Override
        public void onReceive(Context context, Intent intent) {
            String someValue = intent.getStringExtra("someName");
            counter+=1;
            Call<MovieResponse> call = apiService.getPopular(Values.CATEGORY[0],BuildConfig.API_KEY,Values.LANGUAGE,counter);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse>call, Response<MovieResponse> response) {
                    final List<MovieModel> movies = response.body().getResults();
                    final RecyclerView recyclerView = findViewById(R.id.rc_view);
                    recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.content_main, getApplicationContext()));

                    if (movies != null ){

                        MovieModel firstMovie = movies.get(0);
                        if(firstMovie != null) {
                            Log.i("TAG","#Log Berhasil "+firstMovie.getTitle());

                            Toast toast = Toast.makeText(getApplicationContext(), "Data telah berhasil diperbaharui", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse>call, Throwable t) {
                    // Log error here since request failed

                    Log.i("TAG","#Log GAGAL "+t);

                }
            });
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.moviedb_populer.Service");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.moviedb_populer.Service");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    private void loadMovie() {
        ApiService apiService = ApiBuilder.getClient(this).create(ApiService.class);
        final RecyclerView recyclerView = findViewById(R.id.rc_view);
        final RecyclerView recyclerView2 = findViewById(R.id.rc_view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        Call<MovieResponse> call = apiService.getPopular(Values.CATEGORY[0],BuildConfig.API_KEY,Values.LANGUAGE,Values.PAGE[0]);
        Call<MovieResponse> call2 = apiService.getPopular(Values.CATEGORY[1],BuildConfig.API_KEY,Values.LANGUAGE,Values.PAGE[0]);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse>call, Response<MovieResponse> response) {
                final List<MovieModel> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.content_main, getApplicationContext()));
                recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                        public boolean onSingleTapUp(MotionEvent e){
                            return true;
                        }
                    });

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && gestureDetector.onTouchEvent(e)){
                            int position = rv.getChildAdapterPosition(child);
                            Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra(DetailActivity.EXTRA_TITLE, movies.get(position).getTitle());
                            i.putExtra(DetailActivity.EXTRA_OVERVIEW, movies.get(position).getOverview());
                            i.putExtra(DetailActivity.EXTRA_TIME, movies.get(position).getReleaseDate());
                            i.putExtra(DetailActivity.EXTRA_POSTER, movies.get(position).getPosterPath());
                            i.putExtra(DetailActivity.EXTRA_LANGUAGE, movies.get(position).getOriginalLanguage());
                            try{
                                List<Integer> genr = movies.get(position).getGenreIds();
                                i.putExtra(DetailActivity.EXTRA_GENRES, genr.toString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            getApplicationContext().startActivity(i);
                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });
                if (movies != null ){

                    MovieModel firstMovie = movies.get(0);
                    if(firstMovie != null) {
                        Log.i("TAG","#Log Berhasil "+firstMovie.getTitle());
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse>call, Throwable t) {
                // Log error here since request failed

                Log.i("TAG","#Log GAGAL "+t);

            }
        });
        call2.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse>call, Response<MovieResponse> response) {
                final List<MovieModel> movies = response.body().getResults();
                recyclerView2.setAdapter(new MoviesAdapter(movies, R.layout.content_main, getApplicationContext()));
                recyclerView2.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                        public boolean onSingleTapUp(MotionEvent e){
                            return true;
                        }
                    });

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && gestureDetector.onTouchEvent(e)){
                            int position = rv.getChildAdapterPosition(child);
                            Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra(DetailActivity.EXTRA_TITLE, movies.get(position).getTitle());
                            i.putExtra(DetailActivity.EXTRA_OVERVIEW, movies.get(position).getOverview());
                            i.putExtra(DetailActivity.EXTRA_TIME, movies.get(position).getReleaseDate());
                            i.putExtra(DetailActivity.EXTRA_POSTER, movies.get(position).getPosterPath());
                            i.putExtra(DetailActivity.EXTRA_LANGUAGE, movies.get(position).getOriginalLanguage());
                            try{
                                List<Integer> genr = movies.get(position).getGenreIds();
                                i.putExtra(DetailActivity.EXTRA_GENRES, genr.toString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            getApplicationContext().startActivity(i);
                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });
                if (movies != null ){

                    MovieModel firstMovie = movies.get(0);
                    if(firstMovie != null) {
                        Log.i("TAG","#Log Berhasil "+firstMovie.getTitle());
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse>call, Throwable t) {
                // Log error here since request failed

                Log.i("TAG","#Log GAGAL "+t);

            }
        });
    }

}
