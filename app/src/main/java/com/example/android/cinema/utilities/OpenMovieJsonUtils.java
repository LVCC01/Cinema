package com.example.android.cinema.utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.cinema.MovieProfileData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hjadhav on 4/19/2017.
 */
// This method will be used to filter the required data from JSON data
public class OpenMovieJsonUtils {

    public static ArrayList<MovieProfileData> movies = new ArrayList<>();
    public static ArrayList<MovieProfileData> moviesTitle = new ArrayList<>();
    private static final String TAG = OpenMovieJsonUtils.class.getSimpleName();
    static String poster;
    static String title;
    static String plot;
    static String rating;
    static String releaseDate;
    static String id;

    public static ArrayList<MovieProfileData> getSimpleMovieStringsFromJson(Context context, String detailsJsonStr) throws JSONException {

        JSONObject movieList = new JSONObject(detailsJsonStr);
        JSONArray results = movieList.getJSONArray("results");

        /*String[] movieTitle = new String[results.length()];
        String[] moviePlot = new String[results.length()];
        String[] movieRating = new String[results.length()];
        String[] movieReleaseDate = new String[results.length()];
        String[] movieId = new String[results.length()];*/

        for (int i=0;i<results.length();i++){
            JSONObject movieDetails = results.getJSONObject(i);
            poster = movieDetails.getString("poster_path");
            title = movieDetails.getString("original_title");
            plot = movieDetails.getString("overview");
            rating = movieDetails.getString("vote_average");
            releaseDate = movieDetails.getString("release_date");
            id = movieDetails.getString("id");

            Log.v(TAG,"poster:  " + poster );
            Log.v(TAG,"title:  " + title );
            Log.v(TAG,"plot:  " + plot );
            Log.v(TAG,"rating:  " + rating );
            Log.v(TAG,"releaseDate:  " + releaseDate );
            Log.v(TAG,"movie id: " + id);

            MovieProfileData moviePoster = new MovieProfileData(poster,title,plot,rating,releaseDate,id);

            moviePoster.setPosterUrl(poster);
            moviePoster.setTitleText(title);
            moviePoster.setPlotText(plot);
            moviePoster.setRatingText(rating);
            moviePoster.setReleaseDateText(releaseDate);
            moviePoster.setIdNumber(id);
            movies.add(moviePoster);

            /*movieTitle[i] = title;
            moviePlot[i] = plot;
            movieRating[i] = rating;
            movieReleaseDate[i] = releaseDate;
            movieId[i] = id;*/
        }

        Log.v(TAG,"movies:  " + movies );
        return movies;
    }
}
