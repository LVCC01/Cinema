package com.example.android.cinema;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.cinema.utilities.NetworkUtils;
import com.example.android.cinema.utilities.OpenReviewJsonUtils;
import com.example.android.cinema.utilities.OpenTrailerJsonUtils;

import java.net.URL;
import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieProfileData>> {
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private RecyclerView mReviewList;
    private ReviewAdapter mReviewAdapter;
    String recoveredIdNumber;
    private static final String TAG = ReviewActivity.class.getSimpleName();
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    // Create a constant int to uniquely identify your loader.
    private static final int REVIEW_SEARCH_LOADER = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        MovieDataParcel fetchTrailerData = getIntent().getParcelableExtra("trailerDataLoaded");
        if (fetchTrailerData != null) {
            recoveredIdNumber = fetchTrailerData.movieIdNumber;
        }
        mReviewList = (RecyclerView) findViewById(R.id.rv_reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mReviewList.setLayoutManager(layoutManager);
        mReviewList.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter(OpenReviewJsonUtils.reviews.size());
        mReviewList.setAdapter(mReviewAdapter);

        getSupportLoaderManager().initLoader(REVIEW_SEARCH_LOADER,null,this);
        loadReviewData();
    }
    public void showJsonDataView(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mReviewList.setVisibility(View.VISIBLE);
    }
    public void showErrorMessage(){
        mReviewList.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void loadReviewData() {
        String link = "http://api.themoviedb.org/3/movie";
        String action = "reviews";
        URL reviewUrl = NetworkUtils.buildTrailerDbUrl(link, recoveredIdNumber, action);
        Log.v(TAG, "Trailer URL: " + reviewUrl.toString());

        // Remove the call to execute the AsyncTask
//        new ReviewActivity.FetchReviewData().execute(reviewUrl);

        // Create a bundle called queryBundle. Use putString with SEARCH_QUERY_URL_EXTRA as the key and the String value of the URL as the value
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA,reviewUrl.toString());

        // Call getSupportLoaderManager and store it in a LoaderManager variable
        LoaderManager loaderManager = getSupportLoaderManager();
        // Get our Loader by calling getLoader and passing the ID we specified
        Loader<ArrayList<MovieProfileData>> reviewSearchLoader = loaderManager.getLoader(REVIEW_SEARCH_LOADER);
        // If the Loader was null, initialize it. Else, restart it.
        if (reviewSearchLoader == null){
            loaderManager.initLoader(REVIEW_SEARCH_LOADER,queryBundle,this);
        } else {
            loaderManager.restartLoader(REVIEW_SEARCH_LOADER,queryBundle,this);
        }
    }

    @Override
    public Loader<ArrayList<MovieProfileData>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<MovieProfileData>>(this) {
            // Create a ArrayList<MovieProfileData> member variable called mReviewJson that will store the raw JSON
            ArrayList<MovieProfileData> mReviewJson;
            @Override
            protected void onStartLoading() {
                if (args == null){
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);

                // If mReviewJson is not null, deliver that result. Otherwise, force a load
                if (mReviewJson != null){
                    deliverResult(mReviewJson);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<MovieProfileData> loadInBackground() {
                String searchQueryUrlString = args.getString(SEARCH_QUERY_URL_EXTRA);
                if (searchQueryUrlString == null || TextUtils.isEmpty(searchQueryUrlString)){
                    return null;
                }
                ArrayList<MovieProfileData> filteredJsonDataReview;
                try {
                    URL url = new URL(searchQueryUrlString);
                    String jsonDataFromHttpReview = NetworkUtils.getResponseFromHttp(url);
                    Log.v(TAG, "jsonDataFromHttpReview: " + jsonDataFromHttpReview);
                    filteredJsonDataReview = OpenReviewJsonUtils.getSimpleReviewStringsFromJson(ReviewActivity.this, jsonDataFromHttpReview);
                    Log.v(TAG, "filteredJsonDataReview: " + filteredJsonDataReview);
                    // here there is possibility of error - not sure how array will be handled
                    return filteredJsonDataReview;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(ArrayList<MovieProfileData> data) {
                mReviewJson = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieProfileData>> loader, ArrayList<MovieProfileData> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null && !data.equals("")) {
            Log.v(TAG,"reviewStrings: "+data);
            mReviewAdapter.swapArray();
            showJsonDataView();
            mReviewAdapter.setWeatherData(data);
        }else {
            showErrorMessage();
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieProfileData>> loader) {

    }

    // commenting out Async Task
/*    public class FetchReviewData extends AsyncTask<URL, Void, ArrayList<MovieProfileData>>{

        @Override
        protected ArrayList<MovieProfileData> doInBackground(URL... urls) {
            URL url = urls[0];
            Log.v(TAG, "url: " + url);
            String jsonDataFromHttpReview;

            ArrayList<MovieProfileData> filteredJsonDataReview;
            try {
                jsonDataFromHttpReview = NetworkUtils.getResponseFromHttp(url);
                Log.v(TAG, "jsonDataFromHttpReview: " + jsonDataFromHttpReview);
                filteredJsonDataReview = OpenReviewJsonUtils.getSimpleReviewStringsFromJson(ReviewActivity.this, jsonDataFromHttpReview);
                Log.v(TAG, "filteredJsonDataReview: " + filteredJsonDataReview);

                // here there is possibility of error - not sure how array will be handled
                return filteredJsonDataReview;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieProfileData> reviewStrings) {

            if (reviewStrings != null && !reviewStrings.equals("")) {
                Log.v(TAG,"reviewStrings: "+reviewStrings);
                mReviewAdapter.swapArray();
                showJsonDataView();
                mReviewAdapter.setWeatherData(reviewStrings);
                super.onPostExecute(reviewStrings);
            }else {
                showErrorMessage();
            }
        }
    }*/
}
