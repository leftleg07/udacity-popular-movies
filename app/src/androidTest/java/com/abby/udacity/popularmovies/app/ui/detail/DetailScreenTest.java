package com.abby.udacity.popularmovies.app.ui.detail;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.data.db.MovieContract;
import com.abby.udacity.popularmovies.app.ui.main.PopularFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Tests for the detail movie screen, the main screen which contains a grid of all notes.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DetailScreenTest {

    private static final long MOVIE_ID = 76341;


    /**
     * {@link IntentsTestRule} is an {@link ActivityTestRule} which inits and releases Espresso
     * Intents before and after each test run.
     *
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public IntentsTestRule<DetailActivity> mActivityRule =
            new IntentsTestRule<>(DetailActivity.class, false, false);


    @Test
    public void testDetail() throws Exception {
        Intent intent = new Intent();
        String uriString= MovieContract.PopularMovieEntry.buildPopularMovieUri(MOVIE_ID).toString();
        intent.putExtra(PopularFragment.PARAM_MOVIE_URI, uriString);
        mActivityRule.launchActivity(intent);

        // wait for activity finished
        while(!mActivityRule.getActivity().isFinishing()) {
            TimeUnit.SECONDS.sleep(1);
        }

    }

    @Test
    public void testTrailer() throws Exception {
        Intent intent = new Intent();
        String uriString= MovieContract.PopularMovieEntry.buildPopularMovieUri(MOVIE_ID).toString();
        intent.putExtra(PopularFragment.PARAM_MOVIE_URI, uriString);
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.button_detail_trailer)).perform(click());

        // wait for activity finished
        while(!mActivityRule.getActivity().isFinishing()) {
            TimeUnit.SECONDS.sleep(1);
        }

    }


    @Test
    public void testReview() throws Exception {
        Intent intent = new Intent();
        String uriString= MovieContract.PopularMovieEntry.buildPopularMovieUri(MOVIE_ID).toString();
        intent.putExtra(PopularFragment.PARAM_MOVIE_URI, uriString);
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.button_detail_review)).perform(click());

        // wait for activity finished
        while(!mActivityRule.getActivity().isFinishing()) {
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
