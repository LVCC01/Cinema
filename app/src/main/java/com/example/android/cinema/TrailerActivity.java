package com.example.android.cinema;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cinema.utilities.NetworkUtils;
import com.example.android.cinema.utilities.OpenTrailerJsonUtils;

import java.net.URL;
import java.util.ArrayList;

import static com.example.android.cinema.utilities.OpenMovieJsonUtils.movies;
import static com.example.android.cinema.utilities.OpenTrailerJsonUtils.trailers;

/**
 * Created by hjadhav on 8/7/2017.
 */
// implement LoaderManager.LoaderCallbacks<String> on TrailerActivity
public class TrailerActivity extends AppCompatActivity implements TrailerAdapter.ListItemTrailerClickListener, LoaderManager.LoaderCallbacks<ArrayList<MovieProfileData>> {
    /* A constant to save and restore the URL that is being displayed */
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private RecyclerView mTrailerList;
    private TrailerAdapter mTrailerAdapter;
    String recoveredIdNumber;
    private static final String TAG = TrailerActivity.class.getSimpleName();
    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    // Create a constant int to uniquely identify your loader.
    private static final int TRAILER_SEARCH_LOADER = 22;

    Button listItemTrailerView;
    public MovieProfileData trailerProfileData ;
    Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        MovieDataParcel fetchTrailerData = getIntent().getParcelableExtra("trailerDataLoaded");
        if (fetchTrailerData != null) {
            recoveredIdNumber = fetchTrailerData.movieIdNumber;
        }

        mTrailerList = (RecyclerView) findViewById(R.id.rv_trailers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTrailerList.setLayoutManager(layoutManager);
        mTrailerList.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(trailers.size(), this);
//        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerList.setAdapter(mTrailerAdapter);

        getSupportLoaderManager().initLoader(TRAILER_SEARCH_LOADER,null,this);
        loadTrailerData();

    }

    public void showJsonDataView(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mTrailerList.setVisibility(View.VISIBLE);
    }
    public void showErrorMessage(){
        mTrailerList.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void loadTrailerData() {
        String link = "http://api.themoviedb.org/3/movie";
        String action = "videos";
        URL trailerUrl = NetworkUtils.buildTrailerDbUrl(link, recoveredIdNumber, action);
        Log.v(TAG, "Trailer URL: " + trailerUrl.toString());

        // Remove the call to execute the AsyncTask
//        new TrailerActivity.FetchTrailerData().execute(trailerUrl);

        // Create a bundle called queryBundle. Use putString with SEARCH_QUERY_URL_EXTRA as the key and the String value of the URL as the value
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA,trailerUrl.toString());

        // Call getSupportLoaderManager and store it in a LoaderManager variable
        LoaderManager loaderManager = getSupportLoaderManager();
        // Get our Loader by calling getLoader and passing the ID we specified
        Loader<ArrayList<MovieProfileData>> trailerSearchLoader = loaderManager.getLoader(TRAILER_SEARCH_LOADER);
        // If the Loader was null, initialize it. Else, restart it.
        if (trailerSearchLoader == null){
            loaderManager.initLoader(TRAILER_SEARCH_LOADER,queryBundle,this);
        }else {
            loaderManager.restartLoader(TRAILER_SEARCH_LOADER,queryBundle,this);
        }
    }


    // Override onCreateLoader
    @Override
    public Loader<ArrayList<MovieProfileData>> onCreateLoader(int id, final Bundle args) {
        // Return a new AsyncTaskLoader<String> as an anonymous inner class with this as the constructor's parameter
        return new AsyncTaskLoader<ArrayList<MovieProfileData>>(this) {
            // Create a ArrayList<MovieProfileData> member variable called mTrailerJson that will store the raw JSON
            ArrayList<MovieProfileData> mTrailerJson;

            // Override onStartLoading
            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);

                // If mTrailerJson is not null, deliver that result. Otherwise, force a load
                if (mTrailerJson != null){
                    deliverResult(mTrailerJson);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<MovieProfileData> loadInBackground() {
                String searchQueryUrlString = args.getString(SEARCH_QUERY_URL_EXTRA);
                if (searchQueryUrlString == null || TextUtils.isEmpty(searchQueryUrlString)) {
                    return null;
                }
                ArrayList<MovieProfileData> filteredJsonDataTrailer;
                try {
                    URL url = new URL(searchQueryUrlString);
                    String jsonDataFromHttpTrailer = NetworkUtils.getResponseFromHttp(url);
                    Log.v(TAG, "jsonDataFromHttpTrailer: " + jsonDataFromHttpTrailer);
                    filteredJsonDataTrailer = OpenTrailerJsonUtils.getSimpleTrailerStringsFromJson(TrailerActivity.this, jsonDataFromHttpTrailer);
                    Log.v(TAG, "filteredJsonDataTrailer: " + filteredJsonDataTrailer);

                    // here there is possibility of error - not sure how array will be handled
                    return filteredJsonDataTrailer;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(ArrayList<MovieProfileData> data) {
                mTrailerJson = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieProfileData>> loader, ArrayList<MovieProfileData> data) {
        // Hide the loading indicator
        /* When we finish loading, we want to hide the loading indicator from the user. */
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (data != null && !data.equals("")) {
            showJsonDataView();
            mTrailerAdapter.setWeatherData(data);
        }else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieProfileData>> loader) {

    }

    @Override
    public void onListItemTrailerClick(int clickedItemTrailerIndex) {
        Log.v(TAG,"code reached before the condition");
        String toastMessage = "Item #" + clickedItemTrailerIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        mToast.show();

        Log.v(TAG,"Item #" + clickedItemTrailerIndex + " clicked.");

        MovieProfileData trailerProfileData = trailers.get(clickedItemTrailerIndex);
        String trailerKey = trailerProfileData.getKeyText();
        Log.v(TAG,"KEY: " + trailerKey);
        Intent appIntent = new Intent (Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerKey));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailerKey));
        try {
            startActivity(appIntent);
        } catch (Exception e) {
            startActivity(webIntent);;
        }
    }

    // commenting out Async Task

/*    public class FetchTrailerData extends AsyncTask<URL, Void, ArrayList<MovieProfileData>> {

        @Override
        protected ArrayList<MovieProfileData> doInBackground(URL... urls) {
            URL url = urls[0];
            Log.v(TAG, "url: " + url);
            String jsonDataFromHttpTrailer;

            ArrayList<MovieProfileData> filteredJsonDataTrailer;
            try {
                jsonDataFromHttpTrailer = NetworkUtils.getResponseFromHttp(url);
                Log.v(TAG, "jsonDataFromHttpTrailer: " + jsonDataFromHttpTrailer);
                filteredJsonDataTrailer = OpenTrailerJsonUtils.getSimpleTrailerStringsFromJson(TrailerActivity.this, jsonDataFromHttpTrailer);
                Log.v(TAG, "filteredJsonDataTrailer: " + filteredJsonDataTrailer);

                // here there is possibility of error - not sure how array will be handled
                return filteredJsonDataTrailer;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieProfileData> trailerStrings) {

           *//* mTrailerAdapter.setTrailerData(trailerStrings);
            super.onPostExecute(trailerStrings);*//*

            if (trailerStrings != null && !trailerStrings.equals("")) {
                showJsonDataView();
                mTrailerAdapter.setWeatherData(trailerStrings);
                super.onPostExecute(trailerStrings);
            }else {
                showErrorMessage();
            }
        }
    }*/
}
