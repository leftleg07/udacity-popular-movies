package com.abby.udacity.popularmovies.app.ui.main;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.abby.udacity.popularmovies.app.R;
import com.abby.udacity.popularmovies.app.data.db.MovieContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gsshop on 2016. 8. 5..
 */
public class PopularAdaptor extends CursorAdapter {
    private static final String THUMBNAIL_BASE_URL = "http://image.tmdb.org/t/p/w185";

    public PopularAdaptor(Context context) {
        super(context, null, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.list_item_movie, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String path=cursor.getString(MovieContract.MovieColumns.INDEX_COLUMN_POSTER_PATH);
        String url = THUMBNAIL_BASE_URL + path;
        ViewHolder holder = (ViewHolder) view.getTag();

        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder_error)
                .into(holder.mThumbnailView);

    }

    static class ViewHolder {
        @BindView(R.id.imageView_thumbnail)
        ImageView mThumbnailView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
