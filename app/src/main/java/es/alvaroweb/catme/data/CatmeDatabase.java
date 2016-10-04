/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Table;

import static android.R.attr.value;
import static net.simonvt.schematic.annotation.DataType.Type.*;

@Database(version = CatmeDatabase.VERSION)
public final class CatmeDatabase {
    public static final int VERSION = 1;

    @Table(CategoriesColumns.class) public static final String CATEGORIES = "categories";
    @Table(CategoriesColumns.class) public static final String IMAGES = "images";


    public interface CategoriesColumns{
        @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
        @DataType(TEXT) @NotNull String NAME = "name";
    }

    public interface ImageColumns{
        @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
        @DataType(TEXT) @NotNull String API_ID = "api_id";
        @DataType(INTEGER) @NotNull String IS_FAVORITE = "is_favorite";
        @DataType(INTEGER) @NotNull String VOTE = "vote";
        String FAVORITE_VALUE = "10";
        String NOT_FAVORITE_VALUE = "0";
    }

}
