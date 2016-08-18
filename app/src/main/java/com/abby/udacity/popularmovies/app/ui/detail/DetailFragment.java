package com.abby.udacity.popularmovies.app.ui.detail;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
 * Main UI for the mMovie detail screen.
 */
public class DetailFragment extends Fragment implements DetailContract.View, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = DetailFragment.class.getSimpleName();

    public static final String ARG_PARAM_MOVIE = "_arg_param_movie";
    private static final int DETAIL_LOADER = 10;
    private static final int REVIEW_LOADER = 20;
    private static final int VIDEO_LOADER = 30;
    private static final int FAVORITE_LOADER = 40;

    @BindView(R.id.textView_detail_title)
    TextView mTitleText;
    @BindView(R.id.imageView_detail_poster)
    ImageView mPosterImage;
    @BindView(R.id.textView_detail_date)
    TextView mDateText;
    @BindView(R.id.textView_detail_overview)
    TextView mOverviewText;
    @BindView(R.id.textView_detail_trailer_count)
    TextView mTrailerCountText;
    @BindView(R.id.textView_detail_review_count)
    TextView mReviewCountText;
    @BindView(R.id.checkBox_detail_favorite)
    CheckBox mFavoriteCheck;
    @BindView(R.id.textView_detail_average)
    TextView mAverageText;


    private Uri mUri;
    private DetailContract.Presenter mPresenter;
    private ContentResolver mContentResolver;
    private long mMovieId;
    private Movie mMovie;

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
    public static DetailFragment newInstance(Uri uri) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_MOVIE, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUri = getArguments().getParcelable(ARG_PARAM_MOVIE);
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
        mContentResolver = getActivity().getContentResolver();
        mFavoriteCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Uri uri = MovieContract.FavoriteMovieEntry.buildFavoriteMovieUri(mMovieId);
                if (isChecked) {
                    Cursor cursor = mContentResolver.query(uri, null, null, null, null);
                    if (cursor.getCount() == 0) {
                        ContentValues values = new ContentValues();
                        values.put(MovieContract.MovieColumns._ID, mMovieId);
                        values.put(MovieContract.MovieColumns.COLUMN_ORIGINAL_TITLE, mMovie.mOriginalTitle);
                        values.put(MovieContract.MovieColumns.COLUMN_POSTER_PATH, mMovie.mPosterPath);
                        values.put(MovieContract.MovieColumns.COLUMN_OVERVIEW, mMovie.mOverview);
                        values.put(MovieContract.MovieColumns.COLUMN_VOTE_AVERAGE, mMovie.mVoteAverage);
                        values.put(MovieContract.MovieColumns.COLUMN_POPULARITY, mMovie.mPopularity);
                        values.put(MovieContract.MovieColumns.COLUMN_RELEASE_DATE, mMovie.mReleaseDate);

                        mContentResolver.insert(uri, values);
                    }
                } else {

                    mContentResolver.delete(uri, null, null);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mUri != null) {
            mMovieId = ContentUris.parseId(mUri);

            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
            getLoaderManager().initLoader(FAVORITE_LOADER, null, this);
            getLoaderManager().initLoader(REVIEW_LOADER, null, this);
            getLoaderManager().initLoader(VIDEO_LOADER, null, this);

            mPresenter.fetchVideo(mMovieId);
            mPresenter.fetchReview(mMovieId);
        }
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DETAIL_LOADER:
                return new CursorLoader(getActivity(), mUri, MovieContract.MovieColumns.PROJECTION, null, null, null);
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
                if (data != null && data.moveToNext()) {
                    mMovie = new Movie();
                    mMovie.mOriginalTitle = data.getString(MovieContract.MovieColumns.INDEX_COLUMN_ORIGINAL_TITLE);
                    mMovie.mPosterPath = data.getString(MovieContract.MovieColumns.INDEX_COLUMN_POSTER_PATH);
                    mMovie.mOverview = data.getString(MovieContract.MovieColumns.INDEX_COLUMN_OVERVIEW);
                    mMovie.mVoteAverage = data.getDouble(MovieContract.MovieColumns.INDEX_COLUMN_VOTE_AVERAGE);
                    mMovie.mPopularity = data.getDouble(MovieContract.MovieColumns.INDEX_COLUMN_POPULARITY);
                    mMovie.mReleaseDate = data.getString(MovieContract.MovieColumns.INDEX_COLUMN_RELEASE_DATE);
                    updateDetail(mMovie);
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
                mFavoriteCheck.setChecked(data.getCount() > 0);
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void updateDetail(Movie m) {
        mTitleText.setText(m.mOriginalTitle);
        mDateText.setText(m.mReleaseDate);
        mOverviewText.setText(m.mOverview);
        mAverageText.setText("" + m.mVoteAverage);

        String url = THUMBNAIL_BASE_URL + m.mPosterPath;
        Picasso.with(getActivity())
                .load(url)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder_error)
                .into(mPosterImage);


    }

    @OnClick(R.id.button_detail_review)
    public void onReview(View view) {
        ReviewDialogFragment fragment = ReviewDialogFragment.newInstance(mMovieId);
        fragment.show(getFragmentManager(), ReviewDialogFragment.class.getSimpleName());
    }


    @OnClick(R.id.button_detail_trailer)
    public void onTrailer(View view) {
        TrailerDilaogFragment fragment = TrailerDilaogFragment.newInstance(mMovieId);
        fragment.show(getFragmentManager(), TrailerDilaogFragment.class.getSimpleName());
    }
}
