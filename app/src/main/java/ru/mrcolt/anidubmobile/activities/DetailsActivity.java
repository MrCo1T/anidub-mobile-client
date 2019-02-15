package ru.mrcolt.anidubmobile.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import ru.mrcolt.anidubmobile.R;
import ru.mrcolt.anidubmobile.adapters.DetailsViewPagerAdapter;

public class DetailsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView titleToolbar;
    private Toolbar toolbar;
    private DetailsViewPagerAdapter detailsViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        try {
            initComponents();
            configComponents();
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initComponents() throws Exception {
        viewPager = findViewById(R.id.details_view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        toolbar = findViewById(R.id.details_toolbar);
        detailsViewPagerAdapter = new DetailsViewPagerAdapter(this, getSupportFragmentManager());
    }

    private void configComponents() {
        viewPager.setAdapter(detailsViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            Animatoo.animateSlideRight(this);
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Animatoo.animateSlideRight(this);
        return true;
    }
}
