package com.abby.udacity.popularmovies.app.di.component;

import com.abby.udacity.popularmovies.app.di.module.DetailModule;
import com.abby.udacity.popularmovies.app.ui.detail.DetailActivity;
import com.abby.udacity.popularmovies.app.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by gsshop on 2016. 8. 8..
 */
@Singleton
@Subcomponent(modules = DetailModule.class)
public interface DetailComponent {
    void inject(DetailActivity detailActivity);
    void inject(MainActivity mainActivity);
}
