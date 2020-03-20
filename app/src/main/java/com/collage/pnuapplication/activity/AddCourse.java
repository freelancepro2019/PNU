package com.collage.pnuapplication.activity;

import android.app.DatePickerDialog;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.CourseModeel;
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
import io.paperdb.Paper;

public class AddCourse extends AppCompatActivity {


    @BindView(R.id.titleET)
    EditText titleET;
    @BindView(R.id.numberET)
    EditText numberET;
    @BindView(R.id.descET)
    EditText descET;
    @BindView(R.id.dateET)
    EditText dateET;
    @BindView(R.id.priceET)
    EditText priceET;
    @BindView(R.id.locationET)
    EditText locationET;
    @BindView(R.id.ceditET)EditText ceditET;
    @BindView(R.id.selectImageBtn)
    FrameLayout selectImageBtn;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.loading)
    ProgressBar loading;


    Bitmap bitmap;


    private StorageReference mStorageRef;

    SharedPrefDueDate pref;


    String userId ;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        ButterKnife.bind(this);


        //init the storage
        pref = new SharedPrefDueDate(this);

        userId = getIntent().getStringExtra("user");

        mStorageRef = FirebaseStorage.getInstance().getReference();




        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "MM-dd-yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        dateET.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                new DatePickerDialog(AddCourse.this,date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();



            }
        });



        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup().setCameraButtonText("كاميرا")
                        .setGalleryButtonText("الصور").setCancelText("الغاء").setTitle("اختر"))
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult pickResult) {

                                image.setImageBitmap(pickResult.getBitmap());
                                bitmap = pickResult.getBitmap();

//                                uploadImage(pickResult.getBitmap());

                            }
                        }).show(getSupportFragmentManager());
            }
        });

    }


    @OnClick(R.id.sendBtn)
    void SendButtonAction() {


        if (titleET.getText().toString().isEmpty() || descET.getText().toString().isEmpty() || dateET.getText().toString().isEmpty()
                || priceET.getText().toString().isEmpty() || locationET.getText().toString().isEmpty()) {

            Toast.makeText(this, "من فضلك قم بملئ جميع البيانات", Toast.LENGTH_LONG).show();
            return;
        }


        if (bitmap == null) {
            Toast.makeText(this, "من فضلك قم باختيار الصورة", Toast.LENGTH_LONG).show();
            return;
        }




        loading.setVisibility(View.VISIBLE);
        final StorageReference tripsRef = mStorageRef.child("Course/" + random() + ".png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = tripsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(AddCourse.this, "حدث خطأ عند رفع الصوره ", Toast.LENGTH_LONG).show();
                loading.setVisibility(View.GONE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                //todo to get the downloaded url
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Toast.makeText(AddCourse.this, "حدث خطأ عند رفع الصوره ", Toast.LENGTH_LONG).show();
                            loading.setVisibility(View.GONE);
                            throw task.getException();

                        }

                        // Continue with the task to get the download URL
                        return tripsRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.d("google", "this is the downloaded url      " + downloadUri);
                            addCourse(downloadUri + "");
                        } else {
                            // Handle failures
                            // ...
                            Toast.makeText(AddCourse.this, "حدث خطأ عند رفع الصوره ", Toast.LENGTH_LONG).show();
                            loading.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });


    }


    private void addCourse(String image) {

        CourseModeel model = new CourseModeel();


        model.setId(random());
        model.setTitle(titleET.getText().toString());
        model.setDesc(descET.getText().toString());
        model.setTime(dateET.getText().toString());
        model.setPrice(priceET.getText().toString());
        model.setLocation(locationET.getText().toString());
        model.setImage(image);
        model.setUserId(userId);
        model.setCreditHours(ceditET.getText().toString());
        model.setNumbers(Integer.parseInt(numberET.getText().toString()));
        model.setAvailable(Integer.parseInt(numberET.getText().toString()));



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();



        Toast.makeText(AddCourse.this,"تم الاضافة بنجاح",Toast.LENGTH_LONG).show();

        finish();
        ref.child("Course")
                .child(model.getId()).setValue(model);
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
