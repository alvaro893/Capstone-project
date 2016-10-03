/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/*
 * TODO: Create JavaDoc
 */
public class CatMe extends Application {
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void startTracking(){
        if(mTracker == null) {
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
            mTracker = ga.newTracker(BuildConfig.ANALYTICS_ID);
            mTracker.enableAutoActivityTracking(true);
            ga.enableAutoActivityReports(this);
        }
    }

}
