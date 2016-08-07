package com.abby.udacity.popularmovies.app.movie;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.detail.DetailActivity;
import com.abby.udacity.popularmovies.app.network.Movie;
import com.abby.udacity.popularmovies.app.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Encapsulates fetching movie posters and displaying it as a {@link GridView} layout.
 */
public class PopularMovieFragment extends Fragment implements PopularMovieContract.View {

    public static final String PARAM_KEY_MOVIE = "_param_key_movie";
    PopularMovieContract.Presenter mPresenter;
    private ArrayAdapter<Movie> mMovieAdapter;
    private PopularMovieCursorAdaptor mMovieCursorAdapter;

    @BindView(R.id.gridview_movie)
    GridView mGridView;

    public PopularMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mMovieAdapter = new PopularMovieAdapter(getActivity(), new ArrayList<Movie>());
        mMovieCursorAdapter = new PopularMovieCursorAdaptor(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_poster, container, false);
        ButterKnife.bind(this, rootView);

        // Get a reference to the GridView, and attach this adapter to it.
//        mGridView.setAdapter(mMovieAdapter);
        mGridView.setAdapter(mMovieCursorAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovieAdapter.getItem(position);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(PARAM_KEY_MOVIE, movie);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(PARAM_KEY_MOVIE, position);
//                intent.putExtras(mBundle);
                startActivity(intent);

            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_refresh) {
//            updateWeather();
//            return true;
//        }
        if(id == R.id.sort_most_popular) {
            item.setChecked(true);
            return true;
        } else if(id == R.id.sort_top_rated) {
            item.setChecked(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();
        updatePopularMovies();
    }


    @Override
    public void updateList(List<Movie> posters) {
        mMovieAdapter.clear();
        mMovieAdapter.addAll(posters);
        mMovieAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateList(final Cursor cursor) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMovieCursorAdapter.swapCursor(cursor);
            }
        });

    }

    private void updatePopularMovies() {
        if (!Util.isOnline(getContext())) {
            Toast.makeText(getContext(), R.string.message_no_network_connection, Toast.LENGTH_SHORT).show();
        }
        mPresenter.updatePopularMovie();
    }


    @Override
    public void setPresenter(PopularMovieContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
