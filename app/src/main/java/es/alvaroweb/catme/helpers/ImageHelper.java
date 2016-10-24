/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;

import es.alvaroweb.catme.R;
import es.alvaroweb.catme.model.Image;

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

    public static void setImageFromBlob(Context context, byte[] blob, ImageView view){
        Glide.with(context)
                .load(blob)
                .asBitmap()
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.unload_image_24dp)
                .into(view);
    }

    public static void setImageToBlob(Context context, Image image, SimpleTarget<Bitmap> listener) {
        Glide.with(context)
                .load(image.getUrl())
                .asBitmap()
                .thumbnail(0.1f)
                .skipMemoryCache(true)
                .fitCenter()
                .into(listener);
    }
}
