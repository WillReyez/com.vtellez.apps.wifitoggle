package com.vtellez.apps.wifitoggle;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * This app widget is used to manage the status of the device wifi. This is a simple widget which
 * set ON/OFF the wifi depending of the current state. This widget provider send intents with the
 * different actions and the ConnectivityReceiver, broadcast receiver, gets the action and set the
 * new status updating the views.
 *
 * Created by victorm.tellez on 15/09/2015.
 */
public class WifiToggleWidget extends AppWidgetProvider {
    /**
     * Constant used in the logs.
     */
    public static final String WIDGET_TAG = "Toggle wifi";

    /**
     * Constant used to sending and getting the correct intents.
     */
    public static final String ACTION_WIDGET_WIFI = "ActionReceiverWifi";

    /**
     * Handles the on update of the App widget provider. This method send the intents and update
     * the views in order to show the new state to the user as soon as possible.
     *
     * @param context               of the application
     * @param appWidgetManager      used to manage the widget
     * @param appWidgetIds          the ids to get the id of the current widget
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        if (BuildConfig.DEBUG) {
            Log.i(WIDGET_TAG, "WifiToggleWidget onUpdate");
        }

        // Get ids  instances  widget
        ComponentName widget = new ComponentName(context, WifiToggleWidget.class);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(widget);
        for (int widgetId : widgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            updateViews(context);

            Intent intent = new Intent(context, ConnectivityReceiver.class);
            intent.setAction(ACTION_WIDGET_WIFI);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.wifi_button, pendingIntent);

            if (BuildConfig.DEBUG) {
                Log.i(WIDGET_TAG, "WifiToggleWidget pending intent set");
            }

            // Tell the AppWidgetManager to perform an update on the current App Widget
            appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }
    }

    /**
     * Updates the views with the current status of the wifi.
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
                    Log.d(WIDGET_TAG, "WifiToggleWidget updateViews ConnectedOrConnecting = true");
                }
                remoteViews.setImageViewResource(R.id.wifi_button, R.mipmap.wifi_on);
            } else {
                if (BuildConfig.DEBUG) {
                    Log.d(WIDGET_TAG, "WifiToggleWidget updateViews ConnectedOrConnecting = false");
                }
                remoteViews.setImageViewResource(R.id.wifi_button, R.mipmap.wifi_off);
            }
        }
        ComponentName widget = new ComponentName(context, WifiToggleWidget.class);
        AppWidgetManager widgetManagers = AppWidgetManager.getInstance(context);
        widgetManagers.updateAppWidget(widget, remoteViews);
    }

}


