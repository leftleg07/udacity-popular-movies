package com.abby.udacity.popularmovies.app.di;


import com.abby.udacity.popularmovies.app.ui.detail.DetailContract;

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
    private DetailContract.View mDetailView;

    public MockModule() {
        MockitoAnnotations.initMocks(this);
    }

    @Provides
    @Singleton
    public DetailContract.View provideDetailView() {
        return mDetailView;
    }

}
