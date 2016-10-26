package es.alvaroweb.catme.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import es.alvaroweb.catme.R;

/**
 * Implementation of App Widget functionality.
 */
public class CatWidget extends AppWidgetProvider {

    private static final String DEBUG_TAG = CatWidget.class.getSimpleName();
    private static int count = 0;
    private static int maxCount = 0;

    static void updateAppWidget(Context context) {
        if (count >= maxCount){
            count = 0;
        }
        context.startService(new Intent(context, WidgetIntentService.class));
        count++;
    }

    public static int getCount() {
        return count;
    }

    public static void setMaxCount(int maxCount) {
        CatWidget.maxCount = maxCount;
    }

    public static void setCount(int count) {
        CatWidget.count = count;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager,  appWidgetIds);
        Log.d(DEBUG_TAG, "on update");
        updateAppWidget(context);
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        super.onReceive(context, intent);
//        Log.d(DEBUG_TAG, "on receive");
//        updateAppWidget(context);
//
//    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.

    }

}

