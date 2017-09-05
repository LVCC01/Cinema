package com.example.android.cinema;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.android.cinema.data.FavoriteMovieListContract;
import com.example.android.cinema.data.FavoriteMovieListDbHelper;
import com.example.android.cinema.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;

public class ChildActivity extends AppCompatActivity   {
    private static final String TAG = ChildActivity.class.getSimpleName();

    private ImageView mDisplayPoster;
    private TextView mDisplayTitle;
    private TextView mDisplayPlot;
    private TextView mDisplayRating;
    private TextView mDisplayReleaseDate;

    private FloatingActionButton mPlayTrailer;
    private FloatingActionButton mDisplayReview;

    private String trailerQuery;
    private ToggleButton starButton;

    private static final int TRAILER_NUM_LIST_ITEMS = 3;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mTrailerList;
    private ReviewAdapter mReviewAdapter;

    String recoveredIdNumber;
    String recoveredTitle;
    SharedPreferences sharedPreferences;

    private SQLiteDatabase mDb;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        // Hide Status Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        setContentView(R.layout.activity_child);

        hideSystemUI();

        mDisplayPoster = (ImageView) findViewById(R.id.child_iv);
        mDisplayTitle = (TextView) findViewById(R.id.child_tv_title);
        mDisplayPlot = (TextView) findViewById(R.id.child_tv_plot);
        mDisplayRating = (TextView) findViewById(R.id.child_tv_rating);
        mDisplayReleaseDate = (TextView) findViewById(R.id.child_tv_releaseDate);

        mPlayTrailer = (FloatingActionButton) findViewById(R.id.play_trailer);
        mDisplayReview = (FloatingActionButton) findViewById(R.id.display_reviews);

        FavoriteMovieListDbHelper dbHelper = new FavoriteMovieListDbHelper(this);
        mDb = dbHelper.getWritableDatabase();


        // TO SAVE TOGGLE BUTTON PREFERENCE METHOD

       /* SharedPreferences tprefs = getSharedPreferences("Favorite Selection",MODE_PRIVATE);
        starButton.setChecked(tprefs.getBoolean("ON",true));*/

        MovieDataParcel fetchData = getIntent().getParcelableExtra("dataLoaded");
        if (fetchData != null) {
            String recoveredUrl = fetchData.moviePoster;
            recoveredTitle = fetchData.movieTitle;
            String recoveredPlot = fetchData.moviePlot;
            String recoveredRating = fetchData.movieRating;
            String recoveredReleaseDate = fetchData.movieReleaseDate;


            recoveredIdNumber = fetchData.movieIdNumber;
            Log.v(TAG, "recoveredUrl: " + recoveredUrl);
            Log.v(TAG, "recoveredTitle: " + recoveredTitle);
            Log.v(TAG, "recoveredPlot: " + recoveredPlot);
            Log.v(TAG, "recoveredRating: " + recoveredRating);
            Log.v(TAG, "recoveredReleaseDate: " + recoveredReleaseDate);
            Log.v(TAG, "recoveredIdNumber: " + recoveredIdNumber);
            String picassoUrl = NetworkUtils.buildPosterUrl(recoveredUrl).toString();
            Picasso.with(this).load(picassoUrl).into(mDisplayPoster);
            mDisplayTitle.setText(recoveredTitle);
            mDisplayPlot.setText(recoveredPlot);
            mDisplayRating.setText(recoveredRating);
//            mDisplayReleaseDate.setText(recoveredReleaseDate);

            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String dateInString = recoveredReleaseDate;
            java.text.SimpleDateFormat targetFormat = new java.text.SimpleDateFormat("MMM yyyy");
            String formattedDate = null;

            try {
                Date date = formatter.parse(dateInString);
                Log.v(TAG,"UnFormatted Date:"+date);
                formattedDate = targetFormat.format(date);
                Log.v(TAG,"Formatted Date:"+formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mDisplayReleaseDate.setText(formattedDate);


            mPlayTrailer.setOnClickListener(new View.OnClickListener() {
                MovieDataParcel loadTrailerData = new MovieDataParcel(recoveredIdNumber);

                @Override
                public void onClick(View v) {

                    // starting an intent after clicking 'PLay Trailer Button' on Main screen
                    Class destinationActivity = TrailerActivity.class;
                    Intent startTrailerActivityIntent = new Intent(ChildActivity.this, destinationActivity);
                    startTrailerActivityIntent.putExtra("trailerDataLoaded", loadTrailerData);
                    startActivity(startTrailerActivityIntent);
                }
            });
            mDisplayReview.setOnClickListener(new View.OnClickListener() {
                MovieDataParcel loadTrailerData = new MovieDataParcel(recoveredIdNumber);


                @Override
                public void onClick(View v) {
                    // starting an intent after clicking 'Review Button' on Main screen
                    Class destinationActivity = ReviewActivity.class;
                    Intent startTrailerActivityIntent = new Intent(ChildActivity.this, destinationActivity);
                    startTrailerActivityIntent.putExtra("trailerDataLoaded", loadTrailerData);
                    startActivity(startTrailerActivityIntent);
                }
            });

            // Try insering Toggle Button code here
            saveAndLoadToggle();
        }

    }
    public void saveAndLoadToggle(){
        starButton = (ToggleButton) findViewById(R.id.star);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // to load toggle button selection
        starButton.setChecked(sharedPreferences.getBoolean(recoveredIdNumber,false));

        // to save toggle button selection result

        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (starButton.isChecked()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(recoveredIdNumber,true);
                    editor.commit();
                    addNewFavoriteMovie(recoveredTitle, recoveredIdNumber);
                    Log.v(TAG,"Toggle Button is clicked ON");
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(recoveredIdNumber,false);
                    editor.commit();
                    removeFavoriteMovie(recoveredTitle);
                    Log.v(TAG,"Toggle Button is clicked OFF");
                }
            }
        });
    }
    public long addNewFavoriteMovie (String movieName, String movieId){
        ContentValues cv = new ContentValues();
        cv.put(FavoriteMovieListContract.FavoriteMovieListEntry.COLUMN_MOVIE_NAME,movieName);
        cv.put(FavoriteMovieListContract.FavoriteMovieListEntry.COLUMN_MOVIE_ID,movieId);
        return mDb.insert(FavoriteMovieListContract.FavoriteMovieListEntry.TABLE_NAME, null, cv);
    }
    private boolean removeFavoriteMovie(String name) {
        recoveredTitle = name;
       /* return mDb.delete(FavoriteMovieListContract.FavoriteMovieListEntry.TABLE_NAME,
                FavoriteMovieListContract.FavoriteMovieListEntry.COLUMN_MOVIE_NAME + "=" + recoveredTitle,null) > 0;*/

        return mDb.delete(FavoriteMovieListContract.FavoriteMovieListEntry.TABLE_NAME,
                FavoriteMovieListContract.FavoriteMovieListEntry.COLUMN_MOVIE_NAME + "=?",
                new String[] { recoveredTitle }) > 0;
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
