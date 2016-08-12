package com.abby.udacity.popularmovies.app.di;


import com.abby.udacity.popularmovies.app.ui.detail.DetailContract;
import com.abby.udacity.popularmovies.app.ui.popular.PopularContract;

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
    private PopularContract.View mPopularView;

    @Mock
    private DetailContract.View mDetailView;

    public MockModule() {
        MockitoAnnotations.initMocks(this);
    }

    @Provides
    @Singleton
    public PopularContract.View providePopularView() {
        return mPopularView;
    }

    @Provides
    @Singleton
    public DetailContract.View provideDetailView() {
        return mDetailView;
    }

}
