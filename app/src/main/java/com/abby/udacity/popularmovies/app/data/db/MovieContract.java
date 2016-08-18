package com.abby.udacity.popularmovies.app.data.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gsshop on 2016. 7. 28..
 */
public class MovieContract {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.abby.udacity.popularmovies.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_POPULAR_MOVIE = "popular_movie";
    public static final String PATH_HIGHEST_RATED_MOVIE = "highest_rated_movie";
    public static final String PATH_FAVORITE_MOVIE = "favorite_movie";
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_VIDEO = "video";

    public interface MovieColumns extends BaseColumns {
        String COLUMN_ORIGINAL_TITLE = "original_title";
        String COLUMN_POSTER_PATH = "poster_path";
        String COLUMN_OVERVIEW = "overview";
        String COLUMN_POPULARITY = "popularity";
        String COLUMN_VOTE_AVERAGE = "vote_average";
        String COLUMN_RELEASE_DATE = "release_date";

        /**
         * Project used when querying content provider. Returns all known fields.
         */
        String[] PROJECTION = new String[]{
                _ID,
                COLUMN_ORIGINAL_TITLE,
                COLUMN_POSTER_PATH,
                COLUMN_OVERVIEW,
                COLUMN_POPULARITY,
                COLUMN_VOTE_AVERAGE,
                COLUMN_RELEASE_DATE

        };
        // these indices must match the projection
        int INDEX_ID = 0;
        int INDEX_COLUMN_ORIGINAL_TITLE = 1;
        int INDEX_COLUMN_POSTER_PATH = 2;
        int INDEX_COLUMN_OVERVIEW = 3;
        int INDEX_COLUMN_POPULARITY = 4;
        int INDEX_COLUMN_VOTE_AVERAGE = 5;
        int INDEX_COLUMN_RELEASE_DATE = 6;
    }

    public static final class PopularMovieEntry {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULAR_MOVIE).build();

        public static final String TABLE_NAME = "popular";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR_MOVIE;

        public static Uri buildPopularMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class HighestRatedMovieEntry {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HIGHEST_RATED_MOVIE).build();

        public static final String TABLE_NAME = "highest_rated";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HIGHEST_RATED_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HIGHEST_RATED_MOVIE;

        public static Uri buildTopRelatedMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class FavoriteMovieEntry implements MovieColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIE).build();

        public static final String COLUMN_REGISTERED_DATE = "registered_date";

        public static final String TABLE_NAME = "favorite";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE_MOVIE;
        public static Uri buildFavoriteMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ReviewEntry implements BaseColumns {

        public static final String TABLE_NAME = "review";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static Uri buildReviewUri(String id) {
            return CONTENT_URI.buildUpon().appendEncodedPath(id).build();
        }

        public static Uri buildReviewMovieUri(long movieId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).appendPath("" + movieId).build();
        }

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        /**
         * Project used when querying content provider. Returns all known fields.
         */
        public static final String[] PROJECTION = new String[]{
                _ID,
                COLUMN_REVIEW_ID,
                COLUMN_MOVIE_ID,
                COLUMN_AUTHOR,
                COLUMN_CONTENT,
                COLUMN_URL,
        };

        // these indices must match the projection
        public static final int INDEX_ID = 0;
        public static final int INDEX_COLUMN_REVIEW_ID = 1;
        public static final int INDEX_COLUMN_MOVIE_ID = 2;
        public static final int INDEX_COLUMN_AUTHOR = 3;
        public static final int INDEX_COLUMN_CONTENT = 4;
        public static final int INDEX_COLUMN_URL = 5;
    }

    public static final class VideoEntry implements BaseColumns {
        public static final String TABLE_NAME = "video";

        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_TYPE = "type";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEO).build();

        public static Uri buildVideoUri(String id) {
            return CONTENT_URI.buildUpon().appendEncodedPath(id).build();
        }

        public static Uri buildVideoMovieUri(long movieId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).appendPath("" + movieId).build();
        }

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;

        /**
         * Project used when querying content provider. Returns all known fields.
         */
        public static final String[] PROJECTION = new String[]{
                _ID,
                COLUMN_VIDEO_ID,
                COLUMN_MOVIE_ID,
                COLUMN_KEY,
                COLUMN_NAME,
                COLUMN_SIZE,
                COLUMN_TYPE

        };

        // these indices must match the projection
        public static final int INDEX_ID = 0;
        public static final int INDEX_COLUMN_VIDEO_ID = 1;
        public static final int INDEX_COLUMN_MOVIE_ID = 2;
        public static final int INDEX_COLUMN_KEY = 3;
        public static final int INDEX_COLUMN_NAME = 4;
        public static final int INDEX_COLUMN_SIZE = 5;
        public static final int INDEX_COLUMN_TYPE = 6;

    }
}
