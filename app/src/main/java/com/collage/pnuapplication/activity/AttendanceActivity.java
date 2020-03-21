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
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.adapter.AttendanceAdapter;
import com.collage.pnuapplication.databinding.ActivityAttendanceBinding;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.AddCertificateModel;
import com.collage.pnuapplication.model.AttendanceModel;
import com.collage.pnuapplication.model.AttendanceUser;
import com.collage.pnuapplication.model.CourseModel;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import io.paperdb.Paper;

public class AttendanceActivity extends AppCompatActivity {
    private ActivityAttendanceBinding binding;
    private List<AttendanceUser> attendanceUserList;
    private AttendanceAdapter adapter;
    private DatabaseReference dRef;
    private CourseModel courseModel;
    private Uri uri = null;
    private final String read_perm = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final int read_req = 1;
    private StorageReference sRef;
    private int index = 0;
    private String imageUrl = "";
    private ProgressDialog dialog;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_attendance);
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("data")) {
            courseModel = (CourseModel) intent.getSerializableExtra("data");
        }
    }

    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        sRef = FirebaseStorage.getInstance().getReference();
        dRef = FirebaseDatabase.getInstance().getReference();
        attendanceUserList = new ArrayList<>();
        adapter = new AttendanceAdapter(this, attendanceUserList);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);

        binding.llChooseImage.setOnClickListener(view -> checkReadPermission());
        binding.tvUpload.setOnClickListener(view -> {
            uploadImage();
        });
        getAttendance();
    }

    private void uploadImage() {

        dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StorageReference ref = sRef.child("Course").child(UUID.randomUUID().toString() + ".png");
        ref.putFile(uri).addOnSuccessListener(taskSnapshot -> {

            ref.getDownloadUrl().addOnSuccessListener(uri -> {
                imageUrl = uri.toString();
                sendCertificate();
            });

        }).addOnFailureListener(e -> {
            dialog.dismiss();
            if (e.getMessage() != null) {
                Log.e("error", e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void sendCertificate() {

        String student_id = attendanceUserList.get(index).getId();

        String id = dRef.child(Tags.table_certificate).child(student_id).push().getKey();

        String time = new SimpleDateFormat("dd/MMM/yyyy hh:mm aa", Locale.ENGLISH).format(new Date(Calendar.getInstance().getTimeInMillis()));

        AddCertificateModel certificateModel = new AddCertificateModel(id, userModel.getId(), userModel.getName(), imageUrl, time);

        dRef.child(Tags.table_certificate).child(student_id).child(id)
                .setValue(certificateModel)
                .addOnSuccessListener(aVoid -> {
                    index++;
                    if (index<attendanceUserList.size())
                    {
                        sendCertificate();
                    }else
                        {
                            dialog.dismiss();
                            Toast.makeText(this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            if (e.getMessage() != null) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void getAttendance() {

        dRef.child(Tags.table_attendance).child(courseModel.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        binding.progBar.setVisibility(View.GONE);

                        if (dataSnapshot.getValue() != null) {
                            try {
                                AttendanceModel model = dataSnapshot.getValue(AttendanceModel.class);
                                if (model != null) {
                                    attendanceUserList.addAll(model.getUsers());
                                }
                            } catch (Exception e) {

                            }

                            if (attendanceUserList.size() > 0) {
                                adapter.notifyDataSetChanged();
                                binding.tvNoAttendance.setVisibility(View.GONE);
                                binding.fl.setVisibility(View.VISIBLE);

                            } else {
                                binding.fl.setVisibility(View.GONE);

                                binding.tvNoAttendance.setVisibility(View.VISIBLE);

                            }
                        } else {
                            binding.fl.setVisibility(View.GONE);

                            binding.tvNoAttendance.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void checkReadPermission() {
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
            Picasso.get().load(uri).fit().into(binding.imageCertificate);
            binding.tvUpload.setVisibility(View.VISIBLE);
        }

    }


}
