package com.collage.pnuapplication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.UserModel;
import com.collage.pnuapplication.preferences.Preferences;
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
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtPassword)
    EditText edtPassword;

    private DatabaseReference dRef;
    private FirebaseAuth mAuth;
    private Preferences preferences;



    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        dRef = FirebaseDatabase.getInstance().getReference();

        preferences = Preferences.newInstance();

        if (preferences.getSession(this).equals(Tags.session_login))
        {
            UserModel userModel = preferences.getUserData(this);

            if (userModel.getType().equals(Tags.student))
            {
                navigateToHomeActivity();
            }else if (userModel.getType().equals(Tags.admin))
            {
                navigateToHomeAdminActivity();
            }else if (userModel.getType().equals(Tags.admin_skill))
            {
                navigateToHomeAdminSkillActivity();
            }
        }

    }

    public void login(View view) {




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
                                preferences.create_update_userData(LoginActivity.this,userModel);

                                if (userModel.getType().equals(Tags.student))
                                {
                                    navigateToHomeActivity();
                                }else if (userModel.getType().equals(Tags.admin))
                                {
                                    navigateToHomeAdminActivity();
                                }else if (userModel.getType().equals(Tags.admin_skill))
                                {
                                    navigateToHomeAdminSkillActivity();
                                }
                            }
                        }else
                            {
                                Toast.makeText(LoginActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                    }
                });
    }

    private void navigateToHomeAdminActivity() {

        Intent intent = new Intent(this,HomeAdminActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToHomeAdminSkillActivity() {

        Intent intent = new Intent(this,HomeAdminSkillActivity.class);
        startActivity(intent);
        finish();
    }
    private void navigateToHomeActivity() {

        Intent intent = new Intent(this,HomeStudentActivity.class);
        startActivity(intent);
        finish();
    }



}
