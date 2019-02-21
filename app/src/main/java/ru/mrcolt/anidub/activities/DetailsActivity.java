package ru.mrcolt.anidub.activities;

import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import ru.mrcolt.anidub.R;
import ru.mrcolt.anidub.adapters.DetailsViewPagerAdapter;

public class DetailsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private DetailsViewPagerAdapter detailsViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initComponents();
        configComponents();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initComponents() {
        Fresco.initialize(this);
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
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
