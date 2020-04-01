package com.collage.pnuapplication.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.activity.TimeLineActivity;
import com.collage.pnuapplication.adapter.TimeLineAdapter;
import com.collage.pnuapplication.databinding.FragmentAllBinding;
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

import java.util.ArrayList;
import java.util.List;

public class Fragment_Clubs extends Fragment {
    private TimeLineActivity activity;
    private FragmentAllBinding binding;
    private List<CourseModel> courseModelList;
    private TimeLineAdapter adapter;
    private DatabaseReference dRef;
    private Preferences preferences;
    private UserModel userModel;

    public static Fragment_Clubs newInstance()
    {
        return new Fragment_Clubs();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (TimeLineActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        dRef = FirebaseDatabase.getInstance().getReference();
        courseModelList = new ArrayList<>();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new TimeLineAdapter(activity,courseModelList,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
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
                                if (courseModel!=null&&courseModel.getCourse_category().equals(Tags.category_club))
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
        ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
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
                                        Toast.makeText(activity, R.string.user_reserved_course, Toast.LENGTH_SHORT).show();
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
                    activity.finish();

                }).addOnFailureListener(e -> {
            dialog.dismiss();
            if (e.getMessage()!=null)
            {
                Toast.makeText(activity,e.getMessage(), Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateReserveCourse(AttendanceModel attendanceModel,ProgressDialog dialog) {
        dRef.child(Tags.table_attendance)
                .child(attendanceModel.getCourse_id())
                .setValue(attendanceModel)
                .addOnSuccessListener(aVoid -> {
                    dialog.dismiss();
                    activity.finish();

                }).addOnFailureListener(e -> {
            dialog.dismiss();
            if (e.getMessage()!=null)
            {
                Toast.makeText(activity,e.getMessage(), Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
