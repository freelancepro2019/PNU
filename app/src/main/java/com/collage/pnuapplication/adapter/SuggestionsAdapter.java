package com.collage.pnuapplication.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.databinding.SuggestionRowBinding;
import com.collage.pnuapplication.model.SuggestModel;

import java.util.List;


public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.ViewHolder> {


    private Context context;
    private List<SuggestModel> data;
    private LayoutInflater inflater;
    public SuggestionsAdapter(Context context, List<SuggestModel> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SuggestionRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.suggestion_row,viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        holder.binding.setModel(data.get(position));

        holder.itemView.setOnClickListener(v -> {

        });




    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private SuggestionRowBinding binding;
        public ViewHolder(@NonNull SuggestionRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

}
