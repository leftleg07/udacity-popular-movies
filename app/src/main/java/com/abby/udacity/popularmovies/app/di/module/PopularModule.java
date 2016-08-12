package com.abby.udacity.popularmovies.app.di.module;

import com.abby.udacity.popularmovies.app.ui.popular.PopularContract;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gsshop on 2016. 8. 8..
 */
@Module
public class PopularModule {
    private final PopularContract.View mView;

    public PopularModule(PopularContract.View view) {
        this.mView = view;
    }

    @Provides @Singleton PopularContract.View provideView() {return  mView;}
}
