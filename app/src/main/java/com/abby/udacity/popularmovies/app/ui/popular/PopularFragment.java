package com.abby.udacity.popularmovies.app.ui.popular;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.data.db.MovieContract;
import com.abby.udacity.popularmovies.app.ui.detail.DetailActivity;
import com.abby.udacity.popularmovies.app.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Encapsulates fetching movie posters and displaying it as a {@link GridView} layout.
 */
public class PopularFragment extends Fragment implements PopularContract.View, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String PARAM_MOVIE_URI = "_param_movie_uri";
    private static final int POPULAR_MOVIE_LOADER = 0;
    private static final int TOP_RATED_LOADER = 1;

    private PopularContract.Presenter mPresenter;
    private PopularAdaptor mAdapter;

    @BindView(R.id.listView_movie)
    GridView mListView;

    private int mPosition = ListView.INVALID_POSITION;
    private int mLoaderId = POPULAR_MOVIE_LOADER;

    public PopularFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mAdapter = new PopularAdaptor(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // Get a reference to the GridView, and attach this adapter to it.
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    int movieId = cursor.getInt(MovieContract.MovieColumns.INDEX_ID);
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    Uri uri;
                    if (mLoaderId == POPULAR_MOVIE_LOADER) {
                        uri = MovieContract.PopularMovieEntry.buildPopularMovieUri(movieId);
                    } else {
                        uri = MovieContract.TopRatedMovieEntry.buildTopRelatedMovieUri(movieId);
                    }
                    intent.putExtra(PARAM_MOVIE_URI, uri.toString());
                    startActivity(intent);
                }
                mPosition = position;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(POPULAR_MOVIE_LOADER, null, this);
        getLoaderManager().initLoader(TOP_RATED_LOADER, null, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadPopularMovies();
    }

    private void loadPopularMovies() {
        if (!Util.isOnline(getContext())) {
            Toast.makeText(getContext(), R.string.message_no_network_connection, Toast.LENGTH_SHORT).show();
        }
        mPresenter.fetchPopularMovie();
    }


    @Override
    public void setPresenter(PopularContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri;
        if (id == POPULAR_MOVIE_LOADER) {
            uri = MovieContract.PopularMovieEntry.CONTENT_URI;
        } else {
            uri = MovieContract.TopRatedMovieEntry.CONTENT_URI;
        }
        return new CursorLoader(getActivity(), uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mLoaderId = loader.getId();
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
