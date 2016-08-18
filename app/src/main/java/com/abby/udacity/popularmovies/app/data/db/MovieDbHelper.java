package com.abby.udacity.popularmovies.app.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gsshop on 2016. 7. 28..
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // popular movie table
        final String SQL_CREATE_POPULAR_MOVE_TABLE = "CREATE TABLE " + MovieContract.PopularMovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieColumns._ID + " INTEGER PRIMARY KEY," +
                MovieContract.MovieColumns.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MovieContract.MovieColumns.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                MovieContract.MovieColumns.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MovieContract.MovieColumns.COLUMN_POPULARITY + " REAL NOT NULL," +
                MovieContract.MovieColumns.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieContract.MovieColumns.COLUMN_RELEASE_DATE + " REAL NOT NULL " +
                " );";

        // top related movie table
        final String SQL_CREATE_TOP_RELATED_MOVE_TABLE = "CREATE TABLE " + MovieContract.HighestRatedMovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieColumns._ID + " INTEGER PRIMARY KEY," +
                MovieContract.MovieColumns.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MovieContract.MovieColumns.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                MovieContract.MovieColumns.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MovieContract.MovieColumns.COLUMN_POPULARITY + " REAL NOT NULL," +
                MovieContract.MovieColumns.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieContract.MovieColumns.COLUMN_RELEASE_DATE + " REAL NOT NULL " +
                " );";

        // favorite move table
        final String SQL_CREATE_FAVORITE_MOVE_TABLE = "CREATE TABLE " + MovieContract.FavoriteMovieEntry.TABLE_NAME + " (" +
                MovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MovieContract.FavoriteMovieEntry.COLUMN_POPULARITY + " REAL NOT NULL," +
                MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE + " REAL NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_REGISTERED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP " +
                " );";

        // review table
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MovieContract.ReviewEntry.TABLE_NAME + " (" +
                MovieContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.ReviewEntry.COLUMN_REVIEW_ID + " TEXT UNIQUE NOT NULL," +
                MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                MovieContract.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL," +
                MovieContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL," +
                MovieContract.ReviewEntry.COLUMN_URL + " TEXT NOT NULL" +
                " );";

        // review table
        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + MovieContract.VideoEntry.TABLE_NAME + " (" +
                MovieContract.VideoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.VideoEntry.COLUMN_VIDEO_ID + " TEXT UNIQUE NOT NULL," +
                MovieContract.VideoEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                MovieContract.VideoEntry.COLUMN_KEY + " TEXT NOT NULL," +
                MovieContract.VideoEntry.COLUMN_NAME + " TEXT NOT NULL," +
                MovieContract.VideoEntry.COLUMN_SIZE + " INTEGER NOT NULL," +
                MovieContract.VideoEntry.COLUMN_TYPE + " TEXT NOT NULL" +
                " );";

        db.execSQL(SQL_CREATE_POPULAR_MOVE_TABLE);
        db.execSQL(SQL_CREATE_TOP_RELATED_MOVE_TABLE);
        db.execSQL(SQL_CREATE_FAVORITE_MOVE_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
        db.execSQL(SQL_CREATE_VIDEO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.PopularMovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.HighestRatedMovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoriteMovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.VideoEntry.TABLE_NAME);
        onCreate(db);
    }
}
