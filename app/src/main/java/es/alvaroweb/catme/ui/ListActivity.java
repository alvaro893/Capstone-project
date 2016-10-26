package es.alvaroweb.catme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.alvaroweb.catme.R;
import es.alvaroweb.catme.ui.fragments.ListFragment;

import static android.R.attr.mode;


public class ListActivity extends AppCompatActivity implements ListFragment.Callback {

    private ListFragment mListFragment;
    @BindInt(R.integer.screenCode) int screenCode;
    @BindView(R.id.go_big_fab) FloatingActionButton bigButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(screenCode < 1){
            bigButton.setVisibility(View.GONE);
        }

        String mode = getIntent().getStringExtra(ListFragment.MODE_ARG);
        switch (mode){
            case ListFragment.FAVORITES_MODE:
                setTitle(getString(R.string.list_activity_title_fav));
                break;
            case ListFragment.VOTE_MODE:
                setTitle(getString(R.string.list_activity_title_votes));
                break;
        }

        if(savedInstanceState == null){
            mListFragment = new ListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.list_fragment_container, mListFragment)
                    .commit();
            mListFragment.setArguments(getIntent().getExtras());
        }
    }

    @Override
    public void itemClick(Bundle extras) {
        Intent intent = new Intent(this, PictureActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @OnClick(R.id.go_big_fab)
    void clickFab(){
        Intent intent = getIntent();
        intent.setClass(this, TabletActivity.class);
        startActivity(intent);
        finish();
    }
}
