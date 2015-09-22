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
 * Implementation of App Widget functionality.
 */
public class WifiToggleWidget extends AppWidgetProvider {

    public static final String WIDGET_TAG = "Toggle wifi";
    public static final String ACTION_WIDGET_WIFI = "ActionReceiverWifi";

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


