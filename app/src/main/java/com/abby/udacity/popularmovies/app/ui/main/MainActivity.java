package com.abby.udacity.popularmovies.app.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abby.udacity.popularmovies.app.MainApplication;
import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.data.network.TheMovieDBApiService;
import com.abby.udacity.popularmovies.app.data.sync.PopularSyncAdapter;
import com.abby.udacity.popularmovies.app.ui.detail.DetailActivity;
import com.abby.udacity.popularmovies.app.ui.detail.DetailFragment;
import com.abby.udacity.popularmovies.app.ui.detail.DetailPresenter;
import com.abby.udacity.popularmovies.app.util.Util;

import javax.inject.Inject;

import static com.abby.udacity.popularmovies.app.ui.main.PopularFragment.FAVORITE_LOADER;
import static com.abby.udacity.popularmovies.app.ui.main.PopularFragment.HIGHEST_RATED_LOADER;
import static com.abby.udacity.popularmovies.app.ui.main.PopularFragment.MOST_POPULAR_LOADER;

public class MainActivity extends AppCompatActivity implements PopularFragment.Callback {

    private static final String DETAIL_FRAGMENT_TAG = DetailFragment.class.getSimpleName();

    @Inject
    SharedPreferences mPrefs;

    @Inject
    Context mContext;

    @Inject
    TheMovieDBApiService mApiService;

    private boolean mTwoPane;
    private DetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }

        // Display the fragment as the main content.
        ((MainApplication) getApplication()).getComponent().inject(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        String mode = mPrefs.getString(getString(R.string.pref_mode_key), getString(R.string.pref_mode_value_most_popular));
        int loaderId = -1;
        if (mode.equals(getString(R.string.pref_mode_value_most_popular))) {
            menu.findItem(R.id.action_most_popular).setChecked(true);
            loaderId = MOST_POPULAR_LOADER;
            fetchPopularMovie();
        } else if (mode.equals(getString(R.string.pref_mode_value_highest_rated))) {
            menu.findItem(R.id.action_highest_rated).setChecked(true);
            loaderId = HIGHEST_RATED_LOADER;
            fetchPopularMovie();
        } else if (mode.equals(getString(R.string.pref_mode_value_favorite))) {
            menu.findItem(R.id.action_favorite).setChecked(true);
            loaderId = FAVORITE_LOADER;
        }

        initLoader(loaderId);
        setPopularMovieTitle(loaderId);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        int loaderId = -1;
        item.setChecked(true);
        if (id == R.id.action_most_popular) {
            loaderId = MOST_POPULAR_LOADER;
            mPrefs.edit().putString(getString(R.string.pref_mode_key), getString(R.string.pref_mode_value_most_popular)).commit();
            fetchPopularMovie();
        } else if (id == R.id.action_highest_rated) {
            loaderId = HIGHEST_RATED_LOADER;
            mPrefs.edit().putString(getString(R.string.pref_mode_key), getString(R.string.pref_mode_value_highest_rated)).commit();
            fetchPopularMovie();
        } else if (id == R.id.action_favorite) {
            loaderId = FAVORITE_LOADER;
            mPrefs.edit().putString(getString(R.string.pref_mode_key), getString(R.string.pref_mode_value_favorite)).commit();
        }

        initLoader(loaderId);
        setPopularMovieTitle(loaderId);

        return super.onOptionsItemSelected(item);
    }

    public void initLoader(int id) {
        PopularFragment fragment =
                (PopularFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_popular);
        if (fragment != null) {
            if (mTwoPane) {
                fragment.getLoaderManager().restartLoader(id, null, fragment);
                ((ViewGroup)findViewById(R.id.detail_container)).removeAllViews();
            } else {
                fragment.getLoaderManager().initLoader(id, null, fragment);
            }
        }
    }

    public void fetchPopularMovie() {
        if (!Util.isOnline(this)) {
            Toast.makeText(this, R.string.message_no_network_connection, Toast.LENGTH_SHORT).show();
        }
        PopularSyncAdapter.syncImmediately(this);
    }

    public void setPopularMovieTitle(int id) {
        String title = getString(R.string.app_name);
        if (id == MOST_POPULAR_LOADER) {
            title += " - " + getString(R.string.action_most_popular);
        } else if (id == HIGHEST_RATED_LOADER) {
            title += " - " + getString(R.string.action_highest_rated);
        } else if (id == FAVORITE_LOADER) {
            title += " - " + getString(R.string.action_favorite);
        }
        setTitle(title);

    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.ARG_PARAM_MOVIE, contentUri);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment, DETAIL_FRAGMENT_TAG)
                    .commit();

            mPresenter = new DetailPresenter(mContext, fragment, mApiService);
            mPresenter.setUpPresenter();


        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }
    }
}
