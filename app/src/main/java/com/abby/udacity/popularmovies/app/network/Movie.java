package com.abby.udacity.popularmovies.app.network;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Immutable model class for a Movie.
 */
public class Movie implements Parcelable {
    // id
    @SerializedName("id")
    private int mId;

    // original title
    @SerializedName("original_title")
    private String mOriginalTitle;

    //    movie poster image thumbnail
    @SerializedName("poster_path")
    private final String mPosterPath;

    //    A plot synopsis (called overview in the api)
    @SerializedName("overview")
    private final String mOverview;

    //    user rating (called vote_average in the api)
    @SerializedName("vote_average")
    private final double mVoteAverage;

    @SerializedName("popularity")
    private final double mPopularity;

    // release date
    @SerializedName("release_date")
    private final String releaseDate;

    public Movie(Parcel in) {
        this.mId = in.readInt();
        this.mOriginalTitle = in.readString();
        this.mPosterPath = in.readString();
        this.mOverview = in.readString();
        this.mVoteAverage = in.readDouble();
        this.mPopularity = in.readDouble();
        this.releaseDate = in.readString();
    }

    public int getId() {
        return mId;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public double getPopularity() {return mPopularity;}

    public double getVoteAverage() {return mVoteAverage;}

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mOriginalTitle);
        dest.writeString(this.mPosterPath);
        dest.writeString(this.mOverview);
        dest.writeDouble(this.mVoteAverage);
        dest.writeDouble(this.mPopularity);
        dest.writeString(this.releaseDate);

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
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

