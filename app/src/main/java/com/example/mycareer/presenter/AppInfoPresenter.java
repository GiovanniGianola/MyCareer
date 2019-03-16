package com.example.mycareer.presenter;

import com.example.mycareer.base.BasePresenter;
import com.example.mycareer.view.activity.AppInfoActivity;

public interface AppInfoPresenter extends BasePresenter<AppInfoActivity> {
    void setOnClickListenerCardViewAbout();
    void setOnClickListenerCardViewEraseData();
    void setOnClickListenerCardViewSendEmail();
}
