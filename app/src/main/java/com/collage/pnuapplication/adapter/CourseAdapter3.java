package com.collage.pnuapplication.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.databinding.CourseRow2Binding;
import com.collage.pnuapplication.model.CourseModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CourseAdapter3 extends RecyclerView.Adapter<CourseAdapter3.ViewHolder> {


    private Context context;
    private List<CourseModel> data;
    private LayoutInflater inflater;
    public CourseAdapter3(Context context, List<CourseModel> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CourseRow2Binding binding = DataBindingUtil.inflate(inflater,R.layout.course_row2,viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Picasso.get().load(data.get(position).getImage()).fit().into(holder.binding.image);
        holder.binding.setModel(data.get(position));

        holder.itemView.setOnClickListener(v -> {

        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CourseRow2Binding binding;
        public ViewHolder(@NonNull CourseRow2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

}
