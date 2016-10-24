/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.helpers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.analytics.internal.zzy;

import java.io.ByteArrayOutputStream;

import es.alvaroweb.catme.data.CatmeDatabase;
import es.alvaroweb.catme.data.CatmeProvider;
import es.alvaroweb.catme.model.Image;
import es.alvaroweb.catme.model.Vote;
import es.alvaroweb.catme.model.VoteResponse;

import static android.R.attr.value;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.google.android.gms.analytics.internal.zzy.cr;
import static com.google.android.gms.internal.zzapz.bos;

/*
 * TODO: Create JavaDoc
 */
public class ContentHelper {
    public static void updateOrInsertImage(Context context, Image image) {
        String where = CatmeDatabase.ImageColumns.API_ID + "=?";
        String[] selection = {image.getId()};
        ContentResolver cr = context.getContentResolver();
        Uri uri = CatmeProvider.Images.CONTENT_URI;

        ContentValues values = new ContentValues();
        values.put(CatmeDatabase.ImageColumns.API_ID, image.getId());
        values.put(CatmeDatabase.ImageColumns.VOTE, image.getScore());
        // add more values
        insertOrUpdate(cr, uri, values, where, selection);
    }

    public static void updateOrInsertFavorite(ContentResolver cr, Image image){
        String where = CatmeDatabase.ImageColumns.API_ID + "=?";
        String[] selection = {image.getId()};
        Uri uri = CatmeProvider.Images.CONTENT_URI;

        ContentValues values = new ContentValues();
        values.put(CatmeDatabase.ImageColumns.API_ID, image.getId());
        values.put(CatmeDatabase.ImageColumns.IS_FAVORITE, CatmeProvider.Images.FAVORITE_TRUE);
        // add more values
        insertOrUpdate(cr, uri, values, where, selection);
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

        insertOrUpdate(cr, uri, values, where, selection);
    }


    public static void setFavorite(ContentResolver cr, String mCurrentId, String mCurrentUrl, String favoriteValue) {
        ContentValues values = new ContentValues();
        Uri uri = CatmeProvider.Images.withApiId(mCurrentId);

        values.put(CatmeDatabase.ImageColumns.IS_FAVORITE, favoriteValue);
        values.put(CatmeDatabase.ImageColumns.API_ID, mCurrentId);
        values.put(CatmeDatabase.ImageColumns.URL, mCurrentUrl);

        insertOrUpdate(cr, uri, values, null, null);
    }

    public static void setVote(ContentResolver cr, Image image, String vote) {
        ContentValues values = new ContentValues();
        Uri uri = CatmeProvider.Images.withApiId(image.getId());

        values.put(CatmeDatabase.ImageColumns.API_ID, image.getId());
        values.put(CatmeDatabase.ImageColumns.URL, image.getUrl());
        values.put(CatmeDatabase.ImageColumns.VOTE, vote);

        insertOrUpdate(cr, uri, values, null, null);
    }

    public static void setBlob(final Context context, Image image){
        final Uri uri = CatmeProvider.Images.withApiId(image.getId());
        final ContentResolver cr = context.getContentResolver();
        final String thumbnailCol = CatmeDatabase.ImageColumns.THUMBNAIL;


        ImageHelper.setImageToBlob(context, image, new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ContentValues values = new ContentValues();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                resource.compress(Bitmap.CompressFormat.JPEG, 10, bos);
                byte[] blob = bos.toByteArray();
                Log.d("bytes", "bytes:" + blob.length);

                values.put(thumbnailCol, blob);
                insertOrUpdate(cr, uri, values, null, null);
            }
        });
    }

    private static void insertOrUpdate(ContentResolver cr, Uri uri, ContentValues values,
                                       String where, String[] selection){
        int update = cr.update(uri, values, where, selection);
        if(update < 1){
            cr.insert(uri, values);
        }
    }
}
