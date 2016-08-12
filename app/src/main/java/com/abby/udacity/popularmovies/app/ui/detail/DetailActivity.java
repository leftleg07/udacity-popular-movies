package com.abby.udacity.popularmovies.app.ui.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.abby.udacity.popularmovies.app.MainApplication;
import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.di.component.DetailComponent;
import com.abby.udacity.popularmovies.app.di.module.DetailModule;
import com.abby.udacity.popularmovies.app.ui.popular.PopularFragment;

import javax.inject.Inject;

/**
 * Displays movie details screen.
 */
public class DetailActivity extends AppCompatActivity {

    @Inject
    DetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String uri = getIntent().getStringExtra(PopularFragment.PARAM_MOVIE_URI);

        // Display the fragment as the main content.

        DetailFragment fragment =
                (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment == null) {
            // Create the fragment
            fragment = DetailFragment.newInstance(uri);
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit();
        }
        DetailComponent component = ((MainApplication) getApplication()).getComponent().plus(new DetailModule(fragment));
        component.inject(this);

    }
}