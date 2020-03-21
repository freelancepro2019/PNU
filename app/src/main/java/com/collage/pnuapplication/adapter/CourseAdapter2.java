package com.collage.pnuapplication.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.activity.AdminCoursesActivity;
import com.collage.pnuapplication.databinding.CourseRowBinding;
import com.collage.pnuapplication.model.CourseModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CourseAdapter2 extends RecyclerView.Adapter<CourseAdapter2.ViewHolder> {


    private Context context;
    private List<CourseModel> data;
    private LayoutInflater inflater;
    private AdminCoursesActivity activity;
    public CourseAdapter2(Context context, List<CourseModel> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        this.activity = (AdminCoursesActivity) context;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CourseRowBinding binding = DataBindingUtil.inflate(inflater,R.layout.course_row,viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Picasso.get().load(data.get(position).getImage()).fit().into(holder.binding.image);
        holder.binding.setModel(data.get(position));
        holder.itemView.setOnClickListener(v -> {
            CourseModel courseModel = data.get(holder.getAdapterPosition());
            activity.setItemData(courseModel);
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CourseRowBinding binding;
        public ViewHolder(@NonNull CourseRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

}
