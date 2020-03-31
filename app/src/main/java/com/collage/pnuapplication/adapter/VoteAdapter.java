package com.collage.pnuapplication.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.collage.pnuapplication.R;
import com.collage.pnuapplication.activity.CoursesActivity;
import com.collage.pnuapplication.model.ClubCollageModel;
import com.collage.pnuapplication.model.VoteModel;
import com.collage.pnuapplication.utils.SharedPrefDueDate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ViewHolder> {


    private Context context;
    ArrayList<ClubCollageModel> data;
    SharedPrefDueDate pref;

    public VoteAdapter(Context context, ArrayList<ClubCollageModel> data) {
        this.context = context;
        this.data = data;
        pref = new SharedPrefDueDate(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_vote, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Glide.with(context).load(data.get(position).getImage()).into(holder.image);

        holder.title.setText(data.get(position).getName());
        holder.desc.setText(data.get(position).getDesc());


        holder.rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                VoteModel model = new VoteModel();
                model.setId(random());
                model.setClubId(data.get(position).getId());
                model.setUserId(pref.getUserId());
                model.setVoteValue(rating);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("ClubVoting")
                        .child(model.getId()).setValue(model);
                Toast.makeText(context, "تم اضافة التقييم", Toast.LENGTH_LONG).show();
            }
        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(context, CoursesActivity.class);
//
//                i.putExtra("data",data.get(position).getImage());
//
//                i.putExtra("user",data.get(position).getId());
//
//
//
//                context.startActivity(i);
//            }
//        });


    }

    /**
     * to get ids for the firebase
     *
     * @return random string
     */
    protected String random() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

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

        @BindView(R.id.rate)
        RatingBar rate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

}
