package com.abby.udacity.popularmovies.app.ui.detail;


import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.data.db.MovieContract;
import com.abby.udacity.popularmovies.app.data.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Main UI for the movie detail screen.
 */
public class DetailFragment extends Fragment implements DetailContract.View, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = DetailFragment.class.getSimpleName();

    private static final String ARG_PARAM_MOVIE = "_arg_param_movie";
    private static final int DETAIL_LOADER = 10;
    private static final int REVIEW_LOADER = 20;
    private static final int VIDEO_LOADER = 30;
    private static final int FAVORITE_LOADER = 40;

    @BindView(R.id.textView_title) TextView mTitleText;
    @BindView(R.id.imageView_poster) ImageView mPosterImage;
    @BindView(R.id.textView_date) TextView mDateText;
    @BindView(R.id.textView_overview) TextView mOverviewText;
    @BindView(R.id.textView_trailer_count) TextView mTrailerCountText;
    @BindView(R.id.textView_review_count) TextView mReviewCountText;


    private Uri mUri;
    private DetailContract.Presenter mPresenter;
    private long mMovieId;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uri Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String uri) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_MOVIE, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String uriString = getArguments().getString(ARG_PARAM_MOVIE);
            mUri = Uri.parse(uriString);
        }
    }

    private static final String THUMBNAIL_BASE_URL = "http://image.tmdb.org/t/p/w185";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMovieId = ContentUris.parseId(mUri);

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(FAVORITE_LOADER, null, this);
        getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        getLoaderManager().initLoader(VIDEO_LOADER, null, this);

        mPresenter.fetchVideo(mMovieId);
        mPresenter.fetchReview(mMovieId);

    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DETAIL_LOADER:
                return new CursorLoader(getActivity(), mUri, null, null, null, null);
            case REVIEW_LOADER:
                return new CursorLoader(getActivity(), MovieContract.ReviewEntry.buildReviewMovieUri(mMovieId), null, null, null, null);
            case VIDEO_LOADER:
                return new CursorLoader(getActivity(), MovieContract.VideoEntry.buildVideoMovieUri(mMovieId), null, null, null, null);
            case FAVORITE_LOADER:
                return new CursorLoader(getActivity(), MovieContract.FavoriteMovieEntry.buildFavoriteMovieUri(mMovieId), null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case DETAIL_LOADER:
                Log.i(TAG, "detail count: " + data.getCount());
                if(data.getCount() > 0) {
                    data.moveToNext();
                    Movie movie = new Movie();
                    movie.mId = data.getLong(MovieContract.MovieColumns.INDEX_ID);
                    movie.mOriginalTitle = data.getString(MovieContract.MovieColumns.INDEX_COLUMN_ORIGINAL_TITLE);
                    movie.mPosterPath = data.getString(MovieContract.MovieColumns.INDEX_COLUMN_POSTER_PATH);
                    movie.mOverview = data.getString(MovieContract.MovieColumns.INDEX_COLUMN_OVERVIEW);
                    movie.mVoteAverage = data.getDouble(MovieContract.MovieColumns.INDEX_COLUMN_VOTE_AVERAGE);
                    movie.mPopularity = data.getDouble(MovieContract.MovieColumns.INDEX_COLUMN_POPULARITY);
                    movie.mReleaseDate = data.getString(MovieContract.MovieColumns.INDEX_RELEASE_DATE);
                    updateDetail(movie);
                }
                break;
            case REVIEW_LOADER:
                Log.i(TAG, "review count: " + data.getCount());
                mReviewCountText.setText(data.getCount() + " count");
                break;
            case VIDEO_LOADER:
                Log.i(TAG, "video count: " + data.getCount());
                mTrailerCountText.setText(data.getCount() + " count");
                break;
            case FAVORITE_LOADER:
                Log.i(TAG, "favorite count: " + data.getCount());
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void updateDetail(Movie m) {
        getActivity().setTitle(m.mOriginalTitle);
        mTitleText.setText(m.mOriginalTitle);
        mDateText.setText(m.mReleaseDate);
        mOverviewText.setText(m.mOverview);

        String url = THUMBNAIL_BASE_URL + m.mPosterPath;
        Picasso.with(getActivity())
                .load(url)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder_error)
                .into(mPosterImage);


    }

    @OnClick(R.id.button_review)
    public void onReview(View view) {
        ReviewDialogFragment fragment = ReviewDialogFragment.newInstance(mMovieId);
        fragment.show(getFragmentManager(), ReviewDialogFragment.class.getSimpleName());
    }


    @OnClick(R.id.button_trailer)
    public void onTrailer(View view) {
        TrailerDilaogFragment fragment = TrailerDilaogFragment.newInstance(mMovieId);
        fragment.show(getFragmentManager(), TrailerDilaogFragment.class.getSimpleName());
    }
}
