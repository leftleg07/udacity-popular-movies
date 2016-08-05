package com.abby.udacity.popularmovies.app.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Created by heim on 7/30/16.
 */
@RunWith(AndroidJUnit4.class)
public class TestUriMatcher {
    private static final int MOVIE_ID = 209112;
    private static final String REVIEW_ID ="56f4f0bd9251417a440017bd";

    private static final Uri TEST_POPULAR_MOVIE_DIR = MovieContract.PopularMovieEntry.CONTENT_URI;
    private static final Uri TEST_POPULAR_MOVIE_ID_ITEM = MovieContract.PopularMovieEntry.buildPopularMovieUri(MOVIE_ID);
    private static final Uri TEST_TOP_RELATED_MOVIE_DIR = MovieContract.TopRatedMovieEntry.CONTENT_URI;
    private static final Uri TEST_TOP_RELATED_MOVIE_ID_ITEM = MovieContract.TopRatedMovieEntry.buildTopRelatedMovieUri(MOVIE_ID);
    private static final Uri TEST_FAVORITE_MOVIE_DIR = MovieContract.FavoriteMovieEntry.CONTENT_URI;
    private static final Uri TEST_FAVORITE_MOVIE_ID_ITEM = MovieContract.FavoriteMovieEntry.buildFavoriteMovieUri(MOVIE_ID);
    private static final Uri TEST_REVIEW_DIR = MovieContract.ReviewEntry.CONTENT_URI;
    private static final Uri TEST_REVIEW_ID_ITEM = MovieContract.ReviewEntry.buildReviewUri(REVIEW_ID);
    private static final Uri TEST_REVIEW_WITH_MOVIE_ITEM = MovieContract.ReviewEntry.buildReviewMovieUri(MOVIE_ID);


    @Test
    public void testUriMatcher() throws Exception {
        UriMatcher testMatcher = MovieProvider.buildUriMatcher();

        assertEquals("Error: The POPULAR MOVIE URI was matched incorrectly.", testMatcher.match(TEST_POPULAR_MOVIE_DIR), MovieProvider.POPULAR_MOVIE);
        assertEquals("Error: The POPULAR MOVIE ID URI was matched incorrectly.", testMatcher.match(TEST_POPULAR_MOVIE_ID_ITEM), MovieProvider.POPULAR_MOVIE_WITH_ID);
        assertEquals("Error: The TOP RELATED MOVIE URI was matched incorrectly.", testMatcher.match(TEST_TOP_RELATED_MOVIE_DIR), MovieProvider.TOP_RELATED_MOVIE);
        assertEquals("Error: The TOP RELATED MOVIE ID URI was matched incorrectly.", testMatcher.match(TEST_TOP_RELATED_MOVIE_ID_ITEM), MovieProvider.TOP_RELATED_MOVIE_WITH_ID);
        assertEquals("Error: The FAVORITE MOVIE URI was matched incorrectly.", testMatcher.match(TEST_FAVORITE_MOVIE_DIR), MovieProvider.FAVORITE_MOVIE);
        assertEquals("Error: The FAVORITE MOVIE ID URI was matched incorrectly.", testMatcher.match(TEST_FAVORITE_MOVIE_ID_ITEM), MovieProvider.FAVORITE_MOVIE_WITH_ID);
        assertEquals("Error: The REVIEW URI was matched incorrectly.", testMatcher.match(TEST_REVIEW_DIR), MovieProvider.REVIEW);
        assertEquals("Error: The REVIEW ID URI was matched incorrectly.", testMatcher.match(TEST_REVIEW_ID_ITEM), MovieProvider.REVIEW_WITH_REVIEW_ID);
        assertEquals("Error: The REVIEW WITH MOVIE URI was matched incorrectly.", testMatcher.match(TEST_REVIEW_WITH_MOVIE_ITEM), MovieProvider.REVIEW_WITH_MOVIE_ID);

    }
}
