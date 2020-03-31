package com.collage.pnuapplication.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.databinding.AttendanceRowBinding;
import com.collage.pnuapplication.model.AttendanceUser;

import java.util.List;


public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {


    private Context context;
    private List<AttendanceUser> data;
    private LayoutInflater inflater;
    public AttendanceAdapter(Context context, List<AttendanceUser> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        AttendanceRowBinding binding = DataBindingUtil.inflate(inflater,R.layout.attendance_row,viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.binding.setModel(data.get(position));



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private AttendanceRowBinding binding;
        public ViewHolder(@NonNull AttendanceRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

}
