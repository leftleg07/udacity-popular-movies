package com.abby.udacity.popularmovies.app;

import android.app.Application;

import com.abby.udacity.popularmovies.app.di.component.ApplicationComponent;
import com.abby.udacity.popularmovies.app.di.component.DaggerApplicationComponent;
import com.abby.udacity.popularmovies.app.di.module.ApplicationModule;
import com.facebook.stetho.Stetho;

/**
 * Stetho intialize
 */
public class MainApplication extends Application {
    public ApplicationComponent getComponent() {
        return mComponent;
    }

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        mComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }
}
