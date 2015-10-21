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
 * This broadcast receiver gets the intent send from the app widget and sets the view and the status
 * checking the Action of the intent and using the current status.
 *
 * Created by victorm.tellez on 15/09/2015.
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    /**
     * Handles the on receive event where the widget can change its status to wifi on/off depending
     * of the action type got it from the intent.
     * If the action is WifiToggleWidget.ACTION_WIDGET_WIFI updates the status of the
     *
     * @param context   the context of the application
     * @param intent    to get the data
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if (BuildConfig.DEBUG) {
            Log.d(WifiToggleWidget.WIDGET_TAG, "ConnectivityReceiver onReceive  action: " + intent.getAction());
        }

        if (intent.getAction().equals(WifiToggleWidget.ACTION_WIDGET_WIFI)) {

            setWifiConnectivity(context);

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

    /**
     *  Sets the wifi connectivity. First check if the wifi is enabled and the sets the
     *  correspondent view.
     *
     * @param context   of the application
     */
    private void setWifiConnectivity(Context context) {

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

    /**
     * Checks if the wifi is enabled.
     *
     * @param context   of the application
     * @return          true if the wifi is on
     */
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

    /**
     * Sets the state of the widget.
     *
     * @param context   of the application
     * @param state     if true the wifi is set to on and if false the wifi is set to off
     */
    public void setWifiState(Context context, boolean state) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(state);
    }

    /**
     * Updates the views checking the current network and setting the correct image, on/off image
     *
     * @param context   of the application
     */
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
