package com.abby.udacity.popularmovies.app.ui.detail;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.data.db.MovieContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_MOVIE_ID = "_param_movie_id";
    private static final int REVIEW_LOADER = 0;

    // TODO: Rename and change types of parameters
    private long mParamMovieId;


    @BindView(R.id.listView_review)
    ListView mListView;

    private CursorAdapter mAdapter;

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
        setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light);
        if (getArguments() != null) {
            mParamMovieId = getArguments().getLong(ARG_PARAM_MOVIE_ID);
        }

        mAdapter = new ReviewAdapter(getContext());
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
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(REVIEW_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MovieContract.ReviewEntry.buildReviewMovieUri(mParamMovieId);
        return new CursorLoader(getActivity(), uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        assert data.getCount() > 0;
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }


    static class ReviewAdapter extends CursorAdapter {

        public ReviewAdapter(Context context) {
            super(context, null, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.list_item_review, parent, false);
            ViewHolder holder = new ViewHolder(view);
            view.setTag(holder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            String author = cursor.getString(MovieContract.ReviewEntry.INDEX_COLUMN_AUTHOR);
            String content = cursor.getString(MovieContract.ReviewEntry.INDEX_COLUMN_CONTENT);


            ViewHolder holder = (ViewHolder) view.getTag();
            holder.mAuthorText.setText(author);
            holder.mContentText.setText(content);
        }

    }

    static class ViewHolder {
        @BindView(R.id.textView_item_review_author)
        TextView mAuthorText;
        @BindView(R.id.textView_item_review_content)
        TextView mContentText;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
