package com.collage.pnuapplication.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.adapter.CollageClubAdapter;
import com.collage.pnuapplication.model.ClubCollageModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClubFragment extends Fragment {


    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.recycle)
    RecyclerView recyclerView;


    CollageClubAdapter adapter;
    ArrayList<ClubCollageModel> data;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frament_dummy, container, false);
        ButterKnife.bind(this, view);

        data = new ArrayList<>();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);


        recyclerView.setLayoutManager(layoutManager);
        adapter = new CollageClubAdapter(getContext(), data);

        recyclerView.setAdapter(adapter);
        getData();

        return view;
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
