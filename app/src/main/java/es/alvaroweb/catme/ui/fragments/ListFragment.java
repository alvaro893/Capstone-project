package es.alvaroweb.catme.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.alvaroweb.catme.R;
import es.alvaroweb.catme.data.CatmeDatabase;
import es.alvaroweb.catme.data.CatmeProvider;
import es.alvaroweb.catme.helpers.ImageHelper;
import es.alvaroweb.catme.ui.PictureActivity;

import static butterknife.ButterKnife.bind;


/**
 * A placeholder fragment containing a simple view.
 */
public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String MODE_ARG = "mode";
    public static final String FAVORITES_MODE = "favorites";
    public static final String VOTE_MODE = "votes";
    private static final int LOADER = 0;
    private static final String DEBUG_TAG = ListFragment.class.getSimpleName();
    @BindView(R.id.recycle_view) RecyclerView mRecyclerView;

    public ListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        bind(this, root);
        getLoaderManager().initLoader(LOADER, getActivity().getIntent().getExtras(), this);
        return root;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String mode = args.getString(MODE_ARG);
        switch (mode){
            case FAVORITES_MODE:{
                String selection = CatmeDatabase.ImageColumns.IS_FAVORITE + " =?";
                String[] selectionArgs = new String[]{CatmeProvider.Images.FAVORITE_TRUE};
                return new CursorLoader(getActivity(),
                            CatmeProvider.Images.CONTENT_URI, null, selection, selectionArgs, null);
            }
            case VOTE_MODE:{
                String selection = CatmeDatabase.ImageColumns.VOTE + " IS NOT NULL";
                return new CursorLoader(getActivity(),
                        CatmeProvider.Images.CONTENT_URI, null, selection, null, null);
                 }
            default:{return null;}
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        AdapterList adapter = new AdapterList(data, getActivity());
        adapter.setHasStableIds(true);
        mRecyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }

    class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder>{

        private final Cursor mCursor;
        private final Activity mContext;

        AdapterList(Cursor data, Activity context) {
            this.mContext = context;
            this.mCursor = data;
        }

        @Override
        public long getItemId(int position) {
            if(mCursor.moveToPosition(position)){
                return mCursor.getLong(mCursor.getColumnIndex(CatmeDatabase.ImageColumns._ID));
            }
            return 0L;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mContext.getLayoutInflater().inflate(R.layout.list_item, parent, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Log.d(DEBUG_TAG, "Position on list: " + position);

            if(mCursor.moveToPosition(position)){
                int thumbnailCol = mCursor.getColumnIndex(CatmeDatabase.ImageColumns.THUMBNAIL);
                byte[] blob = mCursor.getBlob(thumbnailCol);
                if(blob != null){
                    ImageHelper.setImageFromBlob(mContext, blob, holder.thumbnail);
                }

                String vote = mCursor.getString(mCursor.getColumnIndex(CatmeDatabase.ImageColumns.VOTE));
                holder.voteIcon.setImageResource(getVotedImage(vote));

                holder.rootView.setOnClickListener(itemClick(holder));

            }
        }

        private View.OnClickListener itemClick(ViewHolder holder) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do something here
                    Intent intent = new Intent(getActivity(), PictureActivity.class);
                    intent.putExtra(PictureFragment.CATEGORY_ARG, PictureFragment.NO_CATEGORY);
                    startActivity(intent);
                }
            };
        }

        @Override
        public int getItemCount() {
            return mCursor.getCount();
        }


        class ViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.thumbnail) ImageView thumbnail;
            @BindView(R.id.vote_icon) ImageView voteIcon;
            @BindView(R.id.item_list_root) View rootView;


            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }



    private int getVotedImage(String vote) {
        if (vote == null){
            return R.drawable.ic_image_placeholder;
        }
        switch (vote){
            case CatmeProvider.Images.VOTE_UP:
                return R.drawable.ic_liked;
            case CatmeProvider.Images.VOTE_DOWN:
                return R.drawable.ic_no_liked;
            default:
                return R.drawable.ic_image_placeholder;
        }
    }
}
