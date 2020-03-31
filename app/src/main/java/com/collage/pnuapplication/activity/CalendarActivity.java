package com.collage.pnuapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.adapter.CourseAdapter3;
import com.collage.pnuapplication.language.LanguageHelper;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class CalendarActivity extends AppCompatActivity {

    @BindView(R.id.progBar)
    ProgressBar progBar;
    @BindView(R.id.tvNoCourse)
    TextView tvNoCourse;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.recView)
    RecyclerView recView;
    private CourseAdapter3 adapter;
    private List<CourseModel> data;
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
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        data = new ArrayList<>();
        dRef = FirebaseDatabase.getInstance().getReference();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter3(this, data);
        recView.setAdapter(adapter);

        toolBar.setNavigationOnClickListener(view -> finish());

        getData();


    }


    private void getData() {

        data.clear();
        adapter.notifyDataSetChanged();
        dRef.child(Tags.table_course).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        CourseModel model = ds.getValue(CourseModel.class);

                        if (model != null) {

                            if (model.getUserId().equals(userModel.getId()))
                            {
                                data.add(model);

                            }
                            if (data.size() > 0) {
                                adapter.notifyDataSetChanged();
                                tvNoCourse.setVisibility(View.GONE);
                            } else {
                                tvNoCourse.setVisibility(View.VISIBLE);

                            }
                        }


                    }
                    progBar.setVisibility(View.GONE);

                } else {
                    progBar.setVisibility(View.GONE);
                    tvNoCourse.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progBar.setVisibility(View.GONE);
            }
        });
    }

}
