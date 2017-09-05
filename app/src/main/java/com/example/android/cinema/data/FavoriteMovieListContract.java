package com.example.android.cinema.data;

import android.provider.BaseColumns;

/**
 * Created by hjadhav on 8/21/2017.
 */

public class FavoriteMovieListContract {
    public static final class FavoriteMovieListEntry implements BaseColumns {
        public static final String TABLE_NAME = "favoritelist";
        public static final String COLUMN_MOVIE_NAME = "movieName";
        public static final String COLUMN_MOVIE_ID = "movieId";
    }
}
