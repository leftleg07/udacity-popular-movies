package com.abby.udacity.popularmovies.app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Immutable model class for a movie.
 */
public class Movie implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    public Movie(Parcel in) {
        this.mId = in.readLong();
        this.mOriginalTitle = in.readString();
        this.mPosterPath = in.readString();
        this.mOverview = in.readString();
        this.mVoteAverage = in.readDouble();
        this.mPopularity = in.readDouble();
        this.mReleaseDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mOriginalTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeDouble(mVoteAverage);
        dest.writeDouble(mPopularity);
        dest.writeString(mReleaseDate);

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

