package com.abby.udacity.popularmovies.app.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by gsshop on 2016. 8. 8..
 */
public class Util {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
