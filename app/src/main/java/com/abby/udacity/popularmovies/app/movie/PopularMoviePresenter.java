package com.abby.udacity.popularmovies.app.movie;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

import com.abby.udacity.popularmovies.app.data.MovieContract;
import com.abby.udacity.popularmovies.app.network.TheMovieDBApiService;
import com.abby.udacity.popularmovies.app.sync.PopularMovieSyncAdapter;

import javax.inject.Inject;


/**
 * Listens to user actions from the UI ({@link PopularMovieFragment}), retrieves the data and updates
 * the UI as required.
 */
public class PopularMoviePresenter implements PopularMovieContract.Presenter {
    private static final String TAG = PopularMoviePresenter.class.getSimpleName();

    @Inject PopularMovieContract.View mView;
    @Inject Context mContext;
    @Inject TheMovieDBApiService mService;
    ContentResolver mContentResolver;


    @Inject
    public PopularMoviePresenter() {}

    @Inject
    public void setUpPresenter() {
        mView.setPresenter(this);
        mContentResolver = mContext.getContentResolver();
        mContentResolver.registerContentObserver(MovieContract.PopularMovieEntry.CONTENT_URI, false, new ContentObserver(null){
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                Cursor cursor = mContentResolver.query(uri, null, null, null, null);
                int count = cursor.getCount();
                mView.updateList(cursor);
            }
        });
        mContentResolver.registerContentObserver(MovieContract.TopRatedMovieEntry.CONTENT_URI, false, new ContentObserver(null){
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                Cursor cursor = mContentResolver.query(uri, null, null, null, null);
                mView.updateList(cursor);
            }
        });
    }


    @Override
    public void updatePopularMovie() {
        PopularMovieSyncAdapter.syncImmediately(mContext);
    }

    @Override
    public String getTrailers(String id) {
        String trailers = mService.getTrailers(id).toBlocking().single();


        return trailers;

    }

    @Override
    public String getReviews(String id) {
        String reviews = mService.getReviews(id).toBlocking().single();


        return reviews;
    }


}
