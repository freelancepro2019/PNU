package com.collage.pnuapplication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.UserModel;
import com.collage.pnuapplication.share.Common;
import com.collage.pnuapplication.tags.Tags;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

public class SkillRecordViewProfileActivity extends AppCompatActivity {
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtPassword)
    EditText edtPassword;

    private DatabaseReference dRef;
    private FirebaseAuth mAuth;



    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_record_view_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        dRef = FirebaseDatabase.getInstance().getReference();

        toolBar.setNavigationOnClickListener(view -> finish());


    }

    @OnClick(R.id.btnView)
    public void ViewProfile() {

        if (!validate())
        {
            return;

        }


        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(authResult -> {
                    String user_id = authResult.getUser().getUid();
                    getUserById(dialog,user_id);
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            if (e.getMessage()!=null)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(this, R.string.failed, Toast.LENGTH_SHORT).show();
            }
        });




    }

    private boolean validate() {

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (!email.isEmpty()&&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()&&
                password.length()>=6
        )
        {

            Common.CloseKeyBoard(this,edtEmail);
            return true;

        }else
        {
            if (email.isEmpty())
            {
                Toast.makeText(this, R.string.email_req, Toast.LENGTH_SHORT).show();
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                Toast.makeText(this, R.string.inv_email, Toast.LENGTH_SHORT).show();

            }
            if (password.isEmpty())
            {
                Toast.makeText(this, R.string.pass_req, Toast.LENGTH_SHORT).show();

            }else if (password.length()<6)
            {
                Toast.makeText(this, R.string.pass_to_short, Toast.LENGTH_SHORT).show();

            }
            return false;
        }

    }

    private void getUserById(ProgressDialog dialog, String user_id) {
        dRef.child(Tags.table_user).child(user_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dialog.dismiss();
                        if (dataSnapshot.getValue()!=null)
                        {
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);

                            if (userModel!=null)
                            {

                                Intent intent=new Intent(SkillRecordViewProfileActivity.this,ProfileActivity.class);
                                intent.putExtra("name",userModel.getName());
                                intent.putExtra("email",userModel.getMail());
                                intent.putExtra("phone",userModel.getPhone());
                                startActivity(intent);
                            }


                        }else
                        {
                            Toast.makeText(SkillRecordViewProfileActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                    }
                });
    }



}

