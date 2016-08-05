package com.abby.udacity.popularmovies.app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class PopularMovieSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static PopularMovieSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("PopularMovieSyncService", "onCreate - PopularMovieSyncService");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new PopularMovieSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}