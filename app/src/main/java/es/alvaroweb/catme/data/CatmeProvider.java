/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/*
 * TODO: Create JavaDoc
 */
@ContentProvider(
        authority = CatmeProvider.AUTHORITY,
        database = CatmeDatabase.class)
public final class CatmeProvider {

    public static final String AUTHORITY = "es.alvaroweb.catme.data.CatmeProvider";
    static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY );

    private static Uri buildUri(String... paths){
        Uri.Builder builder = BASE_URI.buildUpon();
        for (String path:paths){
            builder.appendPath(path);
        }
        return builder.build();
    }

    // for table categories
    @TableEndpoint(table = CatmeDatabase.CATEGORIES)
    public static class Categories {
        final static String PATH = CatmeDatabase.CATEGORIES;

        @ContentUri(
                path = PATH,
                type = "vnd.android.cursor.dir/category")
        public static final Uri CONTENT_URI = buildUri(PATH);

    }

    // for table images
    @TableEndpoint(table = CatmeDatabase.IMAGES)
    public static class Images {
        public final static String FAVORITE_TRUE = "10";
        public final static String FAVORITE_FALSE = "0";
        public final static String VOTE_UP = "10";
        public final static String VOTE_DOWN = "1";
        final static String PATH = CatmeDatabase.IMAGES;

        @ContentUri(
                path = PATH,
                type = "vnd.android.cursor.dir/image"
        )
        public static final Uri CONTENT_URI = buildUri(PATH);

        @InexactContentUri(
            name = "API_ID_VALUE",
            path = PATH + "/*",
            type = "vnd.android.cursor.item/image",
            whereColumn = CatmeDatabase.ImageColumns.API_ID,
            pathSegment = 1
        )
        public static Uri withApiId(String apiId){
            return buildUri(PATH, apiId);
        }

//        @InexactContentUri(
//                name = "VOTE_VALUE",
//                path = PATH + "/*",
//                type = "vnd.android.cursor.item/image",
//                whereColumn = CatmeDatabase.ImageColumns.VOTE,
//                pathSegment = 1
//        )
//        public static Uri withVote(String vote){
//            return buildUri(PATH, vote);
//        }
//
//        @InexactContentUri(
//                name = "FAVORITE_VALUE",
//                path = PATH + "/*",
//                type = "vnd.android.cursor.item/image",
//                whereColumn = CatmeDatabase.ImageColumns.IS_FAVORITE,
//                pathSegment = 1
//        )
//        public static Uri withFavoriteValue(String favoriteValue){
//            return buildUri(PATH, favoriteValue);
//        }
    }
}
