package com.abby.udacity.popularmovies.app.di;

import javax.inject.Singleton;

import dagger.Component;

/**
 * google dagger component
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface MockApplicationComponent {
    MockComponent plus(MockModule module);
}
