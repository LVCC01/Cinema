package com.example.android.cinema.utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.cinema.MovieProfileData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hjadhav on 7/29/2017.
 */

public class OpenTrailerJsonUtils {
    public static ArrayList<MovieProfileData> trailers = new ArrayList<>();
    private static final String TAG = OpenTrailerJsonUtils.class.getSimpleName();
    static String id;
    static String key;
    static String name;

    public static ArrayList<MovieProfileData> getSimpleTrailerStringsFromJson(Context context,String trailerDetailsJsonStr) throws JSONException {
        JSONObject trailerList = new JSONObject(trailerDetailsJsonStr);
        JSONArray results = trailerList.getJSONArray("results");

        String[] trailerId = new String[results.length()];
        String[] trailerKey = new String[results.length()];
        String[] trailerName = new String[results.length()];

        trailers = new ArrayList<>();

        for (int i=0;i< results.length();i++){
            JSONObject trailerDetails = results.getJSONObject(i);
            id = trailerDetails.getString("id");
            key = trailerDetails.getString("key");
            name = trailerDetails.getString("name");

            Log.v(TAG,"trailer id:  " + id );
            Log.v(TAG,"trailer key:  " + key );
            Log.v(TAG,"trailer name:  " + name );

            MovieProfileData trailerInfo = new MovieProfileData(id,key,name);
            trailerInfo.setIdText(id);
            trailerInfo.setKeyText(key);
            trailerInfo.setNameText(name);
            trailers.add(trailerInfo);
        }
        Log.v(TAG,"trailers:  " + trailers );
        Log.v(TAG,"trailers numbers:  " + trailers.size() );
        return trailers;
    }
}
