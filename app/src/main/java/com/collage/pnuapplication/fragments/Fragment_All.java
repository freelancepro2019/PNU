package com.collage.pnuapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.activity.CourseDetailsActivity;
import com.collage.pnuapplication.activity.TimeLineActivity;
import com.collage.pnuapplication.adapter.TimeLineAdapter;
import com.collage.pnuapplication.databinding.FragmentAllBinding;
import com.collage.pnuapplication.model.CourseModel;
import com.collage.pnuapplication.tags.Tags;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fragment_All extends Fragment {
    private TimeLineActivity activity;
    private FragmentAllBinding binding;
    private List<CourseModel> courseModelList;
    private TimeLineAdapter adapter;
    private DatabaseReference dRef;


    public static Fragment_All newInstance()
    {
        return new Fragment_All();
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

        Intent intent = new Intent(activity, CourseDetailsActivity.class);
        intent.putExtra("data",courseModel);
        startActivity(intent);
    }


}
