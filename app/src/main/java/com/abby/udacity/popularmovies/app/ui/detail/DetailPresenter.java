package com.abby.udacity.popularmovies.app.ui.detail;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.abby.udacity.popularmovies.app.data.db.MovieContract;
import com.abby.udacity.popularmovies.app.data.model.Review;
import com.abby.udacity.popularmovies.app.data.model.Video;
import com.abby.udacity.popularmovies.app.data.network.TheMovieDBApiService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import rx.schedulers.Schedulers;

/**
 * Created by gsshop on 2016. 8. 9..
 */
public class DetailPresenter implements DetailContract.Presenter {
    private static final String TAG = DetailPresenter.class.getSimpleName();

    private final String OWM_RESULTS = "results";

    private final DetailContract.View mView;

    private final Context mContext;

    private final TheMovieDBApiService mApiService;

    private ContentResolver mContentResolver;


    @Inject
    public DetailPresenter(Context context, DetailContract.View view, TheMovieDBApiService apiService) {
        this.mContext = context;
        this.mView = view;
        this.mApiService = apiService;
        mContentResolver = mContext.getContentResolver();
    }

    @Inject
    public void setUpPresenter() {
        mView.setPresenter(this);

    }


    @Override
    public void fetchVideo(long movieId) {
        try {
            String jsonStr = mApiService.getVideo(movieId).subscribeOn(Schedulers.newThread()).toBlocking().single();
            // These are the names of the JSON objects that need to be extracted.

            JSONObject videoJson = new JSONObject(jsonStr);
            JSONArray videoArray = videoJson.getJSONArray(OWM_RESULTS);
            Video[] videos = new Gson().fromJson(videoArray.toString(), Video[].class);
            updateLocalVideoData(videos, movieId);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            return;
        } catch (RemoteException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            return;
        } catch (OperationApplicationException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            return;

        }


    }


