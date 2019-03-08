package com.example.mycareer.presenter;

import com.example.mycareer.base.BasePresenter;
import com.example.mycareer.view.activity.HomePageActivity;

public interface HomePagePresenter extends BasePresenter<HomePageActivity> {
    void initGoogleSetting();
    void logOut();
    void getCurrentUser();
    void onStart();
    void onStop();

    void handleCloseApp();
}
