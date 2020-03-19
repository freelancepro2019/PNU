package com.collage.pnuapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.adapter.ViewPagerAdapter;
import com.collage.pnuapplication.fragmennt.AllFragment;
import com.collage.pnuapplication.fragmennt.ClubFragment;
import com.collage.pnuapplication.fragmennt.CollageFragment;
import com.collage.pnuapplication.fragmennt.DummyFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeLineActivity extends AppCompatActivity {



    //init the views
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        ButterKnife.bind(this);



        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void CCCIS(View view) {



    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllFragment(), "All");
        adapter.addFragment(new CollageFragment(), "Collage");
        adapter.addFragment(new ClubFragment(), "Club");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

    }

}
