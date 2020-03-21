package com.collage.pnuapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.CourseModel;
import com.collage.pnuapplication.model.ReserveModel;
import com.collage.pnuapplication.utils.SharedPrefDueDate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

public class CoursePaymentActivity extends AppCompatActivity {


    @BindView(R.id.loading)
    ProgressBar loading;


    CourseModel model;
    SharedPrefDueDate pref;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_payment);
        ButterKnife.bind(this);


        model = (CourseModel) getIntent().getSerializableExtra("data");


        pref = new SharedPrefDueDate(this);









    }


    @OnClick(R.id.sendBtn)void sendAction(){
        ReserveModel m = new ReserveModel();


        loading.setVisibility(View.VISIBLE);
        m.setId(random());
        m.setTime(model.getTime());
        m.setCourse(model);
        m.setUserId(pref.getUserId());
        m.setCourseId(model.getId());




        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


        ref.child("Reserve")
                .child(m.getId()).setValue(m);


        model.setAvailable(model.getAvailable()-1);


        ref.child("Course")
                .child(model.getId()).setValue(model);
        Toast.makeText(CoursePaymentActivity.this, "تم الحجز بنجاح", Toast.LENGTH_LONG).show();

        finish();

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
