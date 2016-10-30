package es.alvaroweb.catme.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.alvaroweb.catme.R;
import es.alvaroweb.catme.data.CatmeDatabase;
import es.alvaroweb.catme.data.CatmeProvider;
import es.alvaroweb.catme.data.ImageLoader;
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
    private Callback mCallback;
    public static final String PICTURE_POS = "PICTURE_ID";
    @BindView(R.id.recycle_view) RecyclerView mRecyclerView;
    @BindView(R.id.no_pictures_text_view) TextView noPicturesText;

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
        getLoaderManager().initLoader(LOADER, getArguments(), this);
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mCallback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement callback");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String mode = args.getString(MODE_ARG);
        switch (mode){
            case FAVORITES_MODE: return ImageLoader.allFavoritesInstance(getActivity());
            case VOTE_MODE: return ImageLoader.allVotesInstance(getActivity());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        AdapterList adapter = new AdapterList(data, getActivity());
        adapter.setHasStableIds(true);
        mRecyclerView.setAdapter(adapter);
        if(adapter.getItemCount() < 1){
            switch (getArguments().getString(MODE_ARG)){
                case FAVORITES_MODE: noPicturesText.setText(R.string.no_favorites_warning); break;
                case VOTE_MODE: noPicturesText.setText(R.string.no_voted_pics_warning); break;
            }
            noPicturesText.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

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
        private int mLastPosition = -1;
        private float mOffset;

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
                switch (getArguments().getString(MODE_ARG)){
                    case VOTE_MODE: holder.voteIcon.setImageResource(getVotedImage(vote)); break;
                    case FAVORITES_MODE:
                        holder.voteIcon.setImageResource(R.drawable.ic_star_yellow_24dp); break;
                }


                setAnimation(holder.rootView, position);
                holder.rootView.setOnClickListener(itemClick(holder, position));
            }
        }

        private View.OnClickListener itemClick(ViewHolder holder, final int position) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // open PictureFragment
                    Bundle extras = getArguments();
                    extras.putString(PictureFragment.CATEGORY_ARG, PictureFragment.NO_CATEGORY);
                    extras.putInt(PICTURE_POS, position);
                    mCallback.itemClick(extras);
                }
            };
        }

        // this fix animation issues by setting original alpha and Y-translation
        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            holder.rootView.clearAnimation();
            holder.rootView.setAlpha(1f);
            holder.rootView.setTranslationY(0f);
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

        private void setAnimation(View viewToAnimate, int position){
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > mLastPosition){
                //The view has to be invisible for the animation
                //viewToAnimate.setVisibility(View.INVISIBLE);

                mOffset = 200f;
                Interpolator interpolator =
                        AnimationUtils.loadInterpolator(mContext, android.R.interpolator.decelerate_cubic);


                //Log.d(TAG, "position: " + position);
                //viewToAnimate.setVisibility(View.VISIBLE);
                viewToAnimate.setTranslationY(mOffset);
                viewToAnimate.setAlpha(0f);
                // then animate back to natural position
                viewToAnimate.animate()
                        .translationY(0f)
                        .alpha(1f)
                        .setInterpolator(interpolator)
                        .setDuration(1000L)
                        .start();
                // increase the offset distance for the next view
                mOffset *= 1.4f;

                mLastPosition = position;
            }
        }
    }
    public interface Callback{
        void itemClick(Bundle extras);
    }
}
