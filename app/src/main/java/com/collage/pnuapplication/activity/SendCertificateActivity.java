package com.collage.pnuapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.AddCertificateModel;
import com.collage.pnuapplication.model.UserModel;
import com.collage.pnuapplication.preferences.Preferences;
import com.collage.pnuapplication.share.Common;
import com.collage.pnuapplication.tags.Tags;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class SendCertificateActivity extends AppCompatActivity {
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.imageCertificate)
    RoundedImageView imageCertificate;
    private DatabaseReference dRef;
    private Uri uri = null;
    private final String read_perm = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final int read_req = 1;
    private StorageReference sRef;
    private Preferences preferences;
    private UserModel userModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_certificate);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        sRef = FirebaseStorage.getInstance().getReference();
        dRef = FirebaseDatabase.getInstance().getReference();

        toolBar.setNavigationOnClickListener(view -> finish());


    }

    public void sendCertificate(View view) {

        if (!validate()) {
            return;

        }


        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        String email = edtEmail.getText().toString().trim();

        dRef.child(Tags.table_user)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {

                            UserModel userModel = null;
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                userModel = ds.getValue(UserModel.class);
                                if (userModel != null) {
                                    if (userModel.getMail().equals(email)) {
                                        break;
                                    }
                                }
                            }

                            if (userModel != null) {
                                uploadImage(dialog, userModel);
                            } else {
                                Toast.makeText(SendCertificateActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(SendCertificateActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                    }
                });

    }

    private void uploadImage(ProgressDialog dialog, UserModel userModel) {

        StorageReference ref = sRef.child("Course").child(UUID.randomUUID().toString()+ ".png");
        ref.putFile(uri).addOnSuccessListener(taskSnapshot -> {

            ref.getDownloadUrl().addOnSuccessListener(uri -> addCertificate(dialog,userModel,uri.toString()));

        }).addOnFailureListener(e -> {
            dialog.dismiss();
            if (e.getMessage()!=null)
            {
                Log.e("error",e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(this,getString(R.string.failed), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void addCertificate(ProgressDialog dialog, UserModel model, String url) {
        String id = dRef.child(Tags.table_certificate).child(model.getId()).push().getKey();
        String time = new SimpleDateFormat("dd/MMM/yyyy hh:mm aa", Locale.ENGLISH).format(new Date(Calendar.getInstance().getTimeInMillis()));

        AddCertificateModel certificateModel = new AddCertificateModel(id, userModel.getId(), userModel.getName(), url, time);

        dRef.child(Tags.table_certificate).child(model.getId()).child(id)
                .setValue(certificateModel)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            if (e.getMessage() != null) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private boolean validate() {

        String email = edtEmail.getText().toString().trim();

        if (!email.isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                uri != null
        ) {

            Common.CloseKeyBoard(this, edtEmail);
            return true;

        } else {
            if (email.isEmpty()) {
                Toast.makeText(this, R.string.email_req, Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, R.string.inv_email, Toast.LENGTH_SHORT).show();

            }

            if (uri == null) {
                Toast.makeText(this, getString(R.string.ch_image), Toast.LENGTH_SHORT).show();
            }
            return false;
        }

    }

    public void checkReadPermission(View view) {
        if (ContextCompat.checkSelfPermission(this, read_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{read_perm}, read_req);
        } else {
            selectImage(read_req);
        }
    }

    private void selectImage(int req) {

        Intent intent = new Intent();

        if (req == read_req) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }
            intent.setType("image/*");


        }


        startActivityForResult(intent, req);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == read_req) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED

            ) {
                selectImage(read_req);

            } else {
                Toast.makeText(this, "Permission to access image denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == read_req && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.getData();
            Log.e("uri",uri+"_");
            Picasso.get().load(uri).fit().into(imageCertificate);
        }

    }


}

