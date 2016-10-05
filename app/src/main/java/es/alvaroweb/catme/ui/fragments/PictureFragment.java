package es.alvaroweb.catme.ui.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.alvaroweb.catme.R;
import es.alvaroweb.catme.helpers.ImageHelper;
import es.alvaroweb.catme.helpers.NetworkHelper;
import es.alvaroweb.catme.ui.MainActivity;
import es.alvaroweb.catme.ui.custom.SwipeDismissTouchListener;


/**
 * A placeholder fragment containing a simple view.
 */
public class PictureFragment extends Fragment implements SwipeDismissTouchListener.DismissCallbacks {
    private static final String DEBUG_TAG = PictureFragment.class.getSimpleName();
    private static final String URL_IMAGE_KEY = "url";
    @BindView(R.id.main_image_view) ImageView mainImage;

    public static final String CATEGORY_ARG = "category";
    private String mCurrentUrl = "";

    public PictureFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            mCurrentUrl = savedInstanceState.getString(URL_IMAGE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_picture, container, false);
        ButterKnife.bind(this, root);
        mainImage.setOnTouchListener(new SwipeDismissTouchListener(mainImage, null, this));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mCurrentUrl.equals("")){
            setImageFromSavedState();
        }else{
            setImage();
        }
    }

    private void setImageFromSavedState() {
        ImageHelper.setImage(getActivity(), mainImage, mCurrentUrl, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(URL_IMAGE_KEY, mCurrentUrl);
        super.onSaveInstanceState(outState);
    }

    private void setImage(){
        String category = getCategory();
        NetworkHelper.loadImage(getActivity(), category, mainImage, new NetworkHelper.OnLoaded() {
            @Override
            public void loaded(String url) {
                mainImage.setVisibility(View.VISIBLE);
                mCurrentUrl = url;
            }
        });

    }

    private String getCategory() {
        Bundle extras = getActivity().getIntent().getExtras();
        String category = extras.getString(CATEGORY_ARG);
        if(category != null && category.equals( MainActivity.NO_CATEGORY )){
            return null;
        }
        return category;
    }

    @Override
    public boolean canDismiss(Object token) {
        return true;
    }

    @Override
    public void onDismiss(View view, Object token, float lastPosition) {
        mainImage.setVisibility(View.INVISIBLE);
        setImage();
        Log.d(DEBUG_TAG, "x:" + lastPosition);
        if(lastPosition > 0f){
            Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "left", Toast.LENGTH_SHORT).show();

        }

    }
}
