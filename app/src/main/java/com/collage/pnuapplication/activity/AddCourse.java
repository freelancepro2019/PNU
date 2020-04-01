package com.collage.pnuapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.databinding.DialogAddAddSelectImageBinding;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.CourseModel;
import com.collage.pnuapplication.model.UserModel;
import com.collage.pnuapplication.preferences.Preferences;
import com.collage.pnuapplication.share.Common;
import com.collage.pnuapplication.tags.Tags;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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

    @BindView(R.id.spinner)
    Spinner spinner;


    private final String camera_perm = Manifest.permission.CAMERA;
    private final String read_perm = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private final int read_req = 1;
    private final int camera_req = 2;
    private Uri uri = null;
    private StorageReference sRef;

    private Preferences preferences ;
    private UserModel userModel;
    private List<String> categoryList;
    private String category = "";


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        initView();



    }

    private void initView() {
        categoryList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.course_category_array)));

        ButterKnife.bind(this);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        sRef = FirebaseStorage.getInstance().getReference();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categoryList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                {
                    category = "";
                }else if (i==1)
                {
                    category = Tags.category_club;
                }else if (i==2)
                {
                    category = Tags.category_collage;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dateET.setOnClickListener(v -> {

            final Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateET.setText(sdf.format(myCalendar.getTime()));
            };
            new DatePickerDialog(AddCourse.this,date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();



        });

        selectImageBtn.setOnClickListener(view -> {
            createSelectImageDialog();
        });
    }

    private void createSelectImageDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogAddAddSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_add_add_select_image, null, false);


        binding.btnCancel.setOnClickListener(v ->dialog.dismiss());

        binding.imageGallery.setOnClickListener(view -> {
            dialog.dismiss();
            checkReadPermission(read_req);
        });
        binding.imageCamera.setOnClickListener(view ->{
            dialog.dismiss();
            checkCameraPermission();

        } );


        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void checkReadPermission(int req)
    {
        if (ContextCompat.checkSelfPermission(this,read_perm)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{read_perm},req);
        }else
        {
            selectImage(req);
        }
    }

    private void checkCameraPermission()
    {
        if (ContextCompat.checkSelfPermission(this,camera_perm)!=PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this,write_perm)!=PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(this,new String[]{camera_perm,write_perm},camera_req);
        }else
        {
            selectImage(camera_req);
        }
    }

    private void selectImage(int req)
    {

        Intent intent = new Intent();

        if (req == read_req)
        {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            }else
            {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }
            intent.setType("image/*");


        }
        else if (req == camera_req)
        {
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        }

        startActivityForResult(intent,req);


    }

    private Uri getUriFromBitmap(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream);
        String uri = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"","");
        return Uri.parse(uri);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == camera_req)
        {
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED
                    &&grantResults[1]==PackageManager.PERMISSION_GRANTED
            )
            {
                selectImage(camera_req);

            }else
            {
                Toast.makeText(this, "Permission to access image denied", Toast.LENGTH_SHORT).show();
            }

        }else if (requestCode ==read_req)
        {
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED

            )
            {
                selectImage(read_req);

            }else
            {
                Toast.makeText(this, "Permission to access image denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode ==read_req&&resultCode== Activity.RESULT_OK&&data!=null)
        {
            uri = data.getData();

            Picasso.get().load(uri).into(image);
        }
        else if (requestCode ==camera_req&&resultCode== Activity.RESULT_OK&&data!=null)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);
            uri = getUriFromBitmap(bitmap);
        }

    }


    @OnClick(R.id.sendBtn)
    void SendButtonAction() {


        if (titleET.getText().toString().isEmpty() || descET.getText().toString().isEmpty() || dateET.getText().toString().isEmpty()
                || priceET.getText().toString().isEmpty() || locationET.getText().toString().isEmpty()||category.isEmpty()) {

            Toast.makeText(this,R.string.pls_fill, Toast.LENGTH_LONG).show();
            return;
        }


        if (uri == null) {
            Toast.makeText(this, R.string.ch_image, Toast.LENGTH_LONG).show();
            return;
        }


        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();


        StorageReference ref = sRef.child("Course").child(UUID.randomUUID().toString()+ ".png");
        ref.putFile(uri).addOnSuccessListener(taskSnapshot -> {

            ref.getDownloadUrl().addOnSuccessListener(uri -> addCourse(dialog,uri.toString()));

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


    private void addCourse(ProgressDialog dialog, String image) {

        CourseModel model = new CourseModel();
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference();

        String id = dRef.child(Tags.table_course).push().getKey();

        model.setId(id);
        model.setTitle(titleET.getText().toString());
        model.setDesc(descET.getText().toString());
        model.setTime(dateET.getText().toString());
        model.setPrice(priceET.getText().toString());
        model.setLocation(locationET.getText().toString());
        model.setCourse_category(category);
        model.setImage(image);
        model.setUserId(userModel.getId());
        model.setCreditHours(ceditET.getText().toString());
        model.setNumbers(Integer.parseInt(numberET.getText().toString()));
        model.setAvailable(Integer.parseInt(numberET.getText().toString()));







        dRef.child(Tags.table_course).child(id).setValue(model)
                .addOnSuccessListener(aVoid -> {
                    dialog.dismiss();
                    Toast.makeText(AddCourse.this,getString(R.string.success),Toast.LENGTH_LONG).show();
                    finish();
                }).addOnFailureListener(e -> {
                    dialog.dismiss();
                    if (e.getMessage()!=null)
                    {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this,getString(R.string.failed), Toast.LENGTH_SHORT).show();

                    }
                });
    }


}
