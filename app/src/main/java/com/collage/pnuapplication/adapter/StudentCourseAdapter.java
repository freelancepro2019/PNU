package com.collage.pnuapplication.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.databinding.StudentCourseRowBinding;
import com.collage.pnuapplication.model.AttendanceModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class StudentCourseAdapter extends RecyclerView.Adapter<StudentCourseAdapter.ViewHolder> {


    private Context context;
    private List<AttendanceModel> data;
    private LayoutInflater inflater;
    public StudentCourseAdapter(Context context, List<AttendanceModel> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        StudentCourseRowBinding binding = DataBindingUtil.inflate(inflater,R.layout.student_course_row,viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Picasso.get().load(data.get(position).getCourse_image()).fit().into(holder.binding.image);
        holder.binding.setModel(data.get(position));

        holder.itemView.setOnClickListener(v -> {

        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private StudentCourseRowBinding binding;
        public ViewHolder(@NonNull StudentCourseRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

}
