package com.collage.pnuapplication.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.collage.pnuapplication.R;
import com.collage.pnuapplication.language.LanguageHelper;
import com.collage.pnuapplication.preferences.Preferences;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private Preferences preferences;
    private FirebaseAuth mAuth;
    @BindView(R.id.menuIV)
    ImageView menuIV;

    private int mSelectedId;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initViews();

    }



    private void initViews() {
        mAuth = FirebaseAuth.getInstance();
        mDrawer = findViewById(R.id.nvView);
        mDrawerLayout = findViewById(R.id.drawer_layout);


        mDrawer.setNavigationItemSelectedListener(this);


        menuIV.setOnClickListener(view -> {
            if (mDrawerLayout.isDrawerVisible(GravityCompat.END)) {
                mDrawerLayout.closeDrawer(GravityCompat.END);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
        });

    }



    @SuppressLint("WrongConstant")
    private void itemSelection(int mSelectedId) {

        switch (mSelectedId) {

            case R.id.about_lm:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(HomeActivity.this, AboutApplication.class));
                break;
            case R.id.logout_lm:
               logout();
                break;

        }


    }

    private void logout() {

        mAuth.signOut();
        preferences.clear(this);
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param ,menuItem The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(false);
        mSelectedId = menuItem.getItemId();
        itemSelection(mSelectedId);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        //save selected item so it will remains same even after orientation change
        outState.putInt("SELECTED_ID", mSelectedId);
    }



    public void Timeline(View view) {


//        ClubCollageModel model = new ClubCollageModel();
//
//
//        model.setId(random());
//        model.setName("it club");
//        model.setDesc("A3");
//        model.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQjQtMteULrWDvigHC2Yg5yQf9OX5QeYa2UXzEbWov3q4_i5kZX");
//
//        model.setType("2");
//
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//
//
//        ref.child("ClubCollage")
//                .child(model.getId()).setValue(model);

        startActivity(new Intent(HomeActivity.this,TimeLineActivity.class));
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


    public void Calendar(View view) {
        startActivity(new Intent(HomeActivity.this, CalendarActivity.class));
    }

    public void voteAction(View view) {
        startActivity(new Intent(HomeActivity.this, VoteActivity.class));
    }

    public void suggestAction(View view) {
        startActivity(new Intent(HomeActivity.this, SuggestActivity.class));


    }


    public void cetificateaction(View view) {
        startActivity(new Intent(HomeActivity.this, CertificateActivity.class));
    }
}
