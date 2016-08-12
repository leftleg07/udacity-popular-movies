package com.abby.udacity.popularmovies.app.ui.detail;

import android.net.Uri;

import com.abby.udacity.popularmovies.app.BaseView;
import com.abby.udacity.popularmovies.app.data.model.Movie;
import com.abby.udacity.popularmovies.app.data.model.Review;
import com.abby.udacity.popularmovies.app.data.model.Video;

import java.util.List;

/**
 * Created by gsshop on 2016. 8. 9..
 */
public interface DetailContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter {
        void fetchVideo(long movieId);
        void fetchReview(long movieId);
    }
}
