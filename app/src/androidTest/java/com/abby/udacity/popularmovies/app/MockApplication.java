package com.abby.udacity.popularmovies.app;

import com.abby.udacity.popularmovies.app.di.DaggerMockApplicationComponent;
import com.abby.udacity.popularmovies.app.di.MockApplicationComponent;
import com.abby.udacity.popularmovies.app.di.module.ApplicationModule;

/**
 * Mock Application
 */
public class MockApplication extends MainApplication {
    public MockApplicationComponent getMockComponent() {
        return mMockComponent;
    }

    private MockApplicationComponent mMockComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mMockComponent = DaggerMockApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }
}
