package com.abby.udacity.popularmovies.app.network;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.abby.udacity.popularmovies.app.MockApplication;
import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.data.network.TheMovieDBApiService;
import com.abby.udacity.popularmovies.app.di.MockModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static junit.framework.Assert.assertTrue;

/**
 * Created by gsshop on 2016. 8. 9..
 */
@RunWith(AndroidJUnit4.class)
public class TestAPIService {
    @Inject
    TheMovieDBApiService mApiService;

    @Inject
    Context mContext;

    @Before
    public void setUp() {
        MockApplication application = (MockApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        application.getMockComponent().plus(new MockModule()).inject(this);

    }

    @Test
    public void testMostPopularMovie() throws Exception {
        String order = mContext.getString(R.string.pref_mode_value_most_popular);
        String movie = mApiService.getPopularMovie(order).toBlocking().single();
        assertTrue(movie != null && movie.length() > 0);
    }

    @Test
    public void testToRatedMovie() throws Exception {
        String order = mContext.getString(R.string.pref_mode_value_highest_rated);
        String movie = mApiService.getPopularMovie(order).toBlocking().single();
        assertTrue(movie != null && movie.length() > 0);
    }

    @Test
    public void testVideo() throws Exception {
        int movieId = 209112;
        String video = mApiService.getTrailer(movieId).toBlocking().single();
        assertTrue(video != null && video.length() > 0);

    }

    @Test
    public void testReview() throws Exception {
        int movieId = 209112;
        String review = mApiService.getReview(movieId).toBlocking().single();
        assertTrue(review != null && review.length() > 0);

    }
}
