package com.collage.pnuapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.adapter.StudentCourseAdapter;
import com.collage.pnuapplication.adapter.SuggestionsAdapter;
import com.collage.pnuapplication.adapter.TimeLineAdapter;
import com.collage.pnuapplication.databinding.ActivityStudentCoursesBinding;
import com.collage.pnuapplication.databinding.ActivitySuggesionsBinding;
import com.collage.pnuapplication.databinding.ActivityTimeLineBinding;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.AttendanceModel;
import com.collage.pnuapplication.model.AttendanceUser;
import com.collage.pnuapplication.model.CourseModel;
import com.collage.pnuapplication.model.SuggestModel;
import com.collage.pnuapplication.model.UserModel;
import com.collage.pnuapplication.preferences.Preferences;
import com.collage.pnuapplication.tags.Tags;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class SuggesionsActivity extends AppCompatActivity {
    private ActivitySuggesionsBinding binding;
    private List<SuggestModel> suggestModelList;
    private SuggestionsAdapter adapter;
    private DatabaseReference dRef;
    private Preferences preferences;
    private UserModel userModel;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_suggesions);
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        dRef = FirebaseDatabase.getInstance().getReference();
        suggestModelList = new ArrayList<>();
        adapter = new SuggestionsAdapter(this,suggestModelList);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);

        getCourses();
    }

    private void getCourses() {

        dRef.child(Tags.table_suggest)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        binding.progBar.setVisibility(View.GONE);

                        if (dataSnapshot.getValue()!=null)
                        {
                            for (DataSnapshot ds :dataSnapshot.getChildren())
                            {
                                SuggestModel suggestModel = ds.getValue(SuggestModel.class);
                                if (suggestModel!=null)
                                {
                                    suggestModelList.add(suggestModel);
                                }
                            }

                            if (suggestModelList.size()>0)
                            {
                                adapter.notifyDataSetChanged();
                                binding.tvNoSuggestions.setVisibility(View.GONE);

                            }else
                            {
                                binding.tvNoSuggestions.setVisibility(View.VISIBLE);

                            }
                        }else
                        {
                            binding.tvNoSuggestions.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


}
