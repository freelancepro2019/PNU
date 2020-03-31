package com.collage.pnuapplication.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.preferences.Preferences;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;
import io.paperdb.Paper;

public class SkillStudentActivity extends AppCompatActivity {
    private CardView attachSkill,SkillRecord;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_student);
        ButterKnife.bind(this);

        initView();
    }

    private void initView()
    {
        Paper.init(this);
        attachSkill=findViewById(R.id.attachSkill);
        SkillRecord=findViewById(R.id.skillRecord);
        attachSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SkillStudentActivity.this, AttachCoursesActivity.class));

            }
        });

        SkillRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SkillStudentActivity.this, SkillsRecordContentsActivity.class));

            }
        });
    }


}
