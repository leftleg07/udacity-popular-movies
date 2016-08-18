package com.abby.udacity.popularmovies.app.ui.main;


import android.content.Context;
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
import android.widget.TextView;

import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.data.db.MovieContract;
import com.abby.udacity.popularmovies.app.ui.detail.DetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Encapsulates fetching movie posters and displaying it as a {@link GridView} layout.
 */
public class PopularFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Callback mCallback;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * PopularFragmentCallback for when an item has been selected.
         */
        void onItemSelected(Uri contentUri);
    }


    public static final String PARAM_MOVIE_URI = "_param_movie_uri";
    private static final String STATE_POSITION = "_state_position";
    public static final int MOST_POPULAR_LOADER = 0;
    public static final int HIGHEST_RATED_LOADER = 1;
    public static final int FAVORITE_LOADER = 2;

    private PopularAdaptor mAdapter;

    @BindView(R.id.listView_popular_movie)
    GridView mListView;
    @BindView(R.id.textView_popular_empty)
    TextView mEmptyText;

    private int mPosition = ListView.INVALID_POSITION;
    private int mLastLoaderId = MOST_POPULAR_LOADER;

    public PopularFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new PopularAdaptor(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_POSITION)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mEmptyText.setVisibility(View.INVISIBLE);
        // Get a reference to the GridView, and attach this adapter to it.
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    int movieId = cursor.getInt(MovieContract.MovieColumns.INDEX_ID);
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    Uri uri = null;
                    if (mLastLoaderId == MOST_POPULAR_LOADER) {
                        uri = MovieContract.PopularMovieEntry.buildPopularMovieUri(movieId);
                    } else if (mLastLoaderId == HIGHEST_RATED_LOADER) {
                        uri = MovieContract.HighestRatedMovieEntry.buildTopRelatedMovieUri(movieId);
                    } else if (mLastLoaderId == FAVORITE_LOADER) {
                        uri = MovieContract.FavoriteMovieEntry.buildFavoriteMovieUri(movieId);
                    }
                    mCallback.onItemSelected(uri);
                }
                mPosition = position;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (Callback) getActivity();
        } catch (ClassCastException e) {
            mCallback = null;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_POSITION, mPosition);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mLastLoaderId = id;
        mPosition = 0;
        Uri uri = null;
        if (id == MOST_POPULAR_LOADER) {
            uri = MovieContract.PopularMovieEntry.CONTENT_URI;
        } else if (id == HIGHEST_RATED_LOADER) {
            uri = MovieContract.HighestRatedMovieEntry.CONTENT_URI;
        } else if (id == FAVORITE_LOADER) {
            uri = MovieContract.FavoriteMovieEntry.CONTENT_URI;
        }

        return new CursorLoader(getActivity(), uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(mLastLoaderId != loader.getId()) {
            return;
        }
        if (data.getCount() > 0) {
            mEmptyText.setVisibility(View.INVISIBLE);
            mAdapter.swapCursor(data);
            if (mPosition != ListView.INVALID_POSITION) {
                // If we don't need to restart the loader, and there's a desired position to restore
                // to, do so now.
                mListView.smoothScrollToPosition(mPosition);
            }
        } else {
            mEmptyText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
