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
import com.collage.pnuapplication.databinding.ActivityStudentCoursesBinding;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.AttendanceModel;
import com.collage.pnuapplication.model.AttendanceUser;
import com.collage.pnuapplication.model.CourseModel;
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

public class StudentCoursesActivity extends AppCompatActivity {
    private ActivityStudentCoursesBinding binding;
    private List<AttendanceModel> courseModelList;
    private StudentCourseAdapter adapter;
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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_student_courses);
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        dRef = FirebaseDatabase.getInstance().getReference();
        courseModelList = new ArrayList<>();
        adapter = new StudentCourseAdapter(this,courseModelList);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);

        getCourses();
    }

    private void getCourses() {

        dRef.child(Tags.table_attendance)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        binding.progBar.setVisibility(View.GONE);

                        if (dataSnapshot.getValue()!=null)
                        {
                            for (DataSnapshot ds :dataSnapshot.getChildren())
                            {
                                AttendanceModel model = ds.getValue(AttendanceModel.class);
                                if (model!=null)
                                {
                                    if (isStudentJoinCourse(model))
                                    {
                                        courseModelList.add(model);
                                    }
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

    private boolean isStudentJoinCourse(AttendanceModel model) {

        for (AttendanceUser user :model.getUsers())
        {
            if (user.getId().equals(userModel.getId()))
            {
                return true;
            }
        }
        return false;
    }

    public void setItemData(CourseModel courseModel) {
        Intent intent = new Intent(this,AttendanceActivity.class);
        intent.putExtra("data",courseModel);
        startActivity(intent);
    }
}
