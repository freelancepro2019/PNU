package com.collage.pnuapplication.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.databinding.ActivityAboutApplicationBinding;
import com.collage.pnuapplication.databinding.ActivityProfileBinding;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.preferences.Preferences;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private String lang;
    private String name,phone,email;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        getDataFromIntent();
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.tvName.setText(name);
        binding.tvEmail.setText(email);
        binding.tvPhone.setText(phone);


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("email")) {
            name=intent.getStringExtra("name");
            email=intent.getStringExtra("email");
            phone=intent.getStringExtra("phone");

        }

    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
