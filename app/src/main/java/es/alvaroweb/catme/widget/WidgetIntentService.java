package es.alvaroweb.catme.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;

import es.alvaroweb.catme.R;
import es.alvaroweb.catme.data.ImageLoader;
import es.alvaroweb.catme.ui.MainActivity;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class WidgetIntentService extends IntentService implements Loader.OnLoadCompleteListener<Cursor> {
    private static final String DEBUG_TAG = WidgetIntentService.class.getSimpleName();
    final Context mContext = this;
    private int mCurrentWidgetId = -1;
    private Cursor mCursor;
    private AppWidgetManager mAppWidgetManager;

    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(DEBUG_TAG, "on handle intent");
        mAppWidgetManager = AppWidgetManager.getInstance(mContext);
        int[] appWidgetIds = mAppWidgetManager.getAppWidgetIds(new ComponentName(mContext,
                CatWidget.class));

        // update each widget instance
        for (int appWidgetId : appWidgetIds) {
            mCurrentWidgetId = appWidgetId;
            //String cat = CatWidgetConfigureActivity.loadTitlePref(mContext, appWidgetId);
            ImageLoader imageLoader = ImageLoader.allFavoritesInstance(mContext);
            imageLoader.registerListener(0, this);
            imageLoader.startLoading();
        }

    }

    @Override
    public void onLoadComplete(Loader loader, Cursor data) {
        Log.d(DEBUG_TAG, "on load complete");
        mCursor = data;
        CatWidget.setMaxCount(mCursor.getCount());
        setPicture();
    }

    private void setPicture() {
        if (mCursor.moveToFirst() && mCursor.moveToPosition(CatWidget.getCount())) {
            Log.d(DEBUG_TAG, "item n: " + mCursor.getPosition());
            final RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.cat_widget);
            byte[] blob = mCursor.getBlob(ImageLoader.Query.THUMBNAIL);
            AppWidgetTarget appWidgetTarget = new AppWidgetTarget(mContext, views, R.id.appwidget_image, mCurrentWidgetId);
            loadImage(appWidgetTarget, blob);

            Intent intent = new Intent(mContext, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.appwidget_image, pendingIntent);

            mAppWidgetManager.updateAppWidget(mCurrentWidgetId, views);
        }
    }


    private void loadImage(AppWidgetTarget appWidgetTarget, byte[] blob) {
        Glide.with(mContext.getApplicationContext())
                .load(blob)
                .asBitmap()
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.unload_image_24dp)
                .into(appWidgetTarget);
    }

}
