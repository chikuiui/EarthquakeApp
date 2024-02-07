package com.example.earthquake;

public class Earthquake {
    private String location;
    private String mUrl;
    private double magnitude;
    private long TimeInMilliseconds;

    Earthquake(String location,double magnitude,long TimeInMilliseconds,String url){
        this.location= location;
        this.magnitude=magnitude;
        this.TimeInMilliseconds = TimeInMilliseconds;
        this.mUrl = url;
    }

    public String getLocation() {return location;}
    public String getUrl() {return mUrl;}
    public double getMagnitude() {return magnitude;}
    public long getTimeInMilliseconds() {return TimeInMilliseconds;}
}
