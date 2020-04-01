package com.collage.pnuapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.databinding.ActivityAboutApplicationBinding;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.tags.Tags;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import io.paperdb.Paper;

public class AboutApplication extends AppCompatActivity {
    private ActivityAboutApplicationBinding binding;
    private String lang;
    private DatabaseReference dRef;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_application);
        initView();
    }


    private void initView() {
        dRef = FirebaseDatabase.getInstance().getReference();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        getAppData();

    }

    private void getAppData() {
        DatabaseReference databaseReference;

        if (lang.equals("ar")) {
            databaseReference = dRef.child(Tags.table_setting).child(Tags.table_about).child("ar_about");

        } else {
            databaseReference = dRef.child(Tags.table_setting).child(Tags.table_about).child("en_about");

        }

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binding.progBar.setVisibility(View.GONE);

                if (dataSnapshot.getValue() != null) {
                    binding.setContent(dataSnapshot.getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
