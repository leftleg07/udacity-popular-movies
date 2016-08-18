package com.abby.udacity.popularmovies.app.di.component;

import com.abby.udacity.popularmovies.app.data.sync.PopularSyncAdapter;
import com.abby.udacity.popularmovies.app.di.module.ApplicationModule;
import com.abby.udacity.popularmovies.app.di.module.DetailModule;
import com.abby.udacity.popularmovies.app.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by gsshop on 2016. 8. 2..
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(PopularSyncAdapter popularMovieSyncAdapter);
    void inject(MainActivity popularActivity);
    DetailComponent plus(DetailModule module);

}
