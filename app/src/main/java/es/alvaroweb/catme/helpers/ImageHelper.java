/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.helpers;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

import es.alvaroweb.catme.R;

/*
 * TODO: Create JavaDoc
 */
public class ImageHelper {

    /** Loads and sets the image in the given relativePath parameter using a external library */
    public static void setImage(Context context, ImageView view, String relativePath, boolean cropcenter) {
        String urlToLoad = relativePath;
        DrawableRequestBuilder<String> stringDrawableRequestBuilder = Glide
                .with(context)
                .load(urlToLoad)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.unload_image_24dp)
                .fitCenter();
        if (cropcenter){
            stringDrawableRequestBuilder
                    .centerCrop()
                    .into(view);
        }
        stringDrawableRequestBuilder.into(view);
    }
}
