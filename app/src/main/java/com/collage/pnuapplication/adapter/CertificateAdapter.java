package com.collage.pnuapplication.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.collage.pnuapplication.R;
import com.collage.pnuapplication.activity.AddCertificate;
import com.collage.pnuapplication.activity.CourseDetailActivity;
import com.collage.pnuapplication.model.CertificateModel;
import com.collage.pnuapplication.model.CourseModeel;
import com.collage.pnuapplication.utils.SharedPrefDueDate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.ViewHolder> {


    private Context context;
    ArrayList<CertificateModel> data;

    SharedPrefDueDate pref;

    public CertificateAdapter(Context context, ArrayList<CertificateModel> data) {
        this.context = context;
        this.data = data;


        pref = new SharedPrefDueDate(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_certificate, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Glide.with(context).load(data.get(position).getImage()).into(holder.image);
//        Glide.with(context).load(data.get(position).getImage()).into(holder.descImage);
//        holder.title.setText(data.get(position).getTitle());
//        holder.desc.setText(data.get(position).getDesc());

        getData(holder,data.get(position).getCourseId());
    }


    private void getData(ViewHolder holder , String courseId) {


        DatabaseReference df = FirebaseDatabase.getInstance().getReference();
        df.child("Course").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            CourseModeel model = snapshot.getValue(CourseModeel.class);


                            if (model.getId().equals(courseId)){
                                holder.courseNameTV.setText(model.getTitle());
                            }

//                            if (model.getUserId().equals(userId))
//                            data.add(model);

//                            adapter.notifyDataSetChanged();
                        }
                    }
//                    loading.setVisibility(View.GONE);

                } else {
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.image)
        ImageView image;

        @BindView(R.id.courseNameTV)
        TextView courseNameTV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

}
