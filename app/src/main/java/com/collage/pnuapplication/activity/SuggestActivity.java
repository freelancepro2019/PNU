package com.collage.pnuapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.SuggestModel;
import com.collage.pnuapplication.utils.SharedPrefDueDate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

public class SuggestActivity extends AppCompatActivity {


    @BindView(R.id.titleET)
    EditText titleET;
    @BindView(R.id.descET)
    EditText descET;
    @BindView(R.id.loading)
    ProgressBar loading;


    SharedPrefDueDate pref;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        ButterKnife.bind(this);


        //init the storage
        pref = new SharedPrefDueDate(this);


    }


    @OnClick(R.id.sendBtn)
    void SendButtonAction() {


        if (titleET.getText().toString().isEmpty() || descET.getText().toString().isEmpty()) {

            Toast.makeText(this, "من فضلك قم بملئ جميع البيانات", Toast.LENGTH_LONG).show();
            return;
        }


        loading.setVisibility(View.VISIBLE);

        SuggestModel model = new SuggestModel();


        model.setId(random());
        model.setTitle(titleET.getText().toString());
        model.setDesc(descET.getText().toString());
        model.setUserId(pref.getUserId());
        model.setTime(System.currentTimeMillis() + "");


        Toast.makeText(SuggestActivity.this, "تم الارسال  بنجاح", Toast.LENGTH_LONG).show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


        ref.child("Suggest")
                .child(model.getId()).setValue(model);


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
