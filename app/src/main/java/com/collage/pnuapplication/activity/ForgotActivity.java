package com.collage.pnuapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.collage.pnuapplication.R;

public class ForgotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
    }

    public void settings(View view) {
        startActivity(new Intent(ForgotActivity.this,SettingsActivity.class));
    }
}
