package com.abby.udacity.popularmovies.app.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.abby.udacity.popularmovies.app.BuildConfig;
import com.abby.udacity.popularmovies.app.data.network.TheMovieDBApiService;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * A module to wrap the Application state and expose it to the graph.
 */

@Module
public class ApplicationModule {
    private final Context applicationContext;

    public ApplicationModule(Context applicationContext) {
        this.applicationContext = applicationContext;


    }

    @Provides
    @Singleton
    public Context proviceApplicationContext() {
        return applicationContext;
    }

    @Provides
    @Singleton
    public TheMovieDBApiService provideTheMovieDBApiService(OkHttpClient client) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDBApiService.POPULAR_MOVE_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();


        return retrofit.create(TheMovieDBApiService.class);
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl newUrl = request.url().newBuilder().addQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY).build();
                Request newRequest = request.newBuilder().url(newUrl).build();
                return chain.proceed(newRequest);
            }
        });

        return httpClient.build();

    }

    @Provides
    @Singleton // Application reference must come from AppModule.class
    public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }


}
