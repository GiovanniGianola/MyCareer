package com.example.mycareer.presenter;

import android.support.v4.app.FragmentManager;

import com.example.mycareer.base.BasePresenter;
import com.example.mycareer.model.Profile;
import com.example.mycareer.view.activity.HomePageActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public interface HomePagePresenter extends BasePresenter<HomePageActivity> {
    void initGoogleSetting();
    void logOut();
    void getCurrentUser();
    void onStart();
    void onStop();

    void handleCloseApp();
}
