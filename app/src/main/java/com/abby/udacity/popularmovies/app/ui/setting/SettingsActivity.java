package com.abby.udacity.popularmovies.app.ui.setting;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Show Setting for Movie Movie
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingFragment())
                    .commit();
        }
    }

}
