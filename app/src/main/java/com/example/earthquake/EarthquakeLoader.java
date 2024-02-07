package com.example.earthquake;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String mUrl;

    public EarthquakeLoader(Context context , String url){
        super(context);
        mUrl = url;
    }


    // it ensure that the loader fetches fresh data or initiate the loading process as soon as possible.
    @Override
    protected void onStartLoading(){
        forceLoad();
    }


    // it load data in background thread to avoid application stuck.
    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        if(mUrl == null)return null;
        List<Earthquake> earthquakes = QueryUtils.fetchEarthquake(mUrl);
        return earthquakes;
    }
}
