package es.alvaroweb.catme.ui.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import es.alvaroweb.catme.R;
import es.alvaroweb.catme.helpers.ImageHelper;
import es.alvaroweb.catme.model.Image;

import static butterknife.ButterKnife.bind;


/**
 * A placeholder fragment containing a simple view.
 */
public class ListFragment extends Fragment {
    @BindView(R.id.main_image_view)  ImageView mainImage;

    public static final String MODE_ARG = "mode";

    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        bind(this, root);
        ImageHelper.setImage(getActivity(), mainImage, "http://24.media.tumblr.com/tumblr_lknwy4X27H1qbvcv6o1_500.jpg",true);
        return root;
    }
}
