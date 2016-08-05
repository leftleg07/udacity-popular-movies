package com.abby.udacity.popularmovies.app.network;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gsshop on 2016. 7. 11..
 */
public interface TheMovieDBApiService {
    String POPULAR_MOVE_BASE_URL = "http://api.themoviedb.org";

    @GET("3/movie/{order}")
    Observable<String> getPopularMovie(@Path("order") String order);

    @GET("3/movie/{id}/videos")
    Observable<String> getTrailers(@Path("id") String id);

    @GET("3/movie/{id}/reviews")
    Observable<String> getReviews(@Path("id") String id);
}
