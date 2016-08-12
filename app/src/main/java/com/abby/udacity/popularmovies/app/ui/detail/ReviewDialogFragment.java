package com.abby.udacity.popularmovies.app.ui.detail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abby.udacity.popularmovies.app.R;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_MOVIE_ID = "_param_movie_id";

    // TODO: Rename and change types of parameters
    private long mParamMovieId;


    public ReviewDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movieId Parameter 1.
     * @return A new instance of fragment ReviewDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewDialogFragment newInstance(long movieId) {
        ReviewDialogFragment fragment = new ReviewDialogFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamMovieId = getArguments().getLong(ARG_PARAM_MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
