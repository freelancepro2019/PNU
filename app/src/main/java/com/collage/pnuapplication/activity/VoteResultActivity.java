package com.collage.pnuapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.adapter.VoteAdapter;
import com.collage.pnuapplication.adapter.VoteResultAdapter;
import com.collage.pnuapplication.model.ClubCollageModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteResultActivity extends AppCompatActivity {

    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.recycle)
    RecyclerView recyclerView;



    VoteResultAdapter adapter;
    ArrayList<ClubCollageModel> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        ButterKnife.bind(this);



        data = new ArrayList<>();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);


        recyclerView.setLayoutManager(layoutManager);
        adapter = new VoteResultAdapter(this, data);

        recyclerView.setAdapter(adapter);

        getData();


    }


    private void getData() {

        loading.setVisibility(View.VISIBLE);

        data.clear();
        adapter.notifyDataSetChanged();
        DatabaseReference df = FirebaseDatabase.getInstance().getReference();
        df.child("ClubCollage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            ClubCollageModel model = snapshot.getValue(ClubCollageModel.class);


                            if (model.getType().equals("2"))
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
