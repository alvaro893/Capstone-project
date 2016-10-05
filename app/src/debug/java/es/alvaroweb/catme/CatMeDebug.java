/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme;

import com.facebook.stetho.Stetho;

import es.alvaroweb.catme.CatMe;

/*
 * TODO: Create JavaDoc
 */
public class CatMeDebug extends CatMe {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
