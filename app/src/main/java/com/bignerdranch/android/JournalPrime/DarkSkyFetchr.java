package com.bignerdranch.android.JournalPrime;

import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ad939564 on 4/9/2018.
 */

//DarkSkyFetchr handles the networking aspect of the API
public class DarkSkyFetchr {

    private static final String TAG = "DarkSkyFetchr";
    private static final String API_KEY = "0608051a6da549bf10d568618a505a37";
    private static final String LAT = "37.8267";
    private static final String LON = "-122.4233";

    public byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }finally{
            connection.disconnect();
        }
    }
    public String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }

    //fetchItems method
    public List<DarkSkyItem> fetchItems(){

        List<DarkSkyItem> items = new ArrayList<>();

        try{
            String url = "https://api.darksky.net/forecast/0608051a6da549bf10d568618a505a37/37.8267,-122.4233";
//            String url = Uri.parse("https://api.darksky.net/forecast/")
//                    .buildUpon()
//                    .appendQueryParameter("api_key", API_KEY)
//                    .appendQueryParameter("lat", LAT)
//                    .appendQueryParameter("lon", LON)
//                    .build().toString();
            String jsonString = getUrlString(url);
//            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        }catch (IOException ioe){
            Log.e(TAG, "Failed to fetch items", ioe);
        }catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return items;
    }

    //parsing JSON
    private void parseItems(List<DarkSkyItem> items, JSONObject jsonBody)
            throws JSONException{

        JSONObject currentlyJsonObject = jsonBody.getJSONObject("currently");

        //for testing, ensure object exists
//        Boolean test = currentlyJsonObject.has("summary");
//        Log.i(TAG, "DEBUG: API result has Summary object: " + test.toString());
//        Boolean testTwo = currentlyJsonObject.has("temperature");
//        Log.i(TAG, "DEBUG: API result has temperature object: " + testTwo.toString());


//        JSONArray currentlyJsonArray = forecastJsonObject.getJSONArray("currently");

//        for (int i = 0; i < weatherJsonArray.length(); i++){
//            JSONObject weatherJsonObject = weatherJsonArray.getJSONObject(i);

//        JSONObject forecastJsonObject = currentlyJsonArray.getJSONObject(0);


        DarkSkyItem item = new DarkSkyItem();

        item.setmSkyDescription(currentlyJsonObject.getString("summary"));
        item.setmSkyIcon(currentlyJsonObject.getString("icon"));
//            Log.e(TAG, "Set icon text within DarkSky Item = " + item.getmSkyIcon());
        item.setmTemp(currentlyJsonObject.getString("temperature"));

        items.add(item);
    }
}

//private static final String API ="https://api.darksky.net/forecast/0608051a6da549bf10d568618a505a37";
//google api key: AIzaSyCGyCVQeUK-DmKPpCFaOEMTmD4dsK3OfkM


