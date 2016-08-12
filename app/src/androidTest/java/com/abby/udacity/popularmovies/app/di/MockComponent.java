package com.abby.udacity.popularmovies.app.di;


import com.abby.udacity.popularmovies.app.network.TestAPIService;
import com.abby.udacity.popularmovies.app.ui.detail.DetailPresenterTest;
import com.abby.udacity.popularmovies.app.ui.popular.PopularPresenterTest;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * google dagger's sub component
 */
@Singleton
@Subcomponent(modules = MockModule.class)
public interface MockComponent {
    void inject(PopularPresenterTest popularMoviePresenterTest);
    void inject(DetailPresenterTest detailPresenterTest);
    void inject(TestAPIService testService);

}
