package com.abby.udacity.popularmovies.app.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Service that handles sync.
 */

public class PopularSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static PopularSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("PopularSyncService", "onCreate - PopularSyncService");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new PopularSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}