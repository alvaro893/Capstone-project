package es.alvaroweb.catme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.OnClick;
import es.alvaroweb.catme.CatMe;
import es.alvaroweb.catme.R;
import es.alvaroweb.catme.helpers.NetworkHelper;
import es.alvaroweb.catme.ui.fragments.ListFragment;
import es.alvaroweb.catme.ui.fragments.PictureFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CatMe)getApplication()).startTracking();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        if(savedInstanceState == null){
            NetworkHelper.loadCategories(this);
            //NetworkHelper.loadImagesVoteData(this);
        }
    }

    @OnClick(R.id.now_button)
    public void buttonNowClick(){
        Intent intent = new Intent(this, PictureActivity.class);
        intent.putExtra(PictureFragment.CATEGORY_ARG, PictureFragment.NO_CATEGORY);
        startActivity(intent);
    }

    @OnClick(R.id.categories_button)
    public void buttonCategoriesClick(){
        startActivity(new Intent(this, CategoriesActivity.class));
    }

    @OnClick(R.id.favorites_button)
    public void buttonFavoritesClick(){
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(ListFragment.MODE_ARG, ListFragment.FAVORITES_MODE);
        startActivity(intent);
    }

    @OnClick(R.id.vote_button)
    public void buttonVoteClick(){
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(ListFragment.MODE_ARG, ListFragment.VOTE_MODE);
        startActivity(intent);
    }

}
