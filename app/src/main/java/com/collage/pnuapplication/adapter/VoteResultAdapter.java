package com.collage.pnuapplication.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.collage.pnuapplication.R;
import com.collage.pnuapplication.model.ClubCollageModel;
import com.collage.pnuapplication.model.VoteModel;
import com.collage.pnuapplication.tags.Tags;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VoteResultAdapter extends RecyclerView.Adapter<VoteResultAdapter.ViewHolder> {


    private Context context;
    ArrayList<ClubCollageModel> data;

    public VoteResultAdapter(Context context, ArrayList<ClubCollageModel> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_vote_resuly, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Glide.with(context).load(data.get(position).getImage()).into(holder.image);

        holder.title.setText(data.get(position).getName());
        holder.desc.setText(data.get(position).getDesc());





        getData(holder,data.get(position).getId());
    }



    private void getData(ViewHolder holder,String userId) {


        ArrayList<VoteModel> data =  new ArrayList<>();
        DatabaseReference df = FirebaseDatabase.getInstance().getReference();
        df.child(Tags.table_club_voting).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue()!=null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        VoteModel model = ds.getValue(VoteModel.class);

                        if (model!=null)
                        {
                            if (model.getClubId().equals(userId)){
                                data.add(model);
                            }
                        }

                    }

                    double totalVote = 0;
                    for (int  i =0 ; i<data.size();i++){
                       totalVote +=  data.get(i).getVoteValue();
                    }

                    double r = (totalVote/(data.size()-1));
                    String rate = String.format(Locale.ENGLISH,"%.2f",r);
                    holder.ratingTV.setText(rate);

                } else {
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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

        @BindView(R.id.ratingTV)
        TextView ratingTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

}
