package com.vtellez.apps.wifitoggle;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by victorm.tellez on 15/09/2015.
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(WifiToggleWidget.WIDGET_TAG, "ConnectivityReceiver onReceive  action: " + intent.getAction());
        }
        if (intent.getAction().equals(WifiToggleWidget.ACTION_WIDGET_WIFI)) {

            setWifiConnectivity(context, intent);
        } else if ((intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE"))||
                (intent.getAction().equals("android.net.wifi.STATE_CHANGE"))){

           updateViews(context);

        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context);
        appWidgetManager.updateAppWidget(new ComponentName(context,
                WifiToggleWidget.class), remoteViews);

    }

    private void setWifiConnectivity(Context context, Intent intent) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        if (isWifiEnabled(context)) {
            if (BuildConfig.DEBUG) {
                Log.d(WifiToggleWidget.WIDGET_TAG, "ConnectivityReceiver setWifiConnectivity --> Turn OFF ");
            }
            setWifiState(context, false);
            remoteViews.setImageViewResource(R.id.wifi_button, R.mipmap.wifi_off);
        } else {
            if (BuildConfig.DEBUG) {
                Log.d(WifiToggleWidget.WIDGET_TAG, "ConnectivityReceiver setWifiConnectivity --> Turn ON ");
            }
            setWifiState(context, true);
            remoteViews.setImageViewResource(R.id.wifi_button, R.mipmap.wifi_on);
        }

        ComponentName widget = new ComponentName(context, WifiToggleWidget.class);
        AppWidgetManager widgetManagers = AppWidgetManager.getInstance(context);
        widgetManagers.updateAppWidget(widget, remoteViews);
    }

    public boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            if (BuildConfig.DEBUG) {
                Log.d(WifiToggleWidget.WIDGET_TAG, "ConnectivityReceiver isWifiEnabled = true");
            }
            return true;
        }
        if (BuildConfig.DEBUG) {
            Log.d(WifiToggleWidget.WIDGET_TAG, "ConnectivityReceiver isWifiEnabled = false");
        }
        return false;
    }

    public void setWifiState(Context context, boolean state) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(state);
    }

    public void updateViews(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        if (manager != null) {

            if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .isConnectedOrConnecting()) {
                if (BuildConfig.DEBUG) {
                    Log.d(WifiToggleWidget.WIDGET_TAG, "WifiToggleWidget updateViews ConnectedOrConnecting = true");
                }
                remoteViews.setImageViewResource(R.id.wifi_button, R.mipmap.wifi_on);
            } else {
                if (BuildConfig.DEBUG) {
                    Log.d(WifiToggleWidget.WIDGET_TAG, "WifiToggleWidget updateViews ConnectedOrConnecting = false");
                }
                remoteViews.setImageViewResource(R.id.wifi_button, R.mipmap.wifi_off);
            }
        }

        ComponentName widget = new ComponentName(context, WifiToggleWidget.class);
        AppWidgetManager widgetManagers = AppWidgetManager.getInstance(context);
        widgetManagers.updateAppWidget(widget, remoteViews);
    }

}
