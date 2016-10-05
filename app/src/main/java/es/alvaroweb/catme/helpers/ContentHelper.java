/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.helpers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import es.alvaroweb.catme.data.CatmeDatabase;
import es.alvaroweb.catme.data.CatmeProvider;
import es.alvaroweb.catme.model.Image;
import es.alvaroweb.catme.model.Vote;
import es.alvaroweb.catme.model.VoteResponse;

/*
 * TODO: Create JavaDoc
 */
public class ContentHelper {
    public static void updateOrInsertImage(ContentResolver cr, Image image) {
        String where = CatmeDatabase.ImageColumns.API_ID + "=?";
        String[] selection = {image.getId()};
        Uri uri = CatmeProvider.Images.CONTENT_URI;

        ContentValues values = new ContentValues();
        values.put(CatmeDatabase.ImageColumns.API_ID, image.getId());
        values.put(CatmeDatabase.ImageColumns.VOTE, image.getScore());
        // add more values
        int rowUpdated = cr.update(uri, values, where, selection);
        if (rowUpdated < 1) {
            cr.insert(uri, values);
        }
    }

    public static void updateOrInsertFavorite(ContentResolver cr, Image image){
        String where = CatmeDatabase.ImageColumns.API_ID + "=?";
        String[] selection = {image.getId()};
        Uri uri = CatmeProvider.Images.CONTENT_URI;

        ContentValues values = new ContentValues();
        values.put(CatmeDatabase.ImageColumns.API_ID, image.getId());
        values.put(CatmeDatabase.ImageColumns.IS_FAVORITE, CatmeProvider.Images.FAVORITE_TRUE);
        // add more values
        int rowUpdated = cr.update(uri, values, where, selection );
        if(rowUpdated < 1){
            cr.insert(uri, values);
        }
    }
    /** this is only for one vote*/
    public static void updateOrInsertVote(ContentResolver cr, VoteResponse response) {
        String where = CatmeDatabase.ImageColumns.API_ID + "=?";
        String[] selection;
        Uri uri = CatmeProvider.Images.CONTENT_URI;

        ContentValues values = new ContentValues();
        Vote vote = response.getvoteList().get(0);

        selection = new String[]{String.valueOf(vote.getSubId())};
        values.put(CatmeDatabase.ImageColumns.VOTE, vote.getScore());
        values.put(CatmeDatabase.ImageColumns.API_ID, vote.getSubId());

        int update = cr.update(uri, values, where, selection);
        if(update < 1){
            cr.insert(uri, values);
        }
    }


}
