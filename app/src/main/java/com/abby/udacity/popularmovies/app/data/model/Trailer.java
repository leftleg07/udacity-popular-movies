package com.abby.udacity.popularmovies.app.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Immutable model class for a trailer.
 */
public class Trailer {
    @SerializedName("id")
    public String mTrailerId;

    @SerializedName("key")
    public String mKey;

    @SerializedName("name")
    public String mName;

    @SerializedName("size")
    public long mSize;

    @SerializedName("type")
    public String mType;
}
