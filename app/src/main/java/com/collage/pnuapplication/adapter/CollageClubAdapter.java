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
import com.collage.pnuapplication.activity.AddCourse;
import com.collage.pnuapplication.activity.CoursesActivity;
import com.collage.pnuapplication.model.ClubCollageModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CollageClubAdapter extends RecyclerView.Adapter<CollageClubAdapter.ViewHolder> {


    private Context context;
    ArrayList<ClubCollageModel> data;

    public CollageClubAdapter(Context context, ArrayList<ClubCollageModel> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_collage_club, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Glide.with(context).load(data.get(position).getImage()).into(holder.image);

        holder.title.setText(data.get(position).getName());
        holder.desc.setText(data.get(position).getDesc());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, CoursesActivity.class);

                i.putExtra("data",data.get(position).getImage());

                i.putExtra("user",data.get(position).getId());



                context.startActivity(i);
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
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.desc)
        TextView desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

}
