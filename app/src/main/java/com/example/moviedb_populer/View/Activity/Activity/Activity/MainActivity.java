package com.example.moviedb_populer.View.Activity.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviedb_populer.Adapter.MoviesAdapter;
import com.example.moviedb_populer.BuildConfig;
import com.example.moviedb_populer.Interface.ApiBuilder;
import com.example.moviedb_populer.Interface.ApiService;
import com.example.moviedb_populer.Model.MovieModel;
import com.example.moviedb_populer.Model.MovieResponse;
import com.example.moviedb_populer.R;
import com.example.moviedb_populer.R2;
import com.example.moviedb_populer.Service.TimerService;
import com.example.moviedb_populer.View.Activity.Activity.Fragment.HomeFragment;
import com.example.moviedb_populer.View.Activity.Activity.Fragment.NetworkNotAvailable;
import com.example.moviedb_populer.View.Activity.Activity.Fragment.PlayingFragment;
import com.example.moviedb_populer.View.Activity.Activity.Fragment.PopularFragment;
import com.example.moviedb_populer.values.Values;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {

    private final int SECONDS = 1000;
    private int counter=0;
    public static int currentIndex;


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isNetworkAvailable()){
            loadFragment(new HomeFragment());
        }else{
            loadFragment(new NetworkNotAvailable());
        }
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.BottomNavigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.bbn_item1:
                    onResume();
                    if(isNetworkAvailable()){
                        fragment = new HomeFragment();
                    }else {
                        fragment= new NetworkNotAvailable();
                    }
                    currentIndex=0;
                    FragmentTransaction transaction = getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.pull_in_left,R.anim.push_out_right);
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    return true;
                case R.id.bbn_item2:
                    if(isNetworkAvailable()){
                        fragment = new PopularFragment();
                    }else {
                        fragment= new NetworkNotAvailable();
                    }
                    try{
                        if(currentIndex < 1 ){
                            FragmentTransaction transactionb = getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.pull_in_right,R.anim.push_out_left);
                            transactionb.replace(R.id.fragment_container, fragment);
                            transactionb.addToBackStack(null);
                            transactionb.commit();
                            currentIndex=1;

                        }else if (currentIndex > 1){
                            FragmentTransaction transactionb = getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.pull_in_left,R.anim.push_out_right);
                            transactionb.replace(R.id.fragment_container, fragment);
                            transactionb.addToBackStack(null);
                            transactionb.commit();
                            currentIndex=1;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    return true;
                case R.id.bbn_item3:
                    if(isNetworkAvailable()){
                        fragment = new PlayingFragment();
                    }else {
                        fragment= new NetworkNotAvailable();
                    }
                    currentIndex=2;
                    FragmentTransaction transactionc = getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.pull_in_right,R.anim.push_out_left);
                    transactionc.replace(R.id.fragment_container, fragment);
                    transactionc.addToBackStack(null);
                    transactionc.commit();
                    return true;
            }
            return false;
        }
    };


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
                    if (recyclerView !=null){
                        recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.content_main, getApplicationContext()));
                    }


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

    public boolean isNetworkAvailable() {
       try{
           ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
           NetworkInfo networkInfo = null;
           if(manager != null){
               networkInfo = manager.getActiveNetworkInfo();
           }
           return networkInfo != null && networkInfo.isConnected();
       }catch (NullPointerException e) {
           return false;
       }
    }
}
