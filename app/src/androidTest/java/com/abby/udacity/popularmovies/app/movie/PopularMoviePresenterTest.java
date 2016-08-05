package com.abby.udacity.popularmovies.app.movie;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.abby.udacity.popularmovies.app.MockApplication;
import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.di.MockModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Unit tests for the implementation of {@link PopularMoviePresenter}
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PopularMoviePresenterTest {

    @Inject
    Context context;

    @Inject
    PopularMovieContract.View mView;

    @Inject
    PopularMoviePresenter mPresenter;

    @Inject
    SharedPreferences mPrefs;

    @Before
    public void setUp() {
        MockApplication application = (MockApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        application.getMockComponent().plus(new MockModule()).inject(this);

    }

    @Test
    public void testPopularMovies() throws InterruptedException {
        // fail("implements!");

        String value = context.getString(R.string.pref_order_value_most_popular);
        String key = context.getString(R.string.pref_order_key);
        mPrefs.edit().putString(key, value).commit();


        mPresenter.updatePopularMovie();

        while(true) {
            TimeUnit.MILLISECONDS.sleep(400);
        }
//        verify(mView).updateList(anyList());
    }

    /*
    @Test
    public void testMostRatedMovies() throws InterruptedException {
        // fail("implements!");
        mPresenter = new PopularMoviePresenter(mView, mContext);
        String order = mContext.getString(R.string.pref_order_value_top_rated);
        mPresenter.updateMoviePosters(order);

        verify(mView).updateList(anyList());
    }

    @Test
    public void testTrailers() throws Exception {
        mPresenter = new PopularMoviePresenter(mView, mContext);
        String id = "209112";

        String trailers = mPresenter.getTrailers(id);

        Truth.assertThat(trailers).isNotEmpty();

    }

    @Test
    public void testReviews() throws Exception {
        mPresenter = new PopularMoviePresenter(mView, mContext);
        String id = "209112";

        String reviews = mPresenter.getReviews(id);

        Truth.assertThat(reviews).isNotEmpty();

    }
    */
}