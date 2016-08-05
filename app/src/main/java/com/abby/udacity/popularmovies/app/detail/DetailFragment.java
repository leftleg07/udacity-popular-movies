package com.abby.udacity.popularmovies.app.detail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.network.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main UI for the movie detail screen.
 */
public class DetailFragment extends Fragment {
    private static final String ARG_PARAM_MOVIE = "_arg_param_movie";

    @BindView(R.id.textview_title)
    TextView mTitleView;
    @BindView(R.id.imageview_thumbnail)
    ImageView mThumbnailView;
    @BindView(R.id.textview_overview)
    TextView mOverviewView;
    @BindView(R.id.textview_vote_average)
    TextView mVoteAverageView;
    @BindView(R.id.textview_release_date)
    TextView mReleaseDateView;

    private Movie mMovie;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movie Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(Movie movie) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(ARG_PARAM_MOVIE);
        }
    }

    private static final String THUMBNAIL_BASE_URL = "http://image.tmdb.org/t/p/w185";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        mTitleView.setText(mMovie.getOriginalTitle());
        String url = THUMBNAIL_BASE_URL + mMovie.getPosterPath();
        Picasso.with(getContext())
                .load(url)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder_error)
                .into(mThumbnailView);
        mOverviewView.setText(mMovie.getOverview());
        mVoteAverageView.setText("" + mMovie.getVoteAverage());
        mReleaseDateView.setText(mMovie.getReleaseDate());
        return rootView;
    }


}
