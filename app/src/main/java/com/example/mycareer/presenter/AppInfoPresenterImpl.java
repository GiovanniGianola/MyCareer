package com.example.mycareer.presenter;

import android.app.Activity;

import com.example.mycareer.view.AppInfoView;
import com.example.mycareer.view.activity.AppInfoActivity;

public class AppInfoPresenterImpl implements AppInfoPresenter {
    private static final String TAG = AppInfoPresenterImpl.class.getSimpleName();
    private AppInfoView appInfoView;

    private Activity context;

    public AppInfoPresenterImpl(final Activity context){
        this.context = context;
    }

    @Override
    public void attachView(AppInfoActivity view) {
        appInfoView = view;
    }

    @Override
    public void detachView() {
        appInfoView = null;
    }

    @Override
    public void setOnClickListenerCardViewAbout() {
        appInfoView.onAbout();
    }

    @Override
    public void setOnClickListenerCardViewEraseData() {
        appInfoView.onEraseData();
    }

    @Override
    public void setOnClickListenerCardViewSendEmail() {
        appInfoView.onSendEmail();
    }
}
