package com.abby.udacity.popularmovies.app.data.network;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * The Movie DB API
 */
public interface TheMovieDBApiService {
    String POPULAR_MOVE_BASE_URL = "http://api.themoviedb.org";

    @GET("3/movie/{order}")
    Observable<String> getPopularMovie(@Path("order") String order);

    @GET("3/movie/{id}/videos")
    Observable<String> getTrailer(@Path("id") long movieId);

    @GET("3/movie/{id}/reviews")
    Observable<String> getReview(@Path("id") long movieId);
}
