/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package es.alvaroweb.catme.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.DefaultValue;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Table;

import static android.R.attr.value;
import static net.simonvt.schematic.annotation.DataType.Type.*;

@Database(version = CatmeDatabase.VERSION)
public final class CatmeDatabase {
    public static final int VERSION = 1;

    @Table(CategoriesColumns.class) public static final String CATEGORIES = "categories";
    @Table(ImageColumns.class) public static final String IMAGES = "images";


    public interface CategoriesColumns{
        @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
        @DataType(TEXT) @NotNull String NAME = "name";
    }

    public interface ImageColumns{
        @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
        @DataType(TEXT) @NotNull String API_ID = "api_id";
        @DataType(INTEGER) @DefaultValue("0") String IS_FAVORITE = "is_favorite";
        @DataType(INTEGER) String VOTE = "vote";
    }

}
