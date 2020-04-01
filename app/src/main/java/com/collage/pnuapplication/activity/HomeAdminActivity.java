package com.collage.pnuapplication.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.databinding.DialogLanguageBinding;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.model.UserModel;
import com.collage.pnuapplication.preferences.Preferences;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class HomeAdminActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.navView)
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Preferences preferences;
    private UserModel userModel;
    private FirebaseAuth mAuth;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.home));

        initViews();

    }



    private void initViews() {
        Paper.init(this);
        mAuth = FirebaseAuth.getInstance();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        TextView tvName = navigationView.getHeaderView(0).findViewById(R.id.name);
        tvName.setText(userModel.getName());
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.itemAbout:
                navigateToAboutApplicationActivity();
                break;
            case R.id.itemChangeLanguage:
                createDialogAlert();
                break;
            case R.id.itemChangePassword:
                navigateToChangePasswordActivity();
                break;
            case R.id.itemLogout:
                logout();
                break;
        }
        return true;
    }

    private void navigateToAboutApplicationActivity() {
        Intent intent =new Intent(this,AboutApplication.class);
        startActivity(intent);
    }

    public void Timeline(View view) {

        Intent intent =new Intent(HomeAdminActivity.this,AddCourse.class);
        intent.putExtra("data","");
        intent.putExtra("user",userModel.getId());
        startActivity(intent);
    }

    public void Calendar(View view) {
        startActivity(new Intent(HomeAdminActivity.this, AdminCalenderActivity.class));
    }

    public void voteAction(View view) {
        startActivity(new Intent(HomeAdminActivity.this, VoteResultActivity.class));
    }

    public void suggestions(View view) {
       startActivity(new Intent(HomeAdminActivity.this, SuggesionsActivity.class));
    }

    public void attendance(View view) {
        startActivity(new Intent(HomeAdminActivity.this, AdminCoursesActivity.class));

    }

    private void navigateToChangePasswordActivity() {

        startActivity(new Intent(this, ChangePasswordActivity.class));

    }

    private  void createDialogAlert()
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();
        DialogLanguageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_language, null, false);

        String lang = Paper.book().read("lang","en");

        if (lang.equals("ar"))
        {
            binding.rbAr.setChecked(true);
        }else if (lang.equals("en"))
        {
            binding.rbEn.setChecked(true);

        }

        binding.rbAr.setOnClickListener(view -> {

            dialog.dismiss();
            new Handler()
                    .postDelayed(() -> refreshActivity("ar"),500);
        });

        binding.rbEn.setOnClickListener(view -> {

            dialog.dismiss();
            new Handler()
                    .postDelayed(() -> refreshActivity("en"),500);

        });


        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()

        );


        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }
    private void refreshActivity(String selected_language)
    {

        Paper.init(this);
        Paper.book().write("lang", selected_language);
        preferences.saveSelectedLanguage(this);
        LanguageHelper.setNewLocale(this, selected_language);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    private void logout()
    {

        mAuth.signOut();
        preferences.clear(this);
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed()
    {

        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }else
        {
            finish();
        }
    }

}
