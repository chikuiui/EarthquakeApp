package com.example.earthquake;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.earthquake.databinding.ListItemBinding;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(@NonNull Context context, ArrayList<Earthquake> earthquakes) {
        super(context, 0,earthquakes);
    }

    /*  The getView() method in an ArrayAdapter is used to customize how each item in the adapter's
        data set is displayed in the ListView, GridView, or Spinner
    */

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ListItemBinding binding = ListItemBinding.inflate(LayoutInflater.from(getContext()),parent,false);

        Earthquake currentEarthquake = getItem(position);

        // format magnitude and set the magnitude.
        String formattedMagnitude = formatMagnitude(currentEarthquake.getMagnitude());
        binding.earthquakeMagnitude.setText(formattedMagnitude);

        // below code is to set the background color for the circle in list_item view.
        GradientDrawable magnitudeCircle = (GradientDrawable) binding.earthquakeMagnitude.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);
        
        String originalLocation = currentEarthquake.getLocation();
        String locationOffset,primaryLocation;
        /* divide original location into two parts so that it can be easily readable and we can put into different textViews using
           location separator in originalLocation string.
         */
        if(originalLocation.contains(LOCATION_SEPARATOR)){
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        }else{
            locationOffset = "Near the";
            primaryLocation = originalLocation;
        }
        binding.earthquakeLocation.setText(locationOffset);
        binding.earthquakeLocation2.setText(primaryLocation);
        
        
        // Date object used to format both date and time
        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());
        String formattedDate = formatDate(dateObject);
        String formattedTime = formatTime(dateObject);
        binding.earthquakeDate.setText(formattedDate);
        binding.earthquakeTime.setText(formattedTime);

        // return root view from binding
        return binding.getRoot();
        
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magFormat = new DecimalFormat("0.0");
        return magFormat.format(magnitude);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int)Math.floor(magnitude);
        switch (magnitudeFloor){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        // getColor() to convert the clolor resource ID into an actual Integer color value.
        return ContextCompat.getColor(getContext(),magnitudeColorResourceId);
    }

}
