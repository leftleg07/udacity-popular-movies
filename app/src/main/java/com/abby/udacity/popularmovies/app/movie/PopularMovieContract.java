package com.abby.udacity.popularmovies.app.movie;

import android.database.Cursor;

import com.abby.udacity.popularmovies.app.BaseView;
import com.abby.udacity.popularmovies.app.network.Movie;

import java.util.List;

/**
 * Created by gsshop on 2016. 7. 8..
 */
public interface PopularMovieContract {
    interface View extends BaseView<Presenter>{
        void updateList(List<Movie> movies);
        void updateList(Cursor cursor);
    }
    interface Presenter {
        void updatePopularMovie();
        String getTrailers(String id);
        String getReviews(String id);
    }


}
