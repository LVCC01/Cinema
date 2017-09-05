package com.example.android.cinema.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hjadhav on 8/21/2017.
 */

public class FavoriteMovieListDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritelist.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMovieListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            final String SQL_CREATE_FAVORITELIST_TABLE = "CREATE TABLE " + FavoriteMovieListContract.FavoriteMovieListEntry.TABLE_NAME + " ("+
                    FavoriteMovieListContract.FavoriteMovieListEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                    FavoriteMovieListContract.FavoriteMovieListEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL" +
                    "); ";
        db.execSQL(SQL_CREATE_FAVORITELIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ FavoriteMovieListContract.FavoriteMovieListEntry.TABLE_NAME);
        onCreate(db);
    }
}
