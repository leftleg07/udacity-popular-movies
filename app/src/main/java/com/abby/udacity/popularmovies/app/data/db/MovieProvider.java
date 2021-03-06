package com.abby.udacity.popularmovies.app.data.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * {@link ContentProvider} that stores {@link MovieContract} data.
 */
public class MovieProvider extends ContentProvider {
    public static final int POPULAR_MOVIE = 100;
    public static final int POPULAR_MOVIE_WITH_ID = 101;
    public static final int HIGHEST_RATED_MOVIE = 200;
    public static final int HIGHEST_RATED_MOVIE_WITH_ID = 201;
    public static final int FAVORITE_MOVIE = 300;
    public static final int FAVORITE_MOVIE_WITH_ID = 301;
    public static final int REVIEW = 400;
    public static final int REVIEW_WITH_REVIEW_ID = 401;
    public static final int REVIEW_WITH_MOVIE_ID = 402;
    public static final int VIDEO = 500;
    public static final int VIDEO_WITH_VIDEO_ID = 501;
    public static final int VIDEO_WITH_MOVIE_ID = 502;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;


    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MovieContract.PATH_POPULAR_MOVIE, POPULAR_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_POPULAR_MOVIE + "/#", POPULAR_MOVIE_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_HIGHEST_RATED_MOVIE, HIGHEST_RATED_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_HIGHEST_RATED_MOVIE + "/#", HIGHEST_RATED_MOVIE_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIE, FAVORITE_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIE + "/#", FAVORITE_MOVIE_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/*", REVIEW_WITH_REVIEW_ID);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/*/#", REVIEW_WITH_MOVIE_ID);
        matcher.addURI(authority, MovieContract.PATH_VIDEO, VIDEO);
        matcher.addURI(authority, MovieContract.PATH_VIDEO + "/*", VIDEO_WITH_VIDEO_ID);
        matcher.addURI(authority, MovieContract.PATH_VIDEO + "/*/#", VIDEO_WITH_MOVIE_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        final int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case POPULAR_MOVIE_WITH_ID:
                selection = MovieContract.MovieColumns._ID + " =? ";
                selectionArgs = new String[]{Long.toString(ContentUris.parseId(uri))};
            case POPULAR_MOVIE:
                sortOrder = MovieContract.MovieColumns.COLUMN_POPULARITY + " DESC";
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.PopularMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case HIGHEST_RATED_MOVIE_WITH_ID:
                selection = MovieContract.MovieColumns._ID + " =? ";
                selectionArgs = new String[]{Long.toString(ContentUris.parseId(uri))};
            case HIGHEST_RATED_MOVIE:
                sortOrder = MovieContract.MovieColumns.COLUMN_VOTE_AVERAGE + " DESC";
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.HighestRatedMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAVORITE_MOVIE_WITH_ID:
                selection = MovieContract.MovieColumns._ID + " =? ";
                selectionArgs = new String[]{Long.toString(ContentUris.parseId(uri))};
            case FAVORITE_MOVIE:
                sortOrder = MovieContract.FavoriteMovieEntry.COLUMN_REGISTERED_DATE + " ASC";
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case REVIEW_WITH_REVIEW_ID:
                selection = MovieContract.ReviewEntry.COLUMN_REVIEW_ID + " =? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
            case REVIEW:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case REVIEW_WITH_MOVIE_ID:
                selection = MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " =? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                sortOrder = MovieContract.ReviewEntry.COLUMN_REVIEW_ID + " ASC";
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case VIDEO_WITH_VIDEO_ID:
                selection = MovieContract.TrailerEntry.COLUMN_TRAILER_ID + " =? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
            case VIDEO:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case VIDEO_WITH_MOVIE_ID:
                selection = MovieContract.TrailerEntry.COLUMN_MOVIE_ID + " =? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                sortOrder = MovieContract.TrailerEntry.COLUMN_TRAILER_ID + " ASC";
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case POPULAR_MOVIE:
                return MovieContract.PopularMovieEntry.CONTENT_TYPE;
            case POPULAR_MOVIE_WITH_ID:
                return MovieContract.PopularMovieEntry.CONTENT_ITEM_TYPE;
            case HIGHEST_RATED_MOVIE:
                return MovieContract.HighestRatedMovieEntry.CONTENT_TYPE;
            case HIGHEST_RATED_MOVIE_WITH_ID:
                return MovieContract.HighestRatedMovieEntry.CONTENT_ITEM_TYPE;
            case FAVORITE_MOVIE:
                return MovieContract.FavoriteMovieEntry.CONTENT_TYPE;
            case FAVORITE_MOVIE_WITH_ID:
                return MovieContract.FavoriteMovieEntry.CONTENT_ITEM_TYPE;
            case REVIEW:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case REVIEW_WITH_REVIEW_ID:
            case REVIEW_WITH_MOVIE_ID:
                return MovieContract.ReviewEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case POPULAR_MOVIE:
            case POPULAR_MOVIE_WITH_ID: {
                long _id = db.insert(MovieContract.PopularMovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.PopularMovieEntry.buildPopularMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case HIGHEST_RATED_MOVIE:
            case HIGHEST_RATED_MOVIE_WITH_ID: {
                ContentValues my = new ContentValues(values);
                long _id = db.insert(MovieContract.HighestRatedMovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.HighestRatedMovieEntry.buildTopRelatedMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVORITE_MOVIE:
            case FAVORITE_MOVIE_WITH_ID: {
                long _id = db.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.FavoriteMovieEntry.buildFavoriteMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW:
            case REVIEW_WITH_REVIEW_ID:
            case REVIEW_WITH_MOVIE_ID: {
                long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    String reviewId = values.getAsString(MovieContract.ReviewEntry.COLUMN_REVIEW_ID);
                    returnUri = MovieContract.ReviewEntry.buildReviewUri(reviewId);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case VIDEO:
            case VIDEO_WITH_VIDEO_ID:
            case VIDEO_WITH_MOVIE_ID: {
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    String reviewId = values.getAsString(MovieContract.TrailerEntry.COLUMN_TRAILER_ID);
                    returnUri = MovieContract.TrailerEntry.buildTrailerUri(reviewId);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case POPULAR_MOVIE_WITH_ID:
                selection = MovieContract.MovieColumns._ID + " =? ";
                selectionArgs = new String[]{Long.toString(ContentUris.parseId(uri))};
            case POPULAR_MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.PopularMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HIGHEST_RATED_MOVIE_WITH_ID:
                selection = MovieContract.MovieColumns._ID + " =? ";
                selectionArgs = new String[]{Long.toString(ContentUris.parseId(uri))};
            case HIGHEST_RATED_MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.HighestRatedMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE_MOVIE_WITH_ID:
                selection = MovieContract.MovieColumns._ID + " =? ";
                selectionArgs = new String[]{Long.toString(ContentUris.parseId(uri))};
            case FAVORITE_MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEW_WITH_REVIEW_ID:
                selection = MovieContract.ReviewEntry.COLUMN_REVIEW_ID + " =? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
            case REVIEW:
                rowsDeleted = db.delete(
                        MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEW_WITH_MOVIE_ID:
                selection = MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " =? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                rowsDeleted = db.delete(
                        MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case VIDEO_WITH_VIDEO_ID:
                selection = MovieContract.TrailerEntry.COLUMN_TRAILER_ID + " =? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
            case VIDEO:
                rowsDeleted = db.delete(
                        MovieContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case VIDEO_WITH_MOVIE_ID:
                selection = MovieContract.TrailerEntry.COLUMN_MOVIE_ID + " =? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                rowsDeleted = db.delete(
                        MovieContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case POPULAR_MOVIE_WITH_ID:
                selection = MovieContract.MovieColumns._ID + " =? ";
                selectionArgs = new String[]{Long.toString(ContentUris.parseId(uri))};
            case POPULAR_MOVIE:
                rowsUpdated = db.update(MovieContract.PopularMovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case HIGHEST_RATED_MOVIE_WITH_ID:
                selection = MovieContract.MovieColumns._ID + " =? ";
                selectionArgs = new String[]{Long.toString(ContentUris.parseId(uri))};
            case HIGHEST_RATED_MOVIE:
                rowsUpdated = db.update(MovieContract.HighestRatedMovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FAVORITE_MOVIE_WITH_ID:
                selection = MovieContract.MovieColumns._ID + " =? ";
                selectionArgs = new String[]{Long.toString(ContentUris.parseId(uri))};
            case FAVORITE_MOVIE:
                rowsUpdated = db.update(MovieContract.FavoriteMovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case REVIEW_WITH_REVIEW_ID:
                selection = MovieContract.ReviewEntry.COLUMN_REVIEW_ID + " =? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
            case REVIEW:
                rowsUpdated = db.update(MovieContract.ReviewEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case REVIEW_WITH_MOVIE_ID:
                selection = MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " =? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                rowsUpdated = db.update(MovieContract.ReviewEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case VIDEO_WITH_VIDEO_ID:
                selection = MovieContract.TrailerEntry.COLUMN_TRAILER_ID + " =? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
            case VIDEO:
                rowsUpdated = db.update(MovieContract.TrailerEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case VIDEO_WITH_MOVIE_ID:
                selection = MovieContract.TrailerEntry.COLUMN_MOVIE_ID + " =? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                rowsUpdated = db.update(MovieContract.TrailerEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
