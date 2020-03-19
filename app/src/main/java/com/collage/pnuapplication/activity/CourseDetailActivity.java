package com.collage.pnuapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.collage.pnuapplication.MainActivity;
import com.collage.pnuapplication.R;
import com.collage.pnuapplication.model.ClubCollageModel;
import com.collage.pnuapplication.model.CourseModeel;
import com.collage.pnuapplication.model.ReserveModel;
import com.collage.pnuapplication.utils.SharedPrefDueDate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseDetailActivity extends AppCompatActivity {


    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.titleTV)
    TextView titleTV;

    @BindView(R.id.descTV)
    TextView descTV;

    @BindView(R.id.dateTV)
    TextView dateTV;
    @BindView(R.id.locationTV)
    TextView locationTV;
    @BindView(R.id.availableTV)
    TextView availableTV;
    @BindView(R.id.numberTV)
    TextView numberTV;

    @BindView(R.id.priceTV)
    TextView priceTV;

    @BindView(R.id.bookBtn)
    Button bookBtn;
    @BindView(R.id.cancelBtn)
    Button cancelBtn;
    @BindView(R.id.loading)
    ProgressBar loading;


    CourseModeel model;
    SharedPrefDueDate pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        ButterKnife.bind(this);


        model = (CourseModeel) getIntent().getSerializableExtra("data");


        pref = new SharedPrefDueDate(this);




        if (pref.getUserType()==0) {
            bookBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
        }else {
            bookBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);


        }



        Glide.with(this).load(model.getImage()).into(image);

        titleTV.setText(model.getTitle());
        descTV.setText(model.getDesc());
        dateTV.setText(model.getTime());
        locationTV.setText(model.getLocation());
        availableTV.setText(model.getAvailable() + "");
        numberTV.setText(model.getNumbers() + "");
        priceTV.setText(model.getPrice() + " SAR");


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent i = new Intent(CourseDetailActivity.this,CoursePaymentActivity.class);


                i.putExtra("data",model);

                startActivity(i);
//                finish();
//                ReserveModel m = new ReserveModel();
//
//
//                loading.setVisibility(View.VISIBLE);
//                m.setId(random());
//                m.setTime(model.getTime());
//                m.setCourse(model);
//                m.setUserId(pref.getUserId());
//                m.setCourseId(model.getId());
//
//
//
//
//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//
//
//                ref.child("Reserve")
//                        .child(m.getId()).setValue(m);
//
//
//                model.setAvailable(model.getAvailable()-1);
//
//
//                ref.child("Course")
//                        .child(model.getId()).setValue(model);
//                Toast.makeText(CourseDetailActivity.this, "تم الحجز بنجاح", Toast.LENGTH_LONG).show();
//                finish();

//                Thread thread = new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            sleep(4000);
//                            loading.setVisibility(View.GONE);
//
//
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };


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
}
