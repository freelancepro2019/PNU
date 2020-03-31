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
import com.collage.pnuapplication.adapter.CourseAdapter2;
import com.collage.pnuapplication.databinding.ActivityAdminCoursesBinding;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.CourseModel;
import com.collage.pnuapplication.tags.Tags;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class AdminCoursesActivity extends AppCompatActivity {
    private ActivityAdminCoursesBinding binding;
    private List<CourseModel> courseModelList;
    private CourseAdapter2 adapter;
    private DatabaseReference dRef;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_admin_courses);
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {

        dRef = FirebaseDatabase.getInstance().getReference();
        courseModelList = new ArrayList<>();
        adapter = new CourseAdapter2(this,courseModelList);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);

        getCourses();
    }

    private void getCourses() {

        dRef.child(Tags.table_course)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        binding.progBar.setVisibility(View.GONE);

                        if (dataSnapshot.getValue()!=null)
                        {
                            for (DataSnapshot ds :dataSnapshot.getChildren())
                            {
                                CourseModel courseModel = ds.getValue(CourseModel.class);
                                if (courseModel!=null)
                                {
                                    courseModelList.add(courseModel);
                                }
                            }

                            if (courseModelList.size()>0)
                            {
                                adapter.notifyDataSetChanged();
                                binding.tvNoCourse.setVisibility(View.GONE);

                            }else
                                {
                                    binding.tvNoCourse.setVisibility(View.VISIBLE);

                                }
                        }else
                            {
                                binding.tvNoCourse.setVisibility(View.VISIBLE);
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void setItemData(CourseModel courseModel) {
        Intent intent = new Intent(this,AttendanceActivity.class);
        intent.putExtra("data",courseModel);
        startActivity(intent);
    }
}
