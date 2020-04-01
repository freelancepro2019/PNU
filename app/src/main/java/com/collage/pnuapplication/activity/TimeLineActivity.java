package com.collage.pnuapplication.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.adapter.ViewPagerAdapter;
import com.collage.pnuapplication.databinding.ActivityTimeLineBinding;
import com.collage.pnuapplication.fragments.Fragment_All;
import com.collage.pnuapplication.fragments.Fragment_Clubs;
import com.collage.pnuapplication.fragments.Fragment_Collage;
import com.collage.pnuapplication.language.LanguageHelper;

import io.paperdb.Paper;

public class TimeLineActivity extends AppCompatActivity {
    private ActivityTimeLineBinding binding;
    private ViewPagerAdapter adapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_time_line);
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(Fragment_All.newInstance(),getString(R.string.all));
        adapter.addFragment(Fragment_Clubs.newInstance(),getString(R.string.clubs));
        adapter.addFragment(Fragment_Collage.newInstance(),getString(R.string.collage));

        binding.tab.setupWithViewPager(binding.pager);
        binding.pager.setOffscreenPageLimit(adapter.getCount());
        binding.pager.setAdapter(adapter);
    }



}
