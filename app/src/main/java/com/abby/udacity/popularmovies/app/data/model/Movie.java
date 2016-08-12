package com.abby.udacity.popularmovies.app.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Immutable model class for a Movie.
 */
public class Movie  {
    // id
    @SerializedName("id")
    public long mId;

    // original title
    @SerializedName("original_title")
    public String mOriginalTitle;

    //    movie poster image thumbnail
    @SerializedName("poster_path")
    public String mPosterPath;

    //    A plot synopsis (called overview in the api)
    @SerializedName("overview")
    public String mOverview;

    //    user rating (called vote_average in the api)
    @SerializedName("vote_average")
    public double mVoteAverage;

    @SerializedName("popularity")
    public double mPopularity;

    // release date
    @SerializedName("release_date")
    public String mReleaseDate;

}

