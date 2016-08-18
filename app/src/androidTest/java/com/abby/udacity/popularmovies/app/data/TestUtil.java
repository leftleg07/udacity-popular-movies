package com.abby.udacity.popularmovies.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.abby.udacity.popularmovies.app.data.db.MovieContract;
import com.abby.udacity.popularmovies.app.data.db.MovieDbHelper;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * util
 */
public class TestUtil {
    /**
     * delete database
     * @param context
     */
    static void deleteDatabase(Context context) {context.deleteDatabase(MovieDbHelper.DATABASE_NAME);}

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            String currentValue = valueCursor.getString(idx);
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, currentValue);
        }
    }

    static ContentValues createMovieEntryValues() {
        ContentValues entryValues = new ContentValues();
        entryValues.put(MovieContract.MovieColumns._ID , 209112);
        entryValues.put(MovieContract.MovieColumns.COLUMN_ORIGINAL_TITLE, "Batman v Superman: Dawn of Justice");
        entryValues.put(MovieContract.MovieColumns.COLUMN_POSTER_PATH, "/cGOPbv9wA5gEejkUN892JrveARt.jpg");
        entryValues.put(MovieContract.MovieColumns.COLUMN_OVERVIEW, "Fearing the actions of a god-like Super Hero left unchecked, Gotham City’s own formidable, forceful vigilante takes on Metropolis’s most revered, modern-day savior, while the world wrestles with what sort of hero it really needs. And with Batman and Superman at war with one another, a new threat quickly arises, putting mankind in greater danger than it’s ever known before.");
        entryValues.put(MovieContract.MovieColumns.COLUMN_POPULARITY , 39.3386);
        entryValues.put(MovieContract.MovieColumns.COLUMN_VOTE_AVERAGE, 5.55);
        entryValues.put(MovieContract.MovieColumns.COLUMN_RELEASE_DATE, "2016-03-23");

        return entryValues;
    }

    static long insertMoveEntryValues(Context context, String table) {
        SQLiteDatabase db = new MovieDbHelper(context).getWritableDatabase();
        ContentValues testValues = TestUtil.createMovieEntryValues();

        long entryRowId;
        entryRowId = db.insert(table, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Movie Entry Values", entryRowId != -1);

        db.close();
        return entryRowId;
    }

    static ContentValues createReviewEntryValues() {
        ContentValues entryValues = new ContentValues();
        entryValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID , "56f4f0bd9251417a440017bd");
        entryValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, 209112);
        entryValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, "Rahul Gupta");
        entryValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, "Awesome moview. Best Action sequence. **Slow in the first half**");
        entryValues.put(MovieContract.ReviewEntry.COLUMN_URL, "https://www.themoviedb.org/review/56f4f0bd9251417a440017bd");

        return entryValues;
    }

    static ContentValues createVideoEntryValues() {
        ContentValues entryValues = new ContentValues();
        entryValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID, "571c8dc4c3a36842aa000190");
        entryValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, 209112);
        entryValues.put(MovieContract.TrailerEntry.COLUMN_KEY, "6as8ahAr1Uc");
        entryValues.put(MovieContract.TrailerEntry.COLUMN_NAME, "Exclusive Sneak");
        entryValues.put(MovieContract.TrailerEntry.COLUMN_SIZE, 1080);
        entryValues.put(MovieContract.TrailerEntry.COLUMN_TYPE, "Teaser");

        return entryValues;
    }

    static long insertReviewEntryValues(Context context) {
        SQLiteDatabase db = new MovieDbHelper(context).getWritableDatabase();
        ContentValues testValues = TestUtil.createReviewEntryValues();

        long entryRowId;
        entryRowId = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Review Entry Values", entryRowId != -1);

        db.close();
        return entryRowId;
    }

    static long insertVideoEntryValues(Context context) {
        SQLiteDatabase db = new MovieDbHelper(context).getWritableDatabase();
        ContentValues testValues = TestUtil.createVideoEntryValues();

        long entryRowId;
        entryRowId = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Trailer Entry Values", entryRowId != -1);

        db.close();
        return entryRowId;
    }

}
