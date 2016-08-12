package com.abby.udacity.popularmovies.app.ui.favorite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.abby.udacity.popularmovies.app.R;

/**
 * Displays movie details screen.
 */
public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // Display the fragment as the main content.
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new FavoriteFragment())
                    .commit();
        }
    }
}
