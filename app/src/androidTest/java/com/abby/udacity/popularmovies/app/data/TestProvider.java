package com.abby.udacity.popularmovies.app.data;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.abby.udacity.popularmovies.app.data.db.MovieContract;
import com.abby.udacity.popularmovies.app.data.db.MovieProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by heim on 7/30/16.
 */
@RunWith(AndroidJUnit4.class)
public class TestProvider {
    private static final int MOVIE_ID = 209112;
    private ContentResolver mContentResolver;
    private Context mContext;

    void deleteAllRecord() {

        mContentResolver.delete(
                MovieContract.PopularMovieEntry.CONTENT_URI,
                null,
                null
        );

        mContentResolver.delete(
                MovieContract.TopRatedMovieEntry.CONTENT_URI,
                null,
                null
        );

        mContentResolver.delete(
                MovieContract.FavoriteMovieEntry.CONTENT_URI,
                null,
                null
        );


        mContentResolver.delete(
                MovieContract.ReviewEntry.CONTENT_URI,
                null,
                null
        );

        mContentResolver.delete(
                MovieContract.VideoEntry.CONTENT_URI,
                null,
                null
        );


        Cursor cursor = mContentResolver.query(
                MovieContract.PopularMovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from popular movie table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContentResolver.query(
                MovieContract.TopRatedMovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from top related movie table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContentResolver.query(
                MovieContract.FavoriteMovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from favorite movie table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContentResolver.query(
                MovieContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from review table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContentResolver.query(
                MovieContract.VideoEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from video table during delete", 0, cursor.getCount());
        cursor.close();

    }

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();
        mContentResolver = InstrumentationRegistry.getTargetContext().getContentResolver();
        deleteAllRecord();
    }

    @Test
    public void testProviderRegistry() throws Exception {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // MovieProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
                    false);
        }

    }

    @Test
    public void testGetType() throws Exception {
        final int movieId = 209112;
        final String reviewId = "56f4f0bd9251417a440017bd";
        String type = mContentResolver.getType(MovieContract.PopularMovieEntry.CONTENT_URI);
        assertEquals("Error: the PopularMovieEntry CONTENT_URI should return PopularMovieEntry.CONTENT_TYPE",
                MovieContract.PopularMovieEntry.CONTENT_TYPE, type);

        type = mContentResolver.getType(MovieContract.PopularMovieEntry.buildPopularMovieUri(movieId));
        assertEquals("Error: the TopRatedMovieEntry CONTENT_URI with id should return PopularMovieEntry.CONTENT_ITEM_TYPE",
                MovieContract.PopularMovieEntry.CONTENT_ITEM_TYPE, type);

        type = mContentResolver.getType(MovieContract.TopRatedMovieEntry.CONTENT_URI);
        assertEquals("Error: the TopRatedMovieEntry CONTENT_URI should return TopRatedMovieEntry.CONTENT_TYPE",
                MovieContract.TopRatedMovieEntry.CONTENT_TYPE, type);

        type = mContentResolver.getType(MovieContract.TopRatedMovieEntry.buildTopRelatedMovieUri(movieId));
        assertEquals("Error: the TopRatedMovieEntry CONTENT_URI with id should return TopRatedMovieEntry.CONTENT_ITEM_TYPE",
                MovieContract.TopRatedMovieEntry.CONTENT_ITEM_TYPE, type);

        type = mContentResolver.getType(MovieContract.FavoriteMovieEntry.CONTENT_URI);
        assertEquals("Error: the FavoriteMovieEntry CONTENT_URI should return FavoriteMovieEntry.CONTENT_TYPE",
                MovieContract.FavoriteMovieEntry.CONTENT_TYPE, type);

        type = mContentResolver.getType(MovieContract.FavoriteMovieEntry.buildFavoriteMovieUri(movieId));
        assertEquals("Error: the FavoriteMovieEntry CONTENT_URI with id should return FavoriteMovieEntry.CONTENT_ITEM_TYPE",
                MovieContract.FavoriteMovieEntry.CONTENT_ITEM_TYPE, type);

        type = mContentResolver.getType(MovieContract.ReviewEntry.CONTENT_URI);
        assertEquals("Error: the ReviewEntry CONTENT_URI should return ReviewEntry.CONTENT_TYPE",
                MovieContract.ReviewEntry.CONTENT_TYPE, type);

        type = mContentResolver.getType(MovieContract.ReviewEntry.buildReviewUri(reviewId));
        assertEquals("Error: the ReviewEntry CONTENT_URI with id should return ReviewEntry.CONTENT_ITEM_TYPE",
                MovieContract.ReviewEntry.CONTENT_ITEM_TYPE, type);

        type = mContentResolver.getType(MovieContract.ReviewEntry.buildReviewMovieUri(movieId));
        assertEquals("Error: the ReviewEntry CONTENT_URI with movie should return ReviewEntry.CONTENT_ITEM_TYPE",
                MovieContract.ReviewEntry.CONTENT_ITEM_TYPE, type);
    }

    @Test
    public void testBasicPopularMovieQuery() throws Exception {
        // insert

        ContentValues testValues = TestUtility.createMovieEntryValues();
        ContentValues updateValues = new ContentValues(testValues);
        updateValues.put(MovieContract.MovieColumns.COLUMN_VOTE_AVERAGE, 1.2f);


        Uri uri = mContentResolver.insert(MovieContract.PopularMovieEntry.CONTENT_URI, testValues);
        long movieRowId = ContentUris.parseId(uri);

        assertEquals("Error: Popular Movie Query Validation Failed", testValues.getAsLong(MovieContract.MovieColumns._ID).longValue(), movieRowId);


        // update
        int count = mContentResolver.update(uri, updateValues, null, null);
        assertEquals(1, count);

        Cursor cursor = mContentResolver.query(uri, null, null, null, null, null);

        assertTrue("Error: No Records returned from popular movie query", cursor.moveToFirst());
        TestUtility.validateCurrentRecord("Error: Popular Movie Query Validation Failed", cursor, updateValues);
        cursor.close();

    }


    @Test
    public void testBasicTopRelatedMovieQuery() throws Exception {
        // insert

        ContentValues testValues = TestUtility.createMovieEntryValues();
        ContentValues updateValues = new ContentValues(testValues);
        updateValues.put(MovieContract.MovieColumns.COLUMN_VOTE_AVERAGE, 1.2f);

        Uri uri = mContentResolver.insert(MovieContract.TopRatedMovieEntry.CONTENT_URI, testValues);
        long movieRowId = ContentUris.parseId(uri);

        assertEquals("Error: Top related Movie Query Validation Failed", testValues.getAsLong(MovieContract.MovieColumns._ID).longValue(), movieRowId);

        // update

        int count = mContentResolver.update(uri, updateValues, null, null);
        assertEquals(1, count);


        Cursor cursor = mContentResolver.query(uri, null, null, null, null, null);

        assertTrue("Error: No Records returned from top related movie query", cursor.moveToFirst());
        TestUtility.validateCurrentRecord("Error: Top related Movie Query Validation Failed", cursor, updateValues);
        cursor.close();

    }


    @Test
    public void testBasicFavoriteMovieQuery() throws Exception {

        // insert

        ContentValues testValues = TestUtility.createMovieEntryValues();
        ContentValues updateValues = new ContentValues(testValues);
        updateValues.put(MovieContract.MovieColumns.COLUMN_VOTE_AVERAGE, 1.2f);

        Uri uri = mContentResolver.insert(MovieContract.FavoriteMovieEntry.CONTENT_URI, testValues);

        long movieRowId = ContentUris.parseId(uri);

        assertEquals("Error: Favorite Movie Query Validation Failed", testValues.getAsLong(MovieContract.MovieColumns._ID).longValue(), movieRowId);

        // update

        int count = mContentResolver.update(uri, updateValues, null, null);
        assertEquals(1, count);

        Cursor cursor = mContentResolver.query(uri, null, null, null, null, null);

        assertTrue("Error: No Records returned from favorite movie query", cursor.moveToFirst());
        TestUtility.validateCurrentRecord("Error: Favorite Movie Query Validation Failed", cursor, updateValues);
        cursor.close();

    }


    @Test
    public void testBasicReviewQuery() throws Exception {

        // insert
        ContentValues testValues = TestUtility.createReviewEntryValues();
        Uri uri = mContentResolver.insert(MovieContract.ReviewEntry.CONTENT_URI, testValues);

        String reviewId = uri.getLastPathSegment();
        assertEquals("Error: Review Query Validation Failed", testValues.getAsString(MovieContract.ReviewEntry.COLUMN_REVIEW_ID), reviewId);

        // update

        ContentValues updateValues = new ContentValues(testValues);
        updateValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, "Frank Ochieng");
        int count = mContentResolver.update(uri, updateValues, null, null);
        assertEquals(1, count);

        Cursor cursor = mContentResolver.query(uri, null, null, null, null, null);

        assertTrue("Error: No Records returned from Review query", cursor.moveToFirst());
        TestUtility.validateCurrentRecord("Error: Review Query Validation Failed", cursor, updateValues);
        cursor.close();

        uri = MovieContract.ReviewEntry.buildReviewMovieUri(MOVIE_ID);
        cursor = mContentResolver.query(uri, null, null, null, null, null);

        assertTrue("Error: No Records returned from Review query", cursor.moveToFirst());
        TestUtility.validateCurrentRecord("Error: Review Query Validation Failed", cursor, updateValues);
        cursor.close();

        uri = MovieContract.ReviewEntry.buildReviewMovieUri(MOVIE_ID);
        count = mContentResolver.delete(uri, null, null);
        assertEquals(1, count);

    }

    @Test
    public void testBasicVideoQuery() throws Exception {

        // insert
        ContentValues testValues = TestUtility.createVideoEntryValues();
        Uri uri = mContentResolver.insert(MovieContract.VideoEntry.CONTENT_URI, testValues);

        String videoId = uri.getLastPathSegment();
        assertEquals("Error: Review Query Validation Failed", testValues.getAsString(MovieContract.VideoEntry.COLUMN_VIDEO_ID), videoId);

        // update

        ContentValues updateValues = new ContentValues(testValues);
        updateValues.put(MovieContract.VideoEntry.COLUMN_NAME, "Frank Ochieng");
        int count = mContentResolver.update(uri, updateValues, null, null);
        assertEquals(1, count);

        Cursor cursor = mContentResolver.query(uri, null, null, null, null, null);

        assertTrue("Error: No Records returned from Video query", cursor.moveToFirst());
        TestUtility.validateCurrentRecord("Error: Video Query Validation Failed", cursor, updateValues);
        cursor.close();

        uri = MovieContract.VideoEntry.buildVideoMovieUri(MOVIE_ID);
        cursor = mContentResolver.query(uri, null, null, null, null, null);

        assertTrue("Error: No Records returned from Review query", cursor.moveToFirst());
        TestUtility.validateCurrentRecord("Error: Review Query Validation Failed", cursor, updateValues);
        cursor.close();

        uri = MovieContract.VideoEntry.buildVideoMovieUri(MOVIE_ID);
        count = mContentResolver.delete(uri, null, null);
        assertEquals(1, count);

    }
}
