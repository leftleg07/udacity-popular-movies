package com.abby.udacity.popularmovies.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by gsshop on 2016. 7. 28..
 */
@RunWith(AndroidJUnit4.class)
public class TestDb {
    private Context mContext;

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();
        TestUtility.deleteDatabase(mContext);
    }

    /**
     * create db
     *
     * @throws Exception
     */
    @Test
    public void testCreateDb() throws Exception {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.PopularMovieEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.TopRatedMovieEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.FavoriteMovieEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.ReviewEntry.TABLE_NAME);

        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without the popular movie and the top related movie and the favorite movie and review tables",
                tableNameHashSet.isEmpty());


        /**
         * popluar movie table
         */

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.PopularMovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> entryColumnHashSet = new HashSet<String>();
        entryColumnHashSet.add(MovieContract.MovieColumns._ID);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_ORIGINAL_TITLE);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_POSTER_PATH);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_OVERVIEW);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_VOTE_AVERAGE);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_POPULARITY);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_RELEASE_DATE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            entryColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required popular movie entry columns",
                entryColumnHashSet.isEmpty());


        /**
         * top related movie table
         */
        entryColumnHashSet.clear();
        c.close();
        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.TopRatedMovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        entryColumnHashSet.add(MovieContract.MovieColumns._ID);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_ORIGINAL_TITLE);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_POSTER_PATH);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_OVERVIEW);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_VOTE_AVERAGE);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_POPULARITY);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_RELEASE_DATE);

        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            entryColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required top related movie entry columns",
                entryColumnHashSet.isEmpty());

        /**
         * favorite movie table
         */
        entryColumnHashSet.clear();
        c.close();
        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.FavoriteMovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        entryColumnHashSet.add(MovieContract.MovieColumns._ID);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_ORIGINAL_TITLE);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_POSTER_PATH);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_OVERVIEW);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_VOTE_AVERAGE);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_POPULARITY);
        entryColumnHashSet.add(MovieContract.MovieColumns.COLUMN_RELEASE_DATE);

        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            entryColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required favorite movie entry columns",
                entryColumnHashSet.isEmpty());

        /**
         * review table
         */
        entryColumnHashSet.clear();
        c.close();
        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.ReviewEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for

        entryColumnHashSet.add(MovieContract.ReviewEntry._ID);
        entryColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_REVIEW_ID);
        entryColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_MOVIE_ID);
        entryColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_AUTHOR);
        entryColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_CONTENT);
        entryColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_URL);

        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            entryColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required review entry columns",
                entryColumnHashSet.isEmpty());

        c.close();
        db.close();

    }

    /**
     * popular movie table
     *
     * @throws Exception
     */
    @Test
    public void testPopularMovieTable() throws Exception {

        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        ContentValues testValues = TestUtility.createMovieEntryValues();

        // insert data
        TestUtility.insertMoveEntryValues(mContext, MovieContract.PopularMovieEntry.TABLE_NAME);

        Cursor cursor = db.query(
                MovieContract.PopularMovieEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        assertTrue( "Error: No Records returned from popular movie query", cursor.moveToFirst() );

        TestUtility.validateCurrentRecord("Error: Popular Movie Query Validation Failed", cursor, testValues);

        cursor.close();

        db.close();

    }

    /**
     * top related movie table
     *
     * @throws Exception
     */
    @Test
    public void testTopRelatedMovieTable() throws Exception {

        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        ContentValues testValues = TestUtility.createMovieEntryValues();

        // insert data
        TestUtility.insertMoveEntryValues(mContext, MovieContract.TopRatedMovieEntry.TABLE_NAME);

        Cursor cursor = db.query(
                MovieContract.TopRatedMovieEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        assertTrue( "Error: No Records returned from top related movie query", cursor.moveToFirst() );

        TestUtility.validateCurrentRecord("Error: Top related Movie Query Validation Failed", cursor, testValues);

        cursor.close();
        db.close();
    }

    /**
     * favorite movie table
     *
     * @throws Exception
     */
    @Test
    public void testFavoriteMovieTable() throws Exception {

        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        ContentValues testValues = TestUtility.createMovieEntryValues();

        // insert data
        TestUtility.insertMoveEntryValues(mContext, MovieContract.FavoriteMovieEntry.TABLE_NAME);

        Cursor cursor = db.query(
                MovieContract.FavoriteMovieEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        assertTrue( "Error: No Records returned from favorite movie query", cursor.moveToFirst() );

        TestUtility.validateCurrentRecord("Error: Favorite Movie Query Validation Failed", cursor, testValues);

        cursor.close();
        db.close();

    }

    /**
     * review table
     * @throws Exception
     */
    @Test
    public void testReviewTable() throws Exception {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        ContentValues testValues = TestUtility.createReviewEntryValues();

        // insert data
        TestUtility.insertReviewEntryValues(mContext);

        Cursor cursor = db.query(
                MovieContract.ReviewEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        assertTrue( "Error: No Records returned from review query", cursor.moveToFirst() );

        TestUtility.validateCurrentRecord("Error: Review Query Validation Failed", cursor, testValues);

        cursor.close();
        db.close();

    }
}
