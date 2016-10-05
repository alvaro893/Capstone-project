package es.alvaroweb.catme.ui.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.alvaroweb.catme.R;
import es.alvaroweb.catme.helpers.ImageHelper;
import es.alvaroweb.catme.helpers.NetworkHelper;
import es.alvaroweb.catme.ui.MainActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class PictureFragment extends Fragment {
    @BindView(R.id.main_image_view) ImageView mainImage;

    public static final String CATEGORY_ARG = "category";

    public PictureFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_picture, container, false);
        ButterKnife.bind(this, root);
        setImage();
        return root;
    }

    private void setImage(){
        String category = getCategory();
        NetworkHelper.loadImage(getActivity(), category, mainImage);
    }

    private String getCategory() {
        Bundle extras = getActivity().getIntent().getExtras();
        String category = extras.getString(CATEGORY_ARG);
        if(category != null && category.equals( MainActivity.NO_CATEGORY )){
            return null;
        }
        return category;
    }
}
