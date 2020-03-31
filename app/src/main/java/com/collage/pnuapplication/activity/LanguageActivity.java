package com.collage.pnuapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.preferences.Preferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class LanguageActivity extends AppCompatActivity {

    private String selected_language = "ar";
    @BindView(R.id.rb_ar)
    RadioButton rbAr;
    @BindView(R.id.rb_en)
    RadioButton rbEn;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        Paper.init(this);
        preferences = Preferences.newInstance();

        if (preferences.langSelected(this))
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        rbAr.setOnClickListener(v -> selected_language = "ar"
        );

        rbEn.setOnClickListener(v -> selected_language = "en"
        );
        fab.setOnClickListener(v -> refreshActivity(selected_language)

        );
    }

    private void refreshActivity(String selected_language) {

        Paper.init(this);
        Paper.book().write("lang", selected_language);
        preferences.saveSelectedLanguage(this);
        LanguageHelper.setNewLocale(this, selected_language);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
