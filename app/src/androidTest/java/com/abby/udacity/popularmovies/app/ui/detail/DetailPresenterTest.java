package com.abby.udacity.popularmovies.app.ui.detail;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.abby.udacity.popularmovies.app.MockApplication;
import com.abby.udacity.popularmovies.app.data.db.MovieContract;
import com.abby.udacity.popularmovies.app.di.MockModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import static junit.framework.Assert.assertTrue;


/**
 * Unit tests for the implementation of {@link DetailPresenter}
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DetailPresenterTest {

    private static final long MOVIE_ID = 47933;

    @Inject
    Context mContext;

    @Inject
    DetailPresenter mPresenter;

    @Inject
    DetailContract.View mView;

    private CountDownLatch mSignal;
    private ContentResolver mContentResolver;
    private Cursor mCursor;

    @Before
    public void setUp() {
        MockApplication application = (MockApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        application.getMockComponent().plus(new MockModule()).inject(this);
        mContentResolver = mContext.getContentResolver();
        mSignal = new CountDownLatch(1);

    }


    @Test
    public void testVideo() throws Exception {

        Uri uri = MovieContract.TrailerEntry.buildVideoMovieUri(MOVIE_ID);
        mContentResolver.delete(uri, null, null);
        mContentResolver.registerContentObserver(uri, false, new ContentObserver(null) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                mCursor = mContentResolver.query(uri, null, null, null, null);

                mSignal.countDown();
            }
        });

        mPresenter.fetchTrailer(MOVIE_ID);

        mSignal.await();
        assertTrue(mCursor.getCount() > 0);
    }

    @Test
    public void testReview() throws Exception {

        Uri uri = MovieContract.ReviewEntry.buildReviewMovieUri(MOVIE_ID);
        mContentResolver.delete(uri, null, null);
        mContentResolver.registerContentObserver(uri, false, new ContentObserver(null) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                mCursor = mContentResolver.query(uri, null, null, null, null);

                mSignal.countDown();
            }
        });

        mPresenter.fetchReview(MOVIE_ID);

        mSignal.await();
        assertTrue(mCursor.getCount() > 0);

    }
}