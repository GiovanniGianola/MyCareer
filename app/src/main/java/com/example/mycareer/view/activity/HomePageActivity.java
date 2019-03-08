package com.example.mycareer.view.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycareer.R;
import com.example.mycareer.base.BaseActivity;
import com.example.mycareer.presenter.HomePagePresenter;
import com.example.mycareer.presenter.HomePagePresenterImpl;
import com.example.mycareer.utils.Utils;
import com.example.mycareer.view.HomePageView;
import com.example.mycareer.view.fragment.CoursesFragment;
import com.example.mycareer.view.fragment.HomePageFragment;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class HomePageActivity extends BaseActivity implements HomePageView, NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseUser user;

    private HomePagePresenter homePagePresenter;

    private View mView;
    private ImageView profilePic;
    private TextView userName;
    private TextView userEmail;
    private NavigationView navigationView;

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mView = findViewById(R.id.activity_layout);

        initUI();

        mAuth = FirebaseAuth.getInstance();

        homePagePresenter = new HomePagePresenterImpl(mAuth, this);
        homePagePresenter.attachView(this);
        homePagePresenter.initGoogleSetting();
        homePagePresenter.getCurrentUser();

        displaySelectedScreen(R.id.nav_homepage);
    }

    private void initUI(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_homepage:
                Utils.showMessage(this,"Homepage");
                fragment = new HomePageFragment();
                break;
            case R.id.nav_courses:
                Utils.showMessage(this,"Courses!");
                fragment = new CoursesFragment();
                break;
            case R.id.nav_log_out:
                homePagePresenter.logOut();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, fragment.getClass().getName());
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void setUser(FirebaseUser user) {
        this.user = user;
        updateUserUI();
    }

    @Override
    public void setGoogleClient(GoogleSignInClient googleClient) {
        mGoogleSignInClient = googleClient;
    }

    @Override
    public void databaseError(String error) {
        //Utils.showSnackbar(mView, "Firebase Database Error ! " + error);
        Utils.showMessage(this, "Firebase Database Error ! " + error);
    }

    @Override
    public void closeApp() {
        finish();
    }

    private void updateUserUI(){
        View hView =  navigationView.getHeaderView(0);

        profilePic = hView.findViewById(R.id.profile_pic);
        userName = hView.findViewById(R.id.user_name);
        userEmail = hView.findViewById(R.id.user_email);

        userName.setText(user.getDisplayName());
        userEmail.setText(user.getEmail());
        Picasso.get().load(user.getPhotoUrl()).into(profilePic);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homePagePresenter.detachView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        homePagePresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        homePagePresenter.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            //super.onBackPressed();
            homePagePresenter.handleCloseApp();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
