package com.abby.udacity.popularmovies.app;

import com.abby.udacity.popularmovies.app.di.ApplicationModule;
import com.abby.udacity.popularmovies.app.di.DaggerMockApplicationComponent;
import com.abby.udacity.popularmovies.app.di.MockApplicationComponent;

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
