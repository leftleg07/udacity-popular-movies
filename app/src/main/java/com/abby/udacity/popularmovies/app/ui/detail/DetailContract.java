package com.abby.udacity.popularmovies.app.ui.detail;

import com.abby.udacity.popularmovies.app.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface DetailContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter {
        void fetchTrailer(long movieId);
        void fetchReview(long movieId);
    }
}
