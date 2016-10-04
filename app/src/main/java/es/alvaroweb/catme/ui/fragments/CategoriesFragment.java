package es.alvaroweb.catme.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.alvaroweb.catme.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


/**
 * A placeholder fragment containing a simple view.
 */
public class CategoriesFragment extends Fragment {
    @BindView(R.id.categories_list_view) ListView mListView;
    private ListAdapter mAdapter;
    private Activity mActivity;

    public CategoriesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, root);
        mAdapter = new CategoryAdapter(mActivity, new ArrayList<Category>());
        mListView.setAdapter(mAdapter);
        return root;
    }

    private class CategoryAdapter extends ArrayAdapter<Category>{

        public CategoryAdapter(Context context, List<Category> list) {
            super(context, 0, list);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return super.getView(position, convertView, parent);
        }
    }
}
