package com.abby.udacity.popularmovies.app.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * utility
 */
public class Util {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
