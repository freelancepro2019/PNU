package com.collage.pnuapplication.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.adapter.CourseAdapter;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.CourseModel;
import com.collage.pnuapplication.utils.SharedPrefDueDate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class CoursesActivity extends AppCompatActivity {


    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.titleTV)
    TextView titleTV;
    @BindView(R.id.recycle)
    RecyclerView recyclerView;
    @BindView(R.id.addBtn)
    FloatingActionButton addBtn;


    CourseAdapter adapter;
    ArrayList<CourseModel> data;


    String userImage;
    String userId;
    String type;



    SharedPrefDueDate pref;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        ButterKnife.bind(this);

        pref = new SharedPrefDueDate(this);



        if (pref.getUserType()==0) {
            addBtn.setVisibility(View.GONE);
        }else {
//            addBtn.setVisibility(View.VISIBLE);


        }


        userId = getIntent().getStringExtra("user");

        userImage = getIntent().getStringExtra("data");


        type = getIntent().getStringExtra("type");

        if (type!=null){
            titleTV.setText("قم باختيار الكوررس لرفع الشهادة");
        }


        data = new ArrayList<>();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);


        recyclerView.setLayoutManager(layoutManager);
        adapter = new CourseAdapter(this, data, userImage);

        recyclerView.setAdapter(adapter);

        getData();





        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CoursesActivity.this,AddCourse.class);
                i.putExtra("user",userId);
                startActivity(i);
            }
        });



    }


    private void getData() {

        loading.setVisibility(View.VISIBLE);

        data.clear();
        adapter.notifyDataSetChanged();
        DatabaseReference df = FirebaseDatabase.getInstance().getReference();
        df.child("Course").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            CourseModel model = snapshot.getValue(CourseModel.class);


//                            if (model.getUserId().equals(userId))
                                data.add(model);

                            adapter.notifyDataSetChanged();
                        }
                    }
                    loading.setVisibility(View.GONE);

                } else {
                    loading.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
            }
        });
    }

}