    private void updateLocalVideoData(Video[] entries, long movieId) throws RemoteException, OperationApplicationException {
        final ArrayList<ContentProviderOperation> batch = new ArrayList<>();

// Build hash table of incoming entries
        final HashMap<String, Video> entryMap = new HashMap<>();
        for (Video e : entries) {
            entryMap.put(e.mVideoId, e);
        }

        Uri contentUri = MovieContract.VideoEntry.buildVideoMovieUri(movieId);
        Cursor cursor = mContentResolver.query(contentUri, MovieContract.VideoEntry.PROJECTION, null, null, null);
        Preconditions.checkNotNull(cursor);

        Log.i(TAG, "Found " + cursor.getCount() + " local entries. Computing merge solution...");

        while (cursor.moveToNext()) {
            String videoId = cursor.getString(MovieContract.VideoEntry.INDEX_COLUMN_VIDEO_ID);
            Video match = entryMap.get(videoId);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(videoId);
                // Check to see if the entry needs to be updated
                long mId = cursor.getLong(MovieContract.VideoEntry.INDEX_COLUMN_MOVIE_ID);
                String key = cursor.getString(MovieContract.VideoEntry.INDEX_COLUMN_KEY);
                String name = cursor.getString(MovieContract.VideoEntry.INDEX_COLUMN_NAME);
                long size = cursor.getLong(MovieContract.VideoEntry.INDEX_COLUMN_SIZE);
                Uri existingUri = MovieContract.VideoEntry.buildVideoUri(videoId);
                if ((mId != movieId) ||
                        (match.mKey != null && !match.mKey.equals(key)) ||
                        (match.mName != null && !match.mName.equals(name)) ||
                        (match.mSize != size)) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovieContract.VideoEntry.COLUMN_VIDEO_ID, match.mVideoId)
                            .withValue(MovieContract.VideoEntry.COLUMN_MOVIE_ID, movieId)
                            .withValue(MovieContract.VideoEntry.COLUMN_KEY, match.mKey)
                            .withValue(MovieContract.VideoEntry.COLUMN_NAME, match.mName)
                            .withValue(MovieContract.VideoEntry.COLUMN_SIZE, match.mSize)
                            .withValue(MovieContract.VideoEntry.COLUMN_TYPE, match.mType)
                            .build());
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }

            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = MovieContract.VideoEntry.buildVideoUri(videoId);
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
            }
        }
        cursor.close();

        for (Video entry : entryMap.values()) {
            Log.i(TAG, "Scheduling insert: entry_id=" + entry.mVideoId);
            batch.add(ContentProviderOperation.newInsert(MovieContract.VideoEntry.CONTENT_URI)
                    .withValue(MovieContract.VideoEntry.COLUMN_VIDEO_ID, entry.mVideoId)
                    .withValue(MovieContract.VideoEntry.COLUMN_MOVIE_ID, movieId)
                    .withValue(MovieContract.VideoEntry.COLUMN_KEY, entry.mKey)
                    .withValue(MovieContract.VideoEntry.COLUMN_NAME, entry.mName)
                    .withValue(MovieContract.VideoEntry.COLUMN_SIZE, entry.mSize)
                    .withValue(MovieContract.VideoEntry.COLUMN_TYPE, entry.mType)
                    .build());
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

    @Override
    public void fetchReview(long movieId) {
        try {
            String jsonStr = mApiService.getReview(movieId).subscribeOn(Schedulers.newThread()).toBlocking().single();
            // These are the names of the JSON objects that need to be extracted.


            JSONObject reviewJson = new JSONObject(jsonStr);
            JSONArray reviewArray = reviewJson.getJSONArray(OWM_RESULTS);
            Review[] reviews = new Gson().fromJson(reviewArray.toString(), Review[].class);
            updateLocalReviewData(reviews, movieId);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            return;
        } catch (RemoteException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            return;
        } catch (OperationApplicationException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            return;

        }

    }

    private void updateLocalReviewData(Review[] entries, long movieId) throws RemoteException, OperationApplicationException {
        final ArrayList<ContentProviderOperation> batch = new ArrayList<>();

// Build hash table of incoming entries
        final HashMap<String, Review> entryMap = new HashMap<>();
        for (Review e : entries) {
            entryMap.put(e.mReviewId, e);
        }

        Uri contentUri = MovieContract.ReviewEntry.buildReviewMovieUri(movieId);
        Cursor cursor = mContentResolver.query(contentUri, MovieContract.ReviewEntry.PROJECTION, null, null, null);
        Preconditions.checkNotNull(cursor);

        Log.i(TAG, "Found " + cursor.getCount() + " local entries. Computing merge solution...");

        while (cursor.moveToNext()) {
            String reviewId = cursor.getString(MovieContract.ReviewEntry.INDEX_COLUMN_REVIEW_ID);
            Review match = entryMap.get(reviewId);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(reviewId);
                // Check to see if the entry needs to be updated
                long mId = cursor.getLong(MovieContract.ReviewEntry.INDEX_COLUMN_MOVIE_ID);
                String author = cursor.getString(MovieContract.ReviewEntry.INDEX_COLUMN_AUTHOR);
                String content = cursor.getString(MovieContract.ReviewEntry.INDEX_COLUMN_CONTENT);
                String url = cursor.getString(MovieContract.ReviewEntry.INDEX_COLUMN_URL);
                Uri existingUri = MovieContract.ReviewEntry.buildReviewUri(reviewId);
                if ((mId != movieId) ||
                        (match.mAuthor != null && !match.mAuthor.equals(author)) ||
                        (match.mContent != null && !match.mContent.equals(content)) ||
                        (match.mUrl != null && !match.mUrl.equals(url))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, match.mReviewId)
                            .withValue(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, movieId)
                            .withValue(MovieContract.ReviewEntry.COLUMN_AUTHOR, match.mAuthor)
                            .withValue(MovieContract.ReviewEntry.COLUMN_CONTENT, match.mContent)
                            .withValue(MovieContract.ReviewEntry.COLUMN_URL, match.mUrl)
                            .build());
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }

            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = MovieContract.ReviewEntry.buildReviewUri(reviewId);
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
            }
        }
        cursor.close();

        for (Review entry : entryMap.values()) {
            Log.i(TAG, "Scheduling insert: entry_id=" + entry.mReviewId);
            batch.add(ContentProviderOperation.newInsert(MovieContract.ReviewEntry.CONTENT_URI)
                    .withValue(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, entry.mReviewId)
                    .withValue(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, movieId)
                    .withValue(MovieContract.ReviewEntry.COLUMN_AUTHOR, entry.mAuthor)
                    .withValue(MovieContract.ReviewEntry.COLUMN_CONTENT, entry.mContent)
                    .withValue(MovieContract.ReviewEntry.COLUMN_URL, entry.mUrl)
                    .build());
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
}
