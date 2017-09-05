package com.example.android.cinema.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by hjadhav on 4/19/2017.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    final static String QUERY_PARAM = "api_key";
    final static String API_KEY = "Put Your Key Here";

    final static String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    // Then you will need a ‘size’, which will be one of the following: "w92", "w154", "w185", "w342", "w500", "w780", or "original". For most phones we recommend using “w185”.
    final static String PIC_SIZE = "w500";

    // build URL to get access to MOVIE Database
    // depending on the menu button selected POPULAR or TOP-RATED, respective databases will be accessed
    public static URL buildDbUrl (String menuButtonSelection){
        Uri builtDbUri = Uri.parse(menuButtonSelection).buildUpon()
                .appendQueryParameter(QUERY_PARAM,API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtDbUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    // build URL for movie trailer and movie reviews
    public static URL buildTrailerDbUrl (String menuButtonSelection,String movieId,String action){
        Uri builtDbUri = Uri.parse(menuButtonSelection).buildUpon()
                .appendPath(movieId)
                .appendPath(action)
                .appendQueryParameter(QUERY_PARAM,API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtDbUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    // build URL to get access to MOVIE poster
    public static URL buildPosterUrl (String posterContents){
        Log.v("NetworkUtils","posterContents: "+posterContents);
        Uri posterBuiltUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendPath(PIC_SIZE)
                .appendEncodedPath(posterContents)
                .build();
        Log.v("NetworkUtils","posterBuiltUri: "+posterBuiltUri);
        URL posterUrl;
        try {
            posterUrl = new URL(posterBuiltUri.toString());

            return posterUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // getResponseFromHttpUrl method will get JSON formatted data from the database
    public static String getResponseFromHttp(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
