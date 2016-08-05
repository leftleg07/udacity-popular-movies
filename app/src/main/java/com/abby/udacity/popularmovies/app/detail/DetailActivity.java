package com.abby.udacity.popularmovies.app.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.movie.PopularMovieFragment;
import com.abby.udacity.popularmovies.app.network.Movie;

/**
 * Displays movie details screen.
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        Movie movie = extras.getParcelable(PopularMovieFragment.PARAM_KEY_MOVIE);

        // Display the fragment as the main content.
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, DetailFragment.newInstance(movie))
                    .commit();
        }
    }
}
