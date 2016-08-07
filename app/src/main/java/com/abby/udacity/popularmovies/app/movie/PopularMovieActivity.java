package com.abby.udacity.popularmovies.app.movie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.abby.udacity.popularmovies.app.MainApplication;
import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.di.PopularMovieComponent;
import com.abby.udacity.popularmovies.app.di.PopularMovieModule;

import javax.inject.Inject;

public class PopularMovieActivity extends AppCompatActivity {

    @Inject PopularMoviePresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        // Display the fragment as the main content.
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new PopularMovieFragment())
                    .commit();
        }

        PopularMovieFragment fragment =
                (PopularMovieFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment == null) {
            // Create the fragment
            fragment = new PopularMovieFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit();
            PopularMovieComponent component = ((MainApplication) getApplication()).getComponent().plus(new PopularMovieModule(fragment));
            component.inject(this);
        }

    }


}
