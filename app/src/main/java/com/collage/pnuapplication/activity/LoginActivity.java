package com.collage.pnuapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.collage.pnuapplication.MainActivity;
import com.collage.pnuapplication.R;
import com.collage.pnuapplication.model.UserModel;
import com.collage.pnuapplication.utils.SharedPrefDueDate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {




    @BindView(R.id.emailET)
    EditText emailET;
    @BindView(R.id.passET)
    EditText passET;

    @BindView(R.id.loading)
    ProgressBar loading;


    SharedPrefDueDate pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        pref = new SharedPrefDueDate(this);
    }

    public void login(View view) {




        if (!validate())
            return;


        loading.setVisibility(View.VISIBLE);

        DatabaseReference df = FirebaseDatabase.getInstance().getReference();

        Query q = df.child("User").orderByChild("mail").equalTo(emailET.getText().toString());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(LoginActivity.this, "برجاء التأكد من الكود ", Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.GONE);
                    return;
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserModel model = snapshot.getValue(UserModel.class);
//                    Log.d("google","this is the value "+dataSnapshot/**/.getValue().toString());

                    loading.setVisibility(View.GONE);

                    if (model == null) {
                        Toast.makeText(LoginActivity.this, "برجاء التأكد من الكود ", Toast.LENGTH_LONG).show();
                    } else {
//                        Log.d("google","this is the password"+model.getId());
                        if (!model.getPassword().equals(passET.getText().toString())) {
                            Toast.makeText(LoginActivity.this, "برجاء التأكد من كلمة المرور ", Toast.LENGTH_LONG).show();
                        } else {
                            pref.setUserId(model.getId());


                            if (model.getType().equals("1")) {
                                pref.setUserType(0);
                                //todo here the user is student
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }else {
                                pref.setUserType(1);
                                //todo here the user is admin
                                Intent i = new Intent(LoginActivity.this, HomeAdminActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }

                        }
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        q.addValueEventListener(postListener);




    }


    private boolean validate() {
        if (emailET.getText().toString().isEmpty() || passET.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "من فضلك املئ جميع البيانات", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
