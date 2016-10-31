package es.alvaroweb.catme.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import es.alvaroweb.catme.R;
import es.alvaroweb.catme.ui.fragments.PictureFragment;


public class PictureActivity extends AppCompatActivity {

    protected PictureFragment mPictureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null){
            createPictureFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_picture_id, mPictureFragment)
            .commit();
            mPictureFragment.setArguments(getIntent().getExtras());
        }
    }

    protected void createPictureFragment(){
        mPictureFragment = new PictureFragment();
    }

    /* Make up button behave like back button*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }
}
