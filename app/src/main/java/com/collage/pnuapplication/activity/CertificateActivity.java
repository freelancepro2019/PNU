package com.collage.pnuapplication.activity;

import android.annotation.SuppressLint;
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
import com.collage.pnuapplication.adapter.CertificateAdapter;
import com.collage.pnuapplication.adapter.CourseAdapter;
import com.collage.pnuapplication.model.CertificateModel;
import com.collage.pnuapplication.model.CourseModeel;
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

public class CertificateActivity extends AppCompatActivity {


    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.titleTV)
    TextView titleTV;
    @BindView(R.id.recycle)
    RecyclerView recyclerView;
    @BindView(R.id.addBtn)
    FloatingActionButton addBtn;


    CertificateAdapter adapter;
    ArrayList<CertificateModel> data;


    SharedPrefDueDate pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificates);
        ButterKnife.bind(this);

        pref = new SharedPrefDueDate(this);


        data = new ArrayList<>();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);


        recyclerView.setLayoutManager(layoutManager);
        adapter = new CertificateAdapter(this, data);

        recyclerView.setAdapter(adapter);

        getData();


    }


    private void getData() {

        loading.setVisibility(View.VISIBLE);
        data.clear();
        adapter.notifyDataSetChanged();
        DatabaseReference df = FirebaseDatabase.getInstance().getReference();
        df.child("Certificates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            CertificateModel model = snapshot.getValue(CertificateModel.class);

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
