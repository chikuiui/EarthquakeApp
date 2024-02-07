package com.example.earthquake;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper Methos related to requesting and receiving earthquake data from U-S-G-S website.
 */

/*
  JSON Element contains common code for all the valid types in JSON:

      * JsonObject
      * JsonArray
      * JsonPrimitive(string,integer,boolean)
      * JsonNull
 */


public class QueryUtils {
    // empty constructor.
    public QueryUtils(){}
    // for log message
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    public static List<Earthquake> fetchEarthquake(String mUrl) {
        URL url = createUrl(mUrl);
        String jsonResponse = null;

        try {
            // get data from url into jsonResponse string in JSON format
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Error closing input stream",e);
        }
        // extract data from [ jsonResponse string(JSON FORMAT) ] and store in List of Earthquakes
        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);
        return earthquakes;
    }

    private static List<Earthquake> extractFeatureFromJson(String jsonResponse) {
        if(TextUtils.isEmpty(jsonResponse))return null;

        List<Earthquake> earthquakes = new ArrayList<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray featureArray = baseJsonResponse.getJSONArray("features");

            if(featureArray.length() > 0){
                for(int i=0;i<featureArray.length();i++){
                    JSONObject firstFeature = featureArray.getJSONObject(i);
                    JSONObject properties = firstFeature.getJSONObject("properties");

                    String location = properties.getString("place");
                    double magnitude = properties.getDouble("mag");
                    long time = properties.getLong("time");
                    String url = properties.getString("url");
                    Earthquake ee = new Earthquake(location,magnitude,time,url);
                    earthquakes.add(ee);
                }
            }
        }catch (JSONException e){
            Log.e(LOG_TAG,"Problem parsing the earthquake JSON results",e);
        }
        return earthquakes;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if(url == null)return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG,"Error response code : " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"Problem retrieving the earthquake JSON results. ", e);
        }finally{
            if(urlConnection != null) urlConnection.disconnect();
            if(inputStream != null)inputStream.close();
        }
        return jsonResponse;
    }

    // using static keyword so that we don't nee to create an instance of the class containing this function(QueryUtils).
    private static String readFromStream(InputStream inputStream) throws IOException{
         StringBuilder output = new StringBuilder();
         if(inputStream != null){
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
             BufferedReader reader = new BufferedReader(inputStreamReader);
             String line = reader.readLine();
             while(line != null){
                 output.append(line);
                 line = reader.readLine();
             }
         }
         return output.toString();
    }

    private static URL createUrl(String mUrl) {
        URL url = null;
        try{
            url = new URL(mUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error with creating URL ",e);
        }
        return url;
    }
}
