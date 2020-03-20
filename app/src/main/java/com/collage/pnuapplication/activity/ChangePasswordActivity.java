package com.collage.pnuapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.language.LanguageHelper;

import io.paperdb.Paper;

public class ChangePasswordActivity extends AppCompatActivity {


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

    public void settings(View view) {
        startActivity(new Intent(ChangePasswordActivity.this,SettingsActivity.class));
    }
}
