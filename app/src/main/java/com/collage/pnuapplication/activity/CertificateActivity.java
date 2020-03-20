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
import com.collage.pnuapplication.adapter.CertificateAdapter;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.CertificateModel;
import com.collage.pnuapplication.tags.Tags;
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

public class CertificateActivity extends AppCompatActivity {


    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.tvNoCertificate)
    TextView tvNoCertificate;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.titleTV)
    TextView titleTV;
    @BindView(R.id.recycle)
    RecyclerView recyclerView;
    @BindView(R.id.addBtn)
    FloatingActionButton addBtn;
    private CertificateAdapter adapter;
    private ArrayList<CertificateModel> data;
    private DatabaseReference dRef;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificates);
        ButterKnife.bind(this);
        dRef = FirebaseDatabase.getInstance().getReference();
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(view -> finish());
        data = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CertificateAdapter(this, data);
        recyclerView.setAdapter(adapter);

        getData();


    }


    private void getData() {

        loading.setVisibility(View.VISIBLE);
        data.clear();
        adapter.notifyDataSetChanged();
        dRef.child(Tags.table_certificate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        CertificateModel model = ds.getValue(CertificateModel.class);

                        if (model!=null)
                        {
                            data.add(model);

                        }

                    }

                    if (data.size()>0)
                    {
                        adapter.notifyDataSetChanged();
                        tvNoCertificate.setVisibility(View.GONE);


                    }else
                        {
                            tvNoCertificate.setVisibility(View.VISIBLE);
                        }

                } else {
                    tvNoCertificate.setVisibility(View.VISIBLE);
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
