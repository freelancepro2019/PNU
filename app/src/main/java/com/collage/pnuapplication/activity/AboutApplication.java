package com.collage.pnuapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.collage.pnuapplication.R;

public class AboutApplication extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);
    }

    public void Home(View view) {
        startActivity(new Intent(AboutApplication.this,HomeActivity.class));
    }
}
