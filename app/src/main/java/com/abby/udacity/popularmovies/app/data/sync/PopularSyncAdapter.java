package com.abby.udacity.popularmovies.app.data.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.util.Log;

import com.abby.udacity.popularmovies.app.MainApplication;
import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.data.db.MovieContract;
import com.abby.udacity.popularmovies.app.data.model.Movie;
import com.abby.udacity.popularmovies.app.data.network.TheMovieDBApiService;
import com.abby.udacity.popularmovies.app.util.Util;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.internal.Preconditions;

public class PopularSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String TAG = PopularSyncAdapter.class.getSimpleName();

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ORDER_MODE_MOST_POPULAR, ORDER_MODE_HIGHEST_RATED})
    public @interface OrderMode {
    }

    public static final int ORDER_MODE_MOST_POPULAR = 0;
    public static final int ORDER_MODE_HIGHEST_RATED = 1;


    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int MOVIE_NOTIFICATION_ID = 3004;

    private final String DEFAULT_ORDER_MOST_POPULAR = getContext().getString(R.string.pref_mode_value_most_popular);


    /**
     * Content resolver, for performing database operations.
     */
    ContentResolver mContentResolver;

    @Inject
    TheMovieDBApiService mService;

    @Inject
    SharedPreferences mPrefs;

    @Inject
    Context applicationContext;


    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public PopularSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        ((MainApplication) context).getComponent().inject(this);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public PopularSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        ((MainApplication) context).getComponent().inject(this);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");

        String order = mPrefs.getString(getContext().getString(R.string.pref_mode_key), DEFAULT_ORDER_MOST_POPULAR);
        int mode = -1;
        if (order.equals(DEFAULT_ORDER_MOST_POPULAR)) {
            mode = ORDER_MODE_MOST_POPULAR;
        } else {
            mode = ORDER_MODE_HIGHEST_RATED;
        }
        Uri contentUri = (mode == ORDER_MODE_MOST_POPULAR ? MovieContract.PopularMovieEntry.CONTENT_URI : MovieContract.HighestRatedMovieEntry.CONTENT_URI);
        Cursor cursor = mContentResolver.query(contentUri, MovieContract.MovieColumns.PROJECTION, null, null, null);
        Preconditions.checkNotNull(cursor);
        if (cursor.getCount() > 0) {
            mContentResolver.notifyChange(
                    contentUri, // URI where data was modified
                    null,                           // No local observer
                    false);                         // IMPORTANT: Do not sync to network
            // This sample doesn't support uploads, but if *your* code does, make sure you set
        }

        if (!Util.isOnline(getContext())) {
            return;
        }

        String jsonStr = mService.getPopularMovie(order).toBlocking().single();
        try {
            if (order.equals(DEFAULT_ORDER_MOST_POPULAR)) {
                updateLocalPopularMovieData(jsonStr, syncResult, ORDER_MODE_MOST_POPULAR);
            } else {
                updateLocalPopularMovieData(jsonStr, syncResult, ORDER_MODE_HIGHEST_RATED);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (RemoteException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        } catch (OperationApplicationException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        }

        Log.i(TAG, "Network synchronization complete");
    }

    public void updateLocalPopularMovieData(String jsonStr, final SyncResult syncResult, @OrderMode int mode) throws JSONException, RemoteException, OperationApplicationException {
        final List<Movie> entries = getPopularMovieDataFromJson(jsonStr);

        final ArrayList<ContentProviderOperation> batch = new ArrayList<>();

        // Build hash table of incoming entries
        final HashMap<Long, Movie> entryMap = new HashMap<>();
        for (Movie e : entries) {
            entryMap.put(e.mId, e);
        }

        Uri contentUri = (mode == ORDER_MODE_MOST_POPULAR ? MovieContract.PopularMovieEntry.CONTENT_URI : MovieContract.HighestRatedMovieEntry.CONTENT_URI);
        Cursor cursor = mContentResolver.query(contentUri, MovieContract.MovieColumns.PROJECTION, null, null, null);
        Preconditions.checkNotNull(cursor);

        Log.i(TAG, "Found " + cursor.getCount() + " local entries. Computing merge solution...");

        while (cursor.moveToNext()) {
            syncResult.stats.numEntries++;
            long id = cursor.getLong(MovieContract.MovieColumns.INDEX_ID);
            Movie match = entryMap.get(id);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(id);
                // Check to see if the entry needs to be updated
                String originalTitle = cursor.getString(MovieContract.MovieColumns.INDEX_COLUMN_ORIGINAL_TITLE);
                String poserPath = cursor.getString(MovieContract.MovieColumns.INDEX_COLUMN_POSTER_PATH);
                String overview = cursor.getString(MovieContract.MovieColumns.INDEX_COLUMN_OVERVIEW);
                double voteAverage = cursor.getDouble(MovieContract.MovieColumns.INDEX_COLUMN_VOTE_AVERAGE);
                double popularity = cursor.getDouble(MovieContract.MovieColumns.INDEX_COLUMN_POPULARITY);
                Uri existingUri = (mode == ORDER_MODE_MOST_POPULAR ? MovieContract.PopularMovieEntry.buildPopularMovieUri(id) : MovieContract.HighestRatedMovieEntry.buildTopRelatedMovieUri(id));
                if ((match.mOriginalTitle != null && !match.mOriginalTitle.equals(originalTitle)) ||
                        (match.mPosterPath != null && !match.mPosterPath.equals(poserPath)) ||
                        (match.mOverview != null && !match.mOverview.equals(overview)) ||
                        (match.mPopularity != popularity) ||
                        (match.mVoteAverage != voteAverage)) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovieContract.MovieColumns.COLUMN_ORIGINAL_TITLE, match.mOriginalTitle)
                            .withValue(MovieContract.MovieColumns.COLUMN_POSTER_PATH, match.mPosterPath)
                            .withValue(MovieContract.MovieColumns.COLUMN_OVERVIEW, match.mOverview)
                            .withValue(MovieContract.MovieColumns.COLUMN_VOTE_AVERAGE, match.mVoteAverage)
                            .withValue(MovieContract.MovieColumns.COLUMN_POPULARITY, match.mPopularity)
                            .withValue(MovieContract.MovieColumns.COLUMN_RELEASE_DATE, match.mReleaseDate)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }

            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = mode == ORDER_MODE_MOST_POPULAR ? MovieContract.PopularMovieEntry.buildPopularMovieUri(id) : MovieContract.HighestRatedMovieEntry.buildTopRelatedMovieUri(id);
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        cursor.close();

        for (Movie entry : entryMap.values()) {
            Log.i(TAG, "Scheduling insert: entry_id=" + entry.mId);
            batch.add(ContentProviderOperation.newInsert(contentUri)
                    .withValue(MovieContract.MovieColumns._ID, entry.mId)
                    .withValue(MovieContract.MovieColumns.COLUMN_ORIGINAL_TITLE, entry.mOriginalTitle)
                    .withValue(MovieContract.MovieColumns.COLUMN_POSTER_PATH, entry.mPosterPath)
                    .withValue(MovieContract.MovieColumns.COLUMN_OVERVIEW, entry.mOverview)
                    .withValue(MovieContract.MovieColumns.COLUMN_VOTE_AVERAGE, entry.mVoteAverage)
                    .withValue(MovieContract.MovieColumns.COLUMN_POPULARITY, entry.mPopularity)
                    .withValue(MovieContract.MovieColumns.COLUMN_RELEASE_DATE, entry.mReleaseDate)
                    .build());
            syncResult.stats.numInserts++;
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        if (batch.size() > 0) {
            mContentResolver.applyBatch(MovieContract.CONTENT_AUTHORITY, batch);
            mContentResolver.notifyChange(
                    contentUri, // URI where data was modified
                    null,                           // No local observer
                    false);                         // IMPORTANT: Do not sync to network
            // This sample doesn't support uploads, but if *your* code does, make sure you set
            // syncToNetwork=false in the line above to prevent duplicate syncs.
        }

    }

    private List<Movie> getPopularMovieDataFromJson(String jsonStr) throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULTS = "results";

        JSONObject movieJson = new JSONObject(jsonStr);
        JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);
        Movie[] movies = new Gson().fromJson(movieArray.toString(), Movie[].class);

        return Arrays.asList(movies);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
//        PopularSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
//        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}