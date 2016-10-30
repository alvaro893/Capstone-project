package es.alvaroweb.catme.ui.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.alvaroweb.catme.R;
import es.alvaroweb.catme.data.CatmeDatabase;
import es.alvaroweb.catme.data.CatmeProvider;
import es.alvaroweb.catme.data.ImageLoader;
import es.alvaroweb.catme.helpers.ContentHelper;
import es.alvaroweb.catme.helpers.ImageHelper;
import es.alvaroweb.catme.helpers.NetworkHelper;
import es.alvaroweb.catme.model.Image;
import es.alvaroweb.catme.ui.custom.SwipeDismissTouchListener;

import static es.alvaroweb.catme.data.CatmeDatabase.ImageColumns.IS_FAVORITE;


/**
 * A placeholder fragment containing a simple view.
 */
public class PictureFragment extends Fragment implements
        SwipeDismissTouchListener.DismissCallbacks,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String DEBUG_TAG = PictureFragment.class.getSimpleName();
    private static final String URL_IMAGE_KEY = "url";
    private static final String ID_IMAGE_KEY = "id";
    private static final int FAVORITE_LOADER = 0;
    private static final String IS_FAVORITE_KEY = "is_favorite";
    private static final int CURSOR_LOADER = 1;
    private static final String VOTING_MODE = "votting_mode";
    private static final String GALLERY_MODE = "gallery_mode";
    private static final String LAST_CURSOR_POSITION = "position";
    @BindView(R.id.main_image_view) ImageView mainImage;
    @BindView(R.id.adView) AdView mAdView;
    @BindView(R.id.favorite_fab) FloatingActionButton mFavoriteFab;
    @BindView(R.id.liked) ImageView mLikeIcon;
    @BindView(R.id.not_liked) ImageView mNoLikedIcon;
    @BindView(R.id.info_bar) View mInfoBar;


    public static final String CATEGORY_ARG = "category";
    public static final String NO_CATEGORY = "none";
    private Image mImage;
    private boolean mIsFavorite = false;
    private Loader<Cursor> mLoader;
    private Cursor mCursor;
    private int mLastPosition = -1;
    private String mMode = VOTING_MODE; // default
    private Toast noMorePicsToast;

    public PictureFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImage = new Image();
        setMode();

        if (savedInstanceState != null) {
            mImage.setUrl(savedInstanceState.getString(URL_IMAGE_KEY));
            mImage.setId(savedInstanceState.getString(ID_IMAGE_KEY));
            mLastPosition = savedInstanceState.getInt(LAST_CURSOR_POSITION);
        }
    }

    private void setMode() {
        mLastPosition = getArguments().getInt(ListFragment.PICTURE_POS, -1);
        if (mLastPosition != -1) {
            mMode = GALLERY_MODE;
            getLoaderManager().initLoader(CURSOR_LOADER, null, this);
        }else{
            mMode = VOTING_MODE;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_picture, container, false);
        ButterKnife.bind(this, root);
        setAds();
        mainImage.setOnTouchListener(new SwipeDismissTouchListener(mainImage, null, this));
        mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageFromSavedState();
            }
        });
        updateFavoriteFab();
        noMorePicsToast = Toast.makeText(getActivity(), R.string.no_more, Toast.LENGTH_SHORT);
        return root;
    }

    private void setAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (mMode){
            case VOTING_MODE:{
                exitOnNetworkFailure(true);
                if (mImage.getUrl().equals("")) {
                    setImageFromNetwork();
                } else {
                    setImageFromSavedState();
                }
                mInfoBar.setVisibility(View.VISIBLE);
                break;
            }
            case GALLERY_MODE:{
                setImageFromSavedState();
                mInfoBar.setVisibility(View.GONE);
                break;
            }
        }
       exitOnNetworkFailure(false);
    }

    private void setImageFromSavedState() {
        ImageHelper.setImage(getActivity(), mainImage, mImage.getUrl(), false);
        mainImage.setVisibility(View.VISIBLE);
        mLoader = getLoaderManager().restartLoader(FAVORITE_LOADER, null, PictureFragment.this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(URL_IMAGE_KEY, mImage.getUrl());
        outState.putString(ID_IMAGE_KEY, mImage.getId());
        outState.putBoolean(IS_FAVORITE_KEY, mIsFavorite);
        outState.putInt(LAST_CURSOR_POSITION, mLastPosition);
        super.onSaveInstanceState(outState);
    }

    private void setImageFromNetwork() {
        exitOnNetworkFailure(true);
        String category = getCategory();
        NetworkHelper.loadImage(getActivity(), category, mainImage, new NetworkHelper.OnLoaded() {
            @Override
            public void loaded(String url, String id) {
                mainImage.setVisibility(View.VISIBLE);
                mImage.setUrl(url);
                mImage.setId(id);
                initLoader();
            }
        });

    }

    private void exitOnNetworkFailure(boolean exit){
        if( !NetworkHelper.isNetworkAvailable(getContext()) ){
            Toast.makeText(getContext(), R.string.no_network_warning, Toast.LENGTH_LONG).show();
            if(exit)
                getActivity().finish();
        }
    }

    private void initLoader() {
        if (mLoader != null) {
            getLoaderManager().destroyLoader(FAVORITE_LOADER);
        }
        mLoader = getLoaderManager().restartLoader(FAVORITE_LOADER, null, PictureFragment.this);
    }

    private String getCategory() {
        Bundle extras = getArguments();
        String category = extras.getString(CATEGORY_ARG);
        if (category != null && category.equals(NO_CATEGORY)) {
            return null;
        }
        return category;
    }

    // image dismissable code
    @Override
    public boolean canDismiss(Object token) {
        return true;
    }

    @Override
    public void onDismiss(View view, Object token, float lastPosition) {
        mainImage.setVisibility(View.INVISIBLE);
        boolean swipedToRight = lastPosition > 0f;

        switch (mMode){
            case VOTING_MODE:{
                setImageFromNetwork();
                ContentHelper.insertThumbnail(getActivity(), mImage);
                if(swipedToRight){
                    ContentHelper.setVote(getActivity(), mImage, CatmeProvider.Images.VOTE_DOWN);
                    animateIcon(mNoLikedIcon);
                }else{
                    ContentHelper.setVote(getActivity(), mImage, CatmeProvider.Images.VOTE_UP);
                    animateIcon(mLikeIcon);
                }
                break;
            }
            case GALLERY_MODE:{
                if(swipedToRight){
                    movecursorToPrevious();
                }else{
                    moveCursorToNext();
                }
                break;
            }
        }
    }
    // image dismissable code END

    @OnClick(R.id.favorite_fab)
    void favoriteFabClick() {
        String favoriteValue;
        // switch values
        if (mIsFavorite = !mIsFavorite) {
            favoriteValue = CatmeProvider.Images.FAVORITE_TRUE;
        } else {
            favoriteValue = CatmeProvider.Images.FAVORITE_FALSE;
        }
        ContentHelper.insertThumbnail(getActivity(), mImage);
        ContentHelper.setFavorite(getActivity().getContentResolver(),
                mImage.getId(), mImage.getUrl(), favoriteValue);

    }

    @OnClick(R.id.share_fab)
    void shareFabClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("image/jpeg");
        intent.setData(Uri.parse(mImage.getUrl()));
        startActivity(Intent.createChooser(intent, "Share image to.."));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case FAVORITE_LOADER: {
                String selection = CatmeDatabase.ImageColumns.API_ID + "=?";
                String[] arguments = new String[]{mImage.getId()};
                return new CursorLoader(getActivity(),
                        CatmeProvider.Images.withApiId(mImage.getId()), null, selection, arguments, null);
            }
            case CURSOR_LOADER: {
                String ListFragmentMode = getArguments().getString(ListFragment.MODE_ARG);
                switch (ListFragmentMode) {
                    case ListFragment.FAVORITES_MODE:
                        getActivity().setTitle(R.string.list_activity_title_fav);
                        return ImageLoader.allFavoritesInstance(getContext());
                    case ListFragment.VOTE_MODE:
                        getActivity().setTitle(R.string.list_activity_title_votes);
                        return ImageLoader.allVotesInstance(getContext());
                }
            }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(DEBUG_TAG, "loader id: " + loader.getId() + " finished");
        switch (loader.getId()) {
            case FAVORITE_LOADER: {
                if (data != null && data.moveToFirst()) {
                    String favoriteValue = data.getString(data.getColumnIndex(IS_FAVORITE));
                    mIsFavorite = favoriteValue.equals(CatmeProvider.Images.FAVORITE_TRUE);
                } else {
                    mIsFavorite = false;
                }
                updateFavoriteFab();
                break;
            }
            case CURSOR_LOADER: {
                setCursor(data);
                break;
            }
        }


    }

    private void setCursor(Cursor data) {
        if (data != null) {
            mCursor = data;
            if (mCursor.moveToPosition(mLastPosition)) {
                setImageFromCursor();
            }

        }
    }

    private void setImageFromCursor() {
        mImage.setUrl(mCursor.getString(ImageLoader.Query.URL));
        mImage.setId(mCursor.getString(ImageLoader.Query.API_ID));
        setImageFromSavedState();
    }

    private void moveCursorToNext(){
        if(mCursor.moveToNext()){
            setImageFromCursor();
            mLastPosition = mCursor.getPosition();
        }else{
            mCursor.moveToPosition(mLastPosition);
            mainImage.setVisibility(View.VISIBLE);
            noMorePicsToast.show();
        }
    }

    private void movecursorToPrevious(){
        if(mCursor.moveToPrevious()){
            setImageFromCursor();
            mLastPosition = mCursor.getPosition();
        }else{
            mCursor.moveToPosition(mLastPosition);
            mainImage.setVisibility(View.VISIBLE);
            noMorePicsToast.show();
        }
    }

    private void updateFavoriteFab() {
        if (mIsFavorite) {
            mFavoriteFab.setImageResource(R.drawable.ic_star_yellow_24dp);
        } else {
            mFavoriteFab.setImageResource(R.drawable.ic_start_white_24dp);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void animateIcon(View viewToAnimate) {

        viewToAnimate.setVisibility(View.VISIBLE);

        final float offset0 = 200f;
        final float offset1 = 0f;
        final float offset2 = -300f;
        Interpolator interpolator =
                AnimationUtils.loadInterpolator(getActivity(), android.R.interpolator.decelerate_quad);
        Interpolator interpolator2 =
                AnimationUtils.loadInterpolator(getActivity(), android.R.interpolator.accelerate_quad);

        //initial conditions
        viewToAnimate.setTranslationY(offset0);
        viewToAnimate.setAlpha(1f);

        // acelerate animation from 200 to 0 to -3000
        viewToAnimate.animate()
                .translationY(offset1)
                .setInterpolator(interpolator)
                .setDuration(250L)
                .start();

        viewToAnimate.setTranslationY(0f);

        viewToAnimate.animate()
                .translationY(offset2)
                .alpha(0f)
                .setInterpolator(interpolator2)
                .setDuration(250L)
                .start();
    }

    public interface Callback{

    }
}
