package com.abby.udacity.popularmovies.app.ui.detail;


import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.data.db.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrailerDilaogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrailerDilaogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_MOVIE_ID = "_param_movie_id";

    // Format of the youtube link to a Video Library video
    public static final String YOUTUBE_URL_FMT = "https://www.youtube.com/watch?v=%s";


    private static final int TRAILER_LOADER = 0;

    // TODO: Rename and change types of parameters
    private long mParamMovieId;

    @BindView(R.id.listView_trailer)
    ListView mListView;
    private CursorAdapter mAdapter;

    public TrailerDilaogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movieId Parameter 1.
     * @return A new instance of fragment TrailerDilaogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrailerDilaogFragment newInstance(long movieId) {
        TrailerDilaogFragment fragment = new TrailerDilaogFragment();
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

        mAdapter = new TrailerAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trailer_dilaog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                String key = cursor.getString(MovieContract.VideoEntry.INDEX_COLUMN_KEY);
                String youtubeLink = String.format(Locale.US, YOUTUBE_URL_FMT, key);
                // Start playing the video on Youtube.
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink)));
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(TRAILER_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MovieContract.VideoEntry.buildVideoMovieUri(mParamMovieId);
        return new CursorLoader(getActivity(), uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }

    static class TrailerAdapter extends CursorAdapter {
        // Fallback URL to get a youtube video thumbnail in case one is not provided in the data
        // (normally it should, but this is a safety fallback if it doesn't)
        public static final String YOUTUBE_THUMB_URL_FMT =
                "http://img.youtube.com/vi/%s/default.jpg";

        public TrailerAdapter(Context context) {
            super(context, null, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.list_item_trailer, parent, false);
            ViewHolder holder = new ViewHolder(view);
            view.setTag(holder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            String key = cursor.getString(MovieContract.VideoEntry.INDEX_COLUMN_KEY);
            String name = cursor.getString(MovieContract.VideoEntry.INDEX_COLUMN_NAME);

            String thumbUrl = String.format(Locale.US, YOUTUBE_THUMB_URL_FMT, key);

            ViewHolder holder = (ViewHolder) view.getTag();
            Picasso.with(context)
                    .load(thumbUrl)
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_placeholder_error)
                    .into(holder.mThumbnailImage);

            holder.mNameText.setText(name);
        }

    }

    static class ViewHolder {
        @BindView(R.id.imageView_thumbnail)
        ImageView mThumbnailImage;
        @BindView(R.id.textView_name)
        TextView mNameText;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
