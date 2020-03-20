package com.collage.pnuapplication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.SuggestModel;
import com.collage.pnuapplication.model.UserModel;
import com.collage.pnuapplication.preferences.Preferences;
import com.collage.pnuapplication.share.Common;
import com.collage.pnuapplication.tags.Tags;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

public class SuggestActivity extends AppCompatActivity {


    @BindView(R.id.titleET)
    EditText titleET;
    @BindView(R.id.descET)
    EditText descET;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    private Preferences preferences;
    private UserModel userModel;
    private DatabaseReference dRef;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dRef = FirebaseDatabase.getInstance().getReference();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);

        toolBar.setNavigationOnClickListener(view -> finish());

    }


    @OnClick(R.id.sendBtn)
    void SendButtonAction() {


        if (titleET.getText().toString().isEmpty() || descET.getText().toString().isEmpty()) {

            Toast.makeText(this, R.string.pls_fill, Toast.LENGTH_LONG).show();
            return;
        }

        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        SuggestModel model = new SuggestModel();


        String id = dRef.child(Tags.table_suggest).push().getKey();
        model.setId(id);
        model.setTitle(titleET.getText().toString());
        model.setDesc(descET.getText().toString());
        model.setUserId(userModel.getId());
        model.setTime(System.currentTimeMillis() + "");


        dRef.child(Tags.table_suggest).child(model.getId()).setValue(model)
                .addOnSuccessListener(aVoid -> {
                    dialog.dismiss();
                    Toast.makeText(SuggestActivity.this, getString(R.string.success), Toast.LENGTH_LONG).show();
                    finish();

                }).addOnFailureListener(e -> {
                    dialog.dismiss();
            if (e.getMessage() != null) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

            }
        });


    }


}
