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
import com.collage.pnuapplication.adapter.AttendanceAdapter;
import com.collage.pnuapplication.databinding.ActivityAttendanceBinding;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.AttendanceModel;
import com.collage.pnuapplication.model.AttendanceUser;
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

public class AttendanceActivity extends AppCompatActivity {
    private ActivityAttendanceBinding binding;
    private List<AttendanceUser> attendanceUserList;
    private AttendanceAdapter adapter;
    private DatabaseReference dRef;
    private CourseModel courseModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_attendance);
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("data"))
        {
            courseModel = (CourseModel) intent.getSerializableExtra("data");
        }
    }

    private void initView() {

        dRef = FirebaseDatabase.getInstance().getReference();
        attendanceUserList = new ArrayList<>();
        adapter = new AttendanceAdapter(this,attendanceUserList);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);

        getAttendance();
    }

    private void getAttendance() {

        dRef.child(Tags.table_attendance).child(courseModel.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        binding.progBar.setVisibility(View.GONE);

                        if (dataSnapshot.getValue()!=null)
                        {
                            try {
                                AttendanceModel model = dataSnapshot.getValue(AttendanceModel.class);
                                if (model!=null)
                                {
                                    attendanceUserList.addAll(model.getUsers());
                                }
                            }catch (Exception e)
                            {

                            }

                            if (attendanceUserList.size()>0)
                            {
                                adapter.notifyDataSetChanged();
                                binding.tvNoAttendance.setVisibility(View.GONE);

                            }else
                            {
                                binding.tvNoAttendance.setVisibility(View.VISIBLE);

                            }
                        }else
                        {
                            binding.tvNoAttendance.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
