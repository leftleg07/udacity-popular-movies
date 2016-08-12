package com.abby.udacity.popularmovies.app.ui.popular;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.abby.udacity.popularmovies.app.MockApplication;
import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.data.db.MovieContract;
import com.abby.udacity.popularmovies.app.di.MockModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import static junit.framework.Assert.assertTrue;

/**
 * Unit tests for the implementation of {@link PopularPresenter}
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PopularPresenterTest {

    @Inject
    Context context;

    @Inject
    PopularPresenter mPresenter;

    @Inject
    SharedPreferences mPrefs;

    private ContentResolver mContentResolver;

    CountDownLatch signal = null;
    private Cursor cursor;

    @Before
    public void setUp() {
        MockApplication application = (MockApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        application.getMockComponent().plus(new MockModule()).inject(this);
        mContentResolver = context.getContentResolver();
        signal = new CountDownLatch(1);

    }

    @Test
    public void testPopularMovies() throws InterruptedException {
        // fail("implements!");

        String value = context.getString(R.string.pref_order_value_most_popular);
        String key = context.getString(R.string.pref_order_key);
        mPrefs.edit().putString(key, value).commit();

        Uri uri = MovieContract.PopularMovieEntry.CONTENT_URI;
        mContentResolver.delete(uri, null, null);
        mContentResolver.registerContentObserver(uri, false, new ContentObserver(null){
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                signal.countDown();
                cursor = mContentResolver.query(uri, null, null, null, null);
            }
        });

        mPresenter.fetchPopularMovie();

        signal.await();

        assertTrue(cursor.getCount() > 0);
    }

    @Test
    public void testTopRatedMovies() throws InterruptedException {
        // fail("implements!");

        String value = context.getString(R.string.pref_order_value_top_rated);
        String key = context.getString(R.string.pref_order_key);
        mPrefs.edit().putString(key, value).commit();


        Uri uri = MovieContract.TopRatedMovieEntry.CONTENT_URI;
        mContentResolver.delete(uri, null, null);
        mContentResolver.registerContentObserver(uri, false, new ContentObserver(null){
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                signal.countDown();
                cursor = mContentResolver.query(uri, null, null, null, null);
            }
        });

        mPresenter.fetchPopularMovie();
        signal.await();

        assertTrue(cursor.getCount() > 0);
    }


    /*
    @Test
    public void testMostRatedMovies() throws InterruptedException {
        // fail("implements!");
        mPresenter = new PopularPresenter(mView, mContext);
        String order = mContext.getString(R.string.pref_order_value_top_rated);
        mPresenter.updateMoviePosters(order);

        verify(mView).updateList(anyList());
    }

    @Test
    public void testTrailers() throws Exception {
        mPresenter = new PopularPresenter(mView, mContext);
        String id = "209112";

        String trailers = mPresenter.getVideo(id);

        Truth.assertThat(trailers).isNotEmpty();

    }

    @Test
    public void testReviews() throws Exception {
        mPresenter = new PopularPresenter(mView, mContext);
        String id = "209112";

        String reviews = mPresenter.getReview(id);

        Truth.assertThat(reviews).isNotEmpty();

    }
    */
}