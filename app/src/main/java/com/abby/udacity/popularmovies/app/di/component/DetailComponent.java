package com.abby.udacity.popularmovies.app.di.component;

import com.abby.udacity.popularmovies.app.di.module.DetailModule;
import com.abby.udacity.popularmovies.app.ui.detail.DetailActivity;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 *  dagger's detail component
 */
@Singleton
@Subcomponent(modules = DetailModule.class)
public interface DetailComponent {
    void inject(DetailActivity detailActivity);
}
