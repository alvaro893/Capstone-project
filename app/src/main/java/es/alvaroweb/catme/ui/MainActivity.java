package es.alvaroweb.catme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.alvaroweb.catme.CatMe;
import es.alvaroweb.catme.R;
import es.alvaroweb.catme.helpers.ContentHelper;
import es.alvaroweb.catme.helpers.NetworkHelper;
import es.alvaroweb.catme.ui.fragments.ListFragment;
import es.alvaroweb.catme.ui.fragments.PictureFragment;


public class MainActivity extends AppCompatActivity {

    @BindInt(R.integer.screenCode) int mScreenCode;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    private boolean mIsBigScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CatMe)getApplication()).startTracking();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mIsBigScreen = mScreenCode > 0;
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
        Intent intent = new Intent(this, mIsBigScreen ? TabletActivity.class : ListActivity.class);
        intent.putExtra(ListFragment.MODE_ARG, ListFragment.FAVORITES_MODE);
        startActivity(intent);
    }

    @OnClick(R.id.vote_button)
    public void buttonVoteClick(){
        Intent intent = new Intent(this, mIsBigScreen ? TabletActivity.class : ListActivity.class);
        intent.putExtra(ListFragment.MODE_ARG, ListFragment.VOTE_MODE);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_data:
                ContentHelper.deleteImageData(getApplicationContext());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
