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
import com.collage.pnuapplication.adapter.VoteAdapter;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.ClubCollageModel;
import com.collage.pnuapplication.tags.Tags;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class VoteActivity extends AppCompatActivity {

    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.tvNoVote)
    TextView tvNoVote;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.recycle)
    RecyclerView recyclerView;
    private VoteAdapter adapter;
    private ArrayList<ClubCollageModel> data;
    private DatabaseReference dRef;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();


    }

    private void initView() {
        dRef = FirebaseDatabase.getInstance().getReference();
        data = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new VoteAdapter(this, data);
        recyclerView.setAdapter(adapter);
        toolBar.setNavigationOnClickListener(view -> finish());
        getData();
    }


    private void getData() {

        data.clear();
        adapter.notifyDataSetChanged();
        dRef.child(Tags.table_club_collage).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                loading.setVisibility(View.GONE);

                if (dataSnapshot.getValue()!=null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ClubCollageModel model = snapshot.getValue(ClubCollageModel.class);

                        if (model!=null)
                        {
                            if (model.getType().equals("2"))
                                data.add(model);

                        }


                    }

                    if (data.size()>0)
                    {
                        tvNoVote.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();

                    }else
                    {
                        tvNoVote.setVisibility(View.VISIBLE);

                    }

                } else {
                    tvNoVote.setVisibility(View.VISIBLE);
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
