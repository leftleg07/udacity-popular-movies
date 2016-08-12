package com.abby.udacity.popularmovies.app.ui.popular;

import com.abby.udacity.popularmovies.app.BaseView;

/**
 * Created by gsshop on 2016. 7. 8..
 */
public interface PopularContract {
    interface View extends BaseView<Presenter>{
    }
    interface Presenter {
        void fetchPopularMovie();
        String getVideo(int id);
        String getReview(int id);
    }


}
