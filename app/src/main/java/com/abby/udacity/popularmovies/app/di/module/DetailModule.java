package com.abby.udacity.popularmovies.app.di.module;

import com.abby.udacity.popularmovies.app.ui.detail.DetailContract;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * A module to wrap the Detail state and expose it to the graph.
 */
@Module
public class DetailModule {
    private final DetailContract.View mView;

    public DetailModule(DetailContract.View view) {
        this.mView = view;
    }

    @Provides
    @Singleton
    DetailContract.View provideView() {
        return mView;
    }
}
