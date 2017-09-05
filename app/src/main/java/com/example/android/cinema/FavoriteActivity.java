package com.example.android.cinema;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.cinema.data.FavoriteMovieListContract;
import com.example.android.cinema.data.FavoriteMovieListDbHelper;

public class FavoriteActivity extends AppCompatActivity {
    private FavoriteAdapter mFavoriteAdapter;
    private RecyclerView mFavoriteListRecyclerView;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mFavoriteListRecyclerView = (RecyclerView) this.findViewById(R.id.favorite_movies_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mFavoriteListRecyclerView.setLayoutManager(layoutManager);

        FavoriteMovieListDbHelper dbHelper = new FavoriteMovieListDbHelper(this);
        mDb = dbHelper.getReadableDatabase();

        Cursor cursor = getAllFavoriteMovies();
        mFavoriteAdapter = new FavoriteAdapter(this,cursor);
        mFavoriteListRecyclerView.setAdapter(mFavoriteAdapter);
    }

    private Cursor getAllFavoriteMovies(){
        return mDb.query(
                FavoriteMovieListContract.FavoriteMovieListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavoriteMovieListContract.FavoriteMovieListEntry.COLUMN_MOVIE_NAME
        );
    }



}
