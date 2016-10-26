package es.alvaroweb.catme.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.alvaroweb.catme.R;
import es.alvaroweb.catme.helpers.ImageHelper;
import es.alvaroweb.catme.ui.fragments.PictureFragment;


public class PictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null){
            PictureFragment pictureFragment = new PictureFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_picture_id, pictureFragment)
            .commit();
            pictureFragment.setArguments(getIntent().getExtras());
        }
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
