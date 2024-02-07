package com.example.earthquake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.earthquake.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private ActivityMainBinding binding;
    private EarthquakeAdapter adapter;
    // constant value for the earthquake loader id. we can choose any integer.
    private static final int EARTHQUAKE_LOADER_ID = 1;

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set an empty earthquake adapter to a list.
        adapter = new EarthquakeAdapter(this,new ArrayList<>());
        binding.list.setAdapter(adapter);

        binding.list.setOnItemClickListener((adapterView, view, i, l) -> {
            // when we click any item in this list it will give us the position of the item through -> i
            Earthquake currentEarthquake = adapter.getItem(i);
            // The below code is the url of current item.
            Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
            //the below intent will open the website so the we can see the full details of particular earthquake.
            Intent intent = new Intent(Intent.ACTION_VIEW,earthquakeUri);
            startActivity(intent);
        });

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);

    }


    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minMag = sharedPreferences.getString(getString(R.string.settings_min_magnitude_key),getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit","15");
        uriBuilder.appendQueryParameter("minmag",minMag);
        uriBuilder.appendQueryParameter("orderby",orderBy);

        return new EarthquakeLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
           // the below two line shows if the user internet is off and we are tryring to fetch the data from server.
           // it will crosscheck whether it is connected to network or not.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) binding.emptyView.setText(R.string.no_earthquakes);
        else binding.emptyView.setText(R.string.internet_error);

        // indicator in the center of the screen
        binding.loadingIndicator.setVisibility(View.GONE);

        // clear the adapter of prev earthquake data.
        adapter.clear();

        if(earthquakes != null && !earthquakes.isEmpty()){
            binding.emptyView.setVisibility(View.GONE);
            adapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {
       adapter.clear();
    }





}