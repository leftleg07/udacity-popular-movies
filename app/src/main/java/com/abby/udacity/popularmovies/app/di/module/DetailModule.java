package com.abby.udacity.popularmovies.app.di.module;

import com.abby.udacity.popularmovies.app.ui.detail.DetailContract;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gsshop on 2016. 8. 8..
 */
@Module
public class DetailModule {
    private final DetailContract.View mView;

    public DetailModule(DetailContract.View view) {
        this.mView = view;
    }

    @Provides @Singleton DetailContract.View provideView() {return  mView;}
}
