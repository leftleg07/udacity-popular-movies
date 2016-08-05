package com.abby.udacity.popularmovies.app.di;


import com.abby.udacity.popularmovies.app.movie.PopularMoviePresenterTest;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * google dagger's sub component
 */
@Singleton
@Subcomponent(modules = MockModule.class)
public interface MockComponent {
    void inject(PopularMoviePresenterTest popularMoviePresenterTest);
}
