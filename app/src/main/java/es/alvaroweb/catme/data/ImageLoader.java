/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.data;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import es.alvaroweb.catme.ui.fragments.ListFragment;

/*
 * TODO: Create JavaDoc
 */
public class ImageLoader extends CursorLoader{
//    public static ImageLoader newInstanceForItemId(Context context, long imageId){
//        String id = String.valueOf(imageId);
//        String selection = CatmeDatabase.ImageColumns._ID + "=?";
//        String[] arguments = new String[]{id};
//        Uri uri = CatmeProvider.Images.withApiId(id);
//        return new ImageLoader(context,uri, null, null, null, null);
//    }

    public static ImageLoader allFavoritesInstance(Context context){
        String selection = CatmeDatabase.ImageColumns.IS_FAVORITE + " =?";
        String[] selectionArgs = new String[]{CatmeProvider.Images.FAVORITE_TRUE};
        return new ImageLoader(context,
                CatmeProvider.Images.CONTENT_URI, null, selection, selectionArgs, null);
    }

    public static ImageLoader allVotesInstance(Context context){
        String selection = CatmeDatabase.ImageColumns.VOTE + " IS NOT NULL";
        return new ImageLoader(context,
                CatmeProvider.Images.CONTENT_URI, null, selection, null, null);
    }


    public ImageLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    public interface Query{
        int _ID = 0;
        int API_ID = 1;
        int IS_FAVORITE = 2;
        int VOTE = 3;
        int URL = 4;
        int THUMBNAIL = 5;
    }

}
