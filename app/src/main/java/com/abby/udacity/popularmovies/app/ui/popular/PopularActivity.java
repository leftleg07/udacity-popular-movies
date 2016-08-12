package com.abby.udacity.popularmovies.app.ui.popular;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.abby.udacity.popularmovies.app.MainApplication;
import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.di.component.PopularComponent;
import com.abby.udacity.popularmovies.app.di.module.PopularModule;
import com.abby.udacity.popularmovies.app.ui.favorite.FavoriteActivity;
import com.abby.udacity.popularmovies.app.ui.setting.SettingsActivity;

import javax.inject.Inject;

public class PopularActivity extends AppCompatActivity {

    @Inject
    PopularPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular);

        // Display the fragment as the main content.

        PopularFragment fragment =
                (PopularFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment == null) {
            // Create the fragment
            fragment = new PopularFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit();
        }
        PopularComponent component = ((MainApplication) getApplication()).getComponent().plus(new PopularModule(fragment));
        component.inject(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_favorite) {
            startActivity(new Intent(this, FavoriteActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
