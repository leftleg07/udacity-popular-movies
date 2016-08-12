package com.abby.udacity.popularmovies.app.di.component;

import com.abby.udacity.popularmovies.app.di.module.PopularModule;
import com.abby.udacity.popularmovies.app.ui.popular.PopularActivity;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by gsshop on 2016. 8. 8..
 */
@Singleton
@Subcomponent(modules = PopularModule.class)
public interface PopularComponent {
    void inject(PopularActivity popularMovieActivity);
}
