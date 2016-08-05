package com.abby.udacity.popularmovies.app.movie;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.detail.DetailActivity;
import com.abby.udacity.popularmovies.app.network.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Encapsulates fetching movie posters and displaying it as a {@link GridView} layout.
 */
public class PopularMovieFragment extends Fragment implements PopularMovieContract.View {


    public static final String PARAM_KEY_MOVIE = "_param_key_movie";
    private ArrayAdapter<Movie> mMovieAdapter;
    private PopularMovieContract.Presenter mPresenter;
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
//        mPresenter = new PopularMoviePresenter(this, getActivity());
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
    public void updateList(Cursor cursor) {
        mMovieCursorAdapter.swapCursor(cursor);
    }

    private void updatePopularMovies() {
        if (!isOnline()) {
            Toast.makeText(getContext(), R.string.message_no_network_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.updatePopularMovie();
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public void setPresenter(PopularMovieContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
