package com.abby.udacity.popularmovies.app.ui.popular;

import android.content.Context;

import com.abby.udacity.popularmovies.app.data.network.TheMovieDBApiService;
import com.abby.udacity.popularmovies.app.data.sync.PopularSyncAdapter;

import javax.inject.Inject;


/**
 * Listens to user actions from the UI ({@link PopularFragment}), retrieves the data and updates
 * the UI as required.
 */
public class PopularPresenter implements PopularContract.Presenter {
    private static final String TAG = PopularPresenter.class.getSimpleName();

    @Inject PopularContract.View mView;
    @Inject Context mContext;
    @Inject TheMovieDBApiService mService;

    @Inject
    public PopularPresenter() {}

    @Inject
    public void setUpPresenter() {
        mView.setPresenter(this);
    }

    @Override
    public void fetchPopularMovie() {
        PopularSyncAdapter.syncImmediately(mContext);
    }

    @Override
    public String getVideo(int id) {
        String trailers = mService.getVideo(id).toBlocking().single();

        return trailers;
    }

    @Override
    public String getReview(int id) {
        String reviews = mService.getReview(id).toBlocking().single();


        return reviews;
    }


}
