package es.alvaroweb.catme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.alvaroweb.catme.R;
import es.alvaroweb.catme.ui.fragments.ListFragment;
import es.alvaroweb.catme.ui.fragments.PictureFragment;

public class TabletActivity extends AppCompatActivity implements
        PictureFragment.Callback, ListFragment.Callback {

    private PictureFragment mPictureFragment;
    private ListFragment mListFragment;
    @BindInt(R.integer.screenCode) int screenCode;
    @BindView(R.id.go_small_fab) FloatingActionButton smallButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablet);
        ButterKnife.bind(this);
        if(screenCode < 1){
            Intent intent = getIntent();
            intent.setClass(this, ListActivity.class);
            startActivity(intent);
            finish();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(savedInstanceState == null){
            createPicureFragment();
            createListFragment();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    private Bundle createDefaultFragmentPictureArgs() {
        Bundle bundle = getIntent().getExtras();
        bundle.putInt(ListFragment.PICTURE_POS, 0);
        return bundle;
    }

    private void createListFragment() {
        mListFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.list_fragment_container, mListFragment)
                .commit();
        mListFragment.setArguments(getIntent().getExtras());
    }


    private void createPicureFragment() {
        Bundle defaultArgs = createDefaultFragmentPictureArgs();
        mPictureFragment = new PictureFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.picture_fragment_container, mPictureFragment)
                .commit();
        mPictureFragment.setArguments(defaultArgs);
    }

    private void setPicureFragment(Bundle extras) {
        mPictureFragment = new PictureFragment();
        mPictureFragment.setArguments(extras);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.picture_fragment_container, mPictureFragment)
                .commit();
    }

    @OnClick(R.id.go_small_fab)
    void fabButton(){
        Intent intent = getIntent();
        intent.setClass(this, ListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void itemClick(Bundle extras) {
        setPicureFragment(extras);
    }

}
