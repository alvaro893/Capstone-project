package es.alvaroweb.catme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.OnClick;
import es.alvaroweb.catme.CatMe;
import es.alvaroweb.catme.R;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CatMe)getApplication()).startTracking();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.now_button)
    public void buttonNowClick(){
        startActivity(new Intent(this, PictureActivity.class));
    }

    @OnClick(R.id.categories_button)
    public void buttonCategoriesClick(){
        startActivity(new Intent(this, CategoriesActivity.class));
    }

    @OnClick(R.id.favorites_button)
    public void buttonFavoritesClick(){
        startActivity(new Intent(this, ListActivity.class).putExtra("test", "favorites"));
    }

    @OnClick(R.id.vote_button)
    public void buttonVoteClick(){
        startActivity(new Intent(this, ListActivity.class).putExtra("test", "votes"));
    }

}
