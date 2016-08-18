package com.abby.udacity.popularmovies.app.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Immutable model class for a review.
 */
public class Review {

    @SerializedName("id")
    public String mReviewId;

    @SerializedName("author")
    public String mAuthor;

    @SerializedName("content")
    public String mContent;

    @SerializedName("url")
    public String mUrl;
}
