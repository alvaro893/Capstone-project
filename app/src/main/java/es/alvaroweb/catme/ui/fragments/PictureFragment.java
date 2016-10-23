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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.alvaroweb.catme.R;
import es.alvaroweb.catme.data.CatmeDatabase;
import es.alvaroweb.catme.data.CatmeProvider;
import es.alvaroweb.catme.helpers.ContentHelper;
import es.alvaroweb.catme.helpers.ImageHelper;
import es.alvaroweb.catme.helpers.NetworkHelper;
import es.alvaroweb.catme.model.Image;
import es.alvaroweb.catme.ui.MainActivity;
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
    private static final java.lang.String ID_IMAGE_KEY = "id";
    private static final int FAVORITE_LOADER = 0;
    private static final java.lang.String IS_FAVORITE_KEY = "is_favorite";
    @BindView(R.id.main_image_view) ImageView mainImage;
    @BindView(R.id.adView) AdView mAdView;
    @BindView(R.id.favorite_fab) FloatingActionButton mFavoriteFab;
    @BindView(R.id.liked) ImageView likeIcon;
    @BindView(R.id.not_liked) ImageView noLikedIcon;


    public static final String CATEGORY_ARG = "category";
    //private String mCurrentUrl = "";
    private Image mImage;
    private boolean mIsFavorite = false;
    private Loader<Cursor> mLoader;
    //private String mCurrentId = "";

    public PictureFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImage = new Image();
        if (savedInstanceState != null) {
            mImage.setUrl(savedInstanceState.getString(URL_IMAGE_KEY));
            mImage.setId(savedInstanceState.getString(ID_IMAGE_KEY));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_picture, container, false);
        ButterKnife.bind(this, root);
        setAds();
        mainImage.setOnTouchListener(new SwipeDismissTouchListener(mainImage, null, this));
        updateFavoriteFab();
        return root;
    }

    private void setAds() {

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mImage.getUrl().equals("")) {
            setImageFromSavedState();
        } else {
            setImage();
        }
    }

    private void setImageFromSavedState() {
        ImageHelper.setImage(getActivity(), mainImage, mImage.getUrl(), false);
        mLoader = getLoaderManager().restartLoader(FAVORITE_LOADER, null, PictureFragment.this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(URL_IMAGE_KEY, mImage.getUrl());
        outState.putString(ID_IMAGE_KEY, mImage.getId());
        outState.putBoolean(IS_FAVORITE_KEY, mIsFavorite);
        super.onSaveInstanceState(outState);
    }

    private void setImage() {
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

    private void initLoader() {
        if (mLoader != null) {
            getLoaderManager().destroyLoader(FAVORITE_LOADER);
        }
        mLoader = getLoaderManager().restartLoader(FAVORITE_LOADER, null, PictureFragment.this);
    }

    private String getCategory() {
        Bundle extras = getActivity().getIntent().getExtras();
        String category = extras.getString(CATEGORY_ARG);
        if (category != null && category.equals(MainActivity.NO_CATEGORY)) {
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
        setImage();
        updateFavoriteFab();
        Log.d(DEBUG_TAG, "x:" + lastPosition);
        if (lastPosition > 0f) {
            //swiped to right
            ContentHelper.setVote(getActivity().getContentResolver(), mImage, CatmeProvider.Images.VOTE_UP);
            animateIcon(likeIcon);

        } else {
            //swiped to left
            ContentHelper.setVote(getActivity().getContentResolver(), mImage, CatmeProvider.Images.VOTE_DOWN);
            animateIcon(noLikedIcon);
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
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            String favoriteValue = data.getString(data.getColumnIndex(
                    IS_FAVORITE));
            mIsFavorite = favoriteValue.equals(CatmeProvider.Images.FAVORITE_TRUE);
        } else {
            mIsFavorite = false;
        }
        updateFavoriteFab();

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
}
