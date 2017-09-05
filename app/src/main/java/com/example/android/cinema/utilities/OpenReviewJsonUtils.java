package com.example.android.cinema.utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.cinema.MovieProfileData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hjadhav on 8/8/2017.
 */

public class OpenReviewJsonUtils {
    public static ArrayList<MovieProfileData> reviews = new ArrayList<>();
    private static final String TAG = OpenReviewJsonUtils.class.getSimpleName();
    static String idReview;
    static String authorReview;
    static String contentReview;

    public static ArrayList<MovieProfileData> getSimpleReviewStringsFromJson(Context context, String reviewDetailsJsonStr) throws JSONException {
        JSONObject trailerList = new JSONObject(reviewDetailsJsonStr);
        JSONArray results = trailerList.getJSONArray("results");

        String[] reviewId = new String[results.length()];
        String[] reviewAuthor = new String[results.length()];
        String[] reviewContent = new String[results.length()];
        reviews = new ArrayList<>();

        for (int i=0;i< results.length();i++){
            JSONObject reviewDetails = results.getJSONObject(i);
            idReview = reviewDetails.getString("id");
            authorReview = reviewDetails.getString("author");
            contentReview = reviewDetails.getString("content");

            Log.v(TAG,"review id:  " + idReview );
            Log.v(TAG,"review author:  " + authorReview );
            Log.v(TAG,"review content:  " + contentReview );

            MovieProfileData reviewInfo = new MovieProfileData(idReview,authorReview,contentReview);
            reviewInfo.setIdReviewText(idReview);
            reviewInfo.setAuthorReviewText(authorReview);
            reviewInfo.setContentReviewText(contentReview);

            reviews.add(reviewInfo);
        }
        Log.v(TAG,"reviews:  " + reviews );
        Log.v(TAG,"review numbers:  " + reviews.size() );
        return reviews;
    }
}
