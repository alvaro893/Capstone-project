package es.alvaroweb.catme.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.alvaroweb.catme.R;
import es.alvaroweb.catme.data.CatmeDatabase;
import es.alvaroweb.catme.data.CatmeProvider;
import es.alvaroweb.catme.ui.PictureActivity;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;


/**
 * A placeholder fragment containing a simple view.
 */
public class CategoriesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener{
    private static final int CATEGORY_LIST_LOADER = 0;
    private static final String DEBUG_TAG = CategoriesFragment.class.getSimpleName();
    @BindView(R.id.categories_list_view) ListView mListView;
    private CategoryAdapter mAdapter;
    private Activity mActivity;
    private List<String> mCategoriesList;

    public CategoriesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mCategoriesList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, root);
        getLoaderManager().initLoader(CATEGORY_LIST_LOADER, null, this);
        mAdapter = new CategoryAdapter(mActivity, mCategoriesList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return root;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id){
            case CATEGORY_LIST_LOADER:{
                return new CursorLoader(mActivity, CatmeProvider.Categories.CONTENT_URI
                        ,null,null,null,null);
            }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()){
            do{
                String category = data.getString(
                        data.getColumnIndex(CatmeDatabase.CategoriesColumns.NAME));
                mCategoriesList.add(category);
            }while(data.moveToNext());

            mAdapter.notifyDataSetChanged();
        }else{
            Log.d(DEBUG_TAG, "categories empty!!");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String category = mCategoriesList.get(position);
        Log.d("test", category);
        Intent intent = new Intent(getActivity(), PictureActivity.class);
        intent.putExtra(PictureFragment.CATEGORY_ARG, category);
        startActivity(intent);
    }

    class CategoryAdapter extends ArrayAdapter<String>{
        @BindView(R.id.title_category_text_view)
        TextView titleCategory;
        public CategoryAdapter(Context context, List<String> list) {
            super(context, 0, list);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(mActivity)
                        .inflate(R.layout.row_category, parent, false);
            }
                ButterKnife.bind(this, convertView);
            String category = getItem(position);
            titleCategory.setText(category);
            titleCategory.setContentDescription(category);

            return convertView;
        }
    }
}
