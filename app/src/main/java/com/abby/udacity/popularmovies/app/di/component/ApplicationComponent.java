package com.abby.udacity.popularmovies.app.di.component;

import com.abby.udacity.popularmovies.app.di.module.ApplicationModule;
import com.abby.udacity.popularmovies.app.di.module.DetailModule;
import com.abby.udacity.popularmovies.app.di.module.PopularModule;
import com.abby.udacity.popularmovies.app.data.sync.PopularSyncAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by gsshop on 2016. 8. 2..
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(PopularSyncAdapter popularMovieSyncAdapter);
    PopularComponent plus(PopularModule module);
    DetailComponent plus(DetailModule module);
}
