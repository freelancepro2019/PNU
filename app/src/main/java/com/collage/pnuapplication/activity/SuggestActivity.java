package com.collage.pnuapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.model.CourseModeel;
import com.collage.pnuapplication.model.SuggestModel;
import com.collage.pnuapplication.utils.SharedPrefDueDate;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuggestActivity extends AppCompatActivity {


    @BindView(R.id.titleET)
    EditText titleET;
    @BindView(R.id.descET)
    EditText descET;
    @BindView(R.id.loading)
    ProgressBar loading;


    SharedPrefDueDate pref;

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
