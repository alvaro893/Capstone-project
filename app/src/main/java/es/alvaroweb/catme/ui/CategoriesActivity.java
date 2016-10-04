package es.alvaroweb.catme.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.Api;

import es.alvaroweb.catme.R;
import es.alvaroweb.catme.data.CatmeDatabase;
import es.alvaroweb.catme.data.CatmeProvider;
import es.alvaroweb.catme.model.Categories;
import es.alvaroweb.catme.network.ApiConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoriesActivity extends AppCompatActivity {

    private static final String LOG_TAG = CategoriesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        ApiConnection apiConnection = new ApiConnection(this);
//        apiConnection.getCategories(new Callback<Categories>() {
//            @Override
//            public void onResponse(Call<Categories> call, Response<Categories> response) {
//                Log.d("test", response.body().getCategories().get(4).getName());
//            }
//
//            @Override
//            public void onFailure(Call<Categories> call, Throwable t) {
//                Log.d("test", t.getMessage());
//            }
//        });
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(CatmeDatabase.CategoriesColumns._ID, 6);
//        contentValues.put(CatmeDatabase.CategoriesColumns.NAME, "tesst");
//        int update = getContentResolver().update(CatmeProvider.Categories.CONTENT_URI,
//                contentValues, null, null);
//        Log.d(LOG_TAG, "updaet:" + update);
    }


}
