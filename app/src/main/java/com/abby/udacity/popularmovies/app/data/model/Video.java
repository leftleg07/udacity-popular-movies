package com.abby.udacity.popularmovies.app.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gsshop on 2016. 8. 9..
 */
public class Video {
    @SerializedName("id")
    public String mVideoId;

    @SerializedName("key")
    public String mKey;

    @SerializedName("name")
    public String mName;

    @SerializedName("size")
    public long mSize;

    @SerializedName("type")
    public String mType;
}
