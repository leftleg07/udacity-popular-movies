package com.abby.udacity.popularmovies.app.di;


import com.abby.udacity.popularmovies.app.movie.PopularMovieContract;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * google dagger mock module
 */
@Module
public class MockModule {
    @Mock
    private PopularMovieContract.View mView;

    public MockModule() {
        MockitoAnnotations.initMocks(this);
    }

    @Provides
    @Singleton
    public PopularMovieContract.View providePopularMovieView() {
        return mView;
    }

}
