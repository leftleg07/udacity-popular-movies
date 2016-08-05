package com.abby.udacity.popularmovies.app.di;

import com.abby.udacity.popularmovies.app.sync.PopularMovieSyncAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by gsshop on 2016. 8. 2..
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(PopularMovieSyncAdapter popularMovieSyncAdapter);
}
