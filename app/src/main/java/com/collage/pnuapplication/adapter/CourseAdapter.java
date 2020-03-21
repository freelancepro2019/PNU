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
import com.collage.pnuapplication.model.CourseModel;
import com.collage.pnuapplication.utils.SharedPrefDueDate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {


    private Context context;
    ArrayList<CourseModel> data;
    String userImage;

    SharedPrefDueDate pref;

    public CourseAdapter(Context context, ArrayList<CourseModel> data, String userImage) {
        this.context = context;
        this.data = data;

        this.userImage = userImage;

        pref = new SharedPrefDueDate(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_course, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Glide.with(context).load(userImage).into(holder.image);
        Glide.with(context).load(data.get(position).getImage()).into(holder.descImage);
        holder.title.setText(data.get(position).getTitle());
        holder.desc.setText(data.get(position).getDesc());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (pref.getUserType()==0) {
                    Intent i = new Intent(context, CourseDetailActivity.class);
                    i.putExtra("data", data.get(position));
                    context.startActivity(i);
                }else {
                    Intent i = new Intent(context, AddCertificate.class);
                    i.putExtra("course", data.get(position).getId());
                    context.startActivity(i);
                }
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
        @BindView(R.id.descImage)
        ImageView descImage;
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
