package com.ingic.auditix.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ingic.auditix.R;
import com.ingic.auditix.activities.DockActivity;


/**
 * Created by khan_muhammad on 3/27/2017.
 */

public class InternetHelper {
    public static int getConnectedNetworkType(final DockActivity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        int networkType = -1;
        if (cm != null) {
            NetworkInfo netInfoMob = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo netInfoWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (netInfoMob != null && netInfoMob.isConnectedOrConnecting()) {
                Log.v("TAG", "Mobile Internet connected");
                networkType = ConnectivityManager.TYPE_MOBILE;
            }
            if (netInfoWifi != null && netInfoWifi.isConnectedOrConnecting()) {
                Log.v("TAG", "Wifi Internet connected");
                networkType = ConnectivityManager.TYPE_WIFI;

            }
        }
        return networkType;
    }

    public static boolean isConnectedOnMobile(final DockActivity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        if (cm != null) {
            NetworkInfo netInfoMob = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (netInfoMob != null && netInfoMob.isConnectedOrConnecting()) {
                Log.v("TAG", "Mobile Internet connected");
                isConnected = true;
            }
        }
        return isConnected;
    }

    public static boolean CheckInternetConectivityandShowToast(final DockActivity activity) {

        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null) {
            return true;
        } else {
            // text.setText("Look your not online");
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UIHelper.showShortToastInCenter(activity, activity.getString(R.string.connection_lost));
                }
            });

            return false;
        }


    }

}
