package com.collage.pnuapplication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.databinding.ActivityCourseDetailsBinding;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.AttendanceModel;
import com.collage.pnuapplication.model.AttendanceUser;
import com.collage.pnuapplication.model.CourseModel;
import com.collage.pnuapplication.model.UserModel;
import com.collage.pnuapplication.preferences.Preferences;
import com.collage.pnuapplication.share.Common;
import com.collage.pnuapplication.tags.Tags;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class CourseDetailsActivity extends AppCompatActivity {
    private ActivityCourseDetailsBinding binding;
    private DatabaseReference dRef;
    private CourseModel courseModel =null;
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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_course_details);
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent!=null)
        {
            courseModel = (CourseModel) intent.getSerializableExtra("data");
        }
    }
    private void initView() {
        binding.setModel(courseModel);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        dRef = FirebaseDatabase.getInstance().getReference();
        binding.toolBar.setNavigationOnClickListener(view -> finish());
        binding.btnJoin.setOnClickListener(view -> join());
        Picasso.get().load(Uri.parse(courseModel.getImage())).fit().into(binding.image);

    }


    private void join()
    {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        dRef.child(Tags.table_attendance)
                .child(courseModel.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue()==null)
                        {

                            reserveCourse(courseModel,dialog);
                        }else
                        {

                            AttendanceModel attendanceModel = dataSnapshot.getValue(AttendanceModel.class);
                            if (attendanceModel!=null)
                            {
                                if (attendanceModel.getUsers()!=null&&attendanceModel.getUsers().size()>0)
                                {
                                    if (isUserReserved(attendanceModel.getUsers(),userModel.getId()))
                                    {
                                        dialog.dismiss();
                                        Toast.makeText(CourseDetailsActivity.this, R.string.user_reserved_course, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        AttendanceUser attendanceUser = new AttendanceUser(userModel.getId(),userModel.getName(),userModel.getMail());
                                        attendanceModel.getUsers().add(attendanceUser);
                                        updateReserveCourse(attendanceModel,dialog);
                                    }

                                }else
                                {

                                    AttendanceUser attendanceUser = new AttendanceUser(userModel.getId(),userModel.getName(),userModel.getMail());
                                    List<AttendanceUser> users = new ArrayList<>();
                                    users.add(attendanceUser);
                                    attendanceModel.setUsers(users);
                                    updateReserveCourse(attendanceModel,dialog);

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void reserveCourse(CourseModel courseModel,ProgressDialog dialog) {
        AttendanceUser attendanceUser = new AttendanceUser(userModel.getId(),userModel.getName(),userModel.getMail());
        List<AttendanceUser> users = new ArrayList<>();
        users.add(attendanceUser);
        AttendanceModel model = new AttendanceModel(courseModel.getId(),courseModel.getTitle(),courseModel.getImage(),users);
        dRef.child(Tags.table_attendance)
                .child(courseModel.getId())
                .setValue(model)
                .addOnSuccessListener(aVoid -> {
                    dialog.dismiss();
                    finish();

                }).addOnFailureListener(e -> {
            dialog.dismiss();
            if (e.getMessage()!=null)
            {
                Toast.makeText(CourseDetailsActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(CourseDetailsActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateReserveCourse(AttendanceModel attendanceModel,ProgressDialog dialog) {
        dRef.child(Tags.table_attendance)
                .child(attendanceModel.getCourse_id())
                .setValue(attendanceModel)
                .addOnSuccessListener(aVoid -> {
                    dialog.dismiss();
                    finish();

                }).addOnFailureListener(e -> {
            dialog.dismiss();
            if (e.getMessage()!=null)
            {
                Toast.makeText(CourseDetailsActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(CourseDetailsActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean isUserReserved(List<AttendanceUser> attendanceUserList,String user_id)
    {
        for (AttendanceUser user :attendanceUserList)
        {
            if (user.getId().equals(user_id))
            {
                return true;
            }


        }

        return false;
    }

}
