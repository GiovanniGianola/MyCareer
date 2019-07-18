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
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycareer.R;
import com.example.mycareer.base.BaseActivity;
import com.example.mycareer.presenter.HomePagePresenter;
import com.example.mycareer.presenter.HomePagePresenterImpl;
import com.example.mycareer.utils.Utils;
import com.example.mycareer.view.HomePageView;
import com.example.mycareer.view.fragment.HomePageFragment;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class HomePageActivity extends BaseActivity implements HomePageView {
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseUser user;

    private HomePagePresenter homePagePresenter;

    private ImageView profilePic;
    private TextView userName;
    private TextView userEmail;
    private NavigationView navigationView;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initUI();
        initListeners();

        mAuth = FirebaseAuth.getInstance();

        homePagePresenter = new HomePagePresenterImpl(mAuth, this);
        homePagePresenter.attachView(this);
        homePagePresenter.initGoogleSetting();
        homePagePresenter.getCurrentUser();

        startFragmentWithMessage("Homepage", new HomePageFragment());
    }

    private void initUI(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
    }

    private void initListeners() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                homePagePresenter.onSelectedItem(menuItem.getItemId());
                return true;
            }
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                homePagePresenter.onDrawerOpened();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                homePagePresenter.onDrawerClose();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public Context getContext() {
        return this;
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
        Utils.showMessage(this, "Firebase Database Error ! " + error);
    }

    @Override
    public void closeApp() {
        finish();
    }

    @Override
    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void startFragmentWithMessage(String message, Fragment destination) {
        Utils.showMessage(this, message);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        //replacing the fragment
        if (destination != null && (currentFragment == null || !currentFragment.getClass().equals(destination.getClass()))) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            ft.replace(R.id.content_frame, destination, destination.getClass().getName());
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void startClassWithMessage(String message, Class destination) {
        Utils.showMessage(this, message);
        Utils.setIntent(this, destination);
    }

    @Override
    public void addProfileMenu(int id) {
        Menu menu = navigationView.getMenu();
        menu.setGroupVisible(R.id.profiles_menu, true);
    }

    private void updateUserUI(){
        View hView =  navigationView.getHeaderView(0);

        profilePic = hView.findViewById(R.id.imageview_profilePic);
        userName = hView.findViewById(R.id.textview_userName);
        userEmail = hView.findViewById(R.id.textview_userEmail);

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
}
