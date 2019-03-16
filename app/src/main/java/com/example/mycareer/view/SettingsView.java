package com.example.mycareer.view;

import com.example.mycareer.base.BaseView;

public interface SettingsView extends BaseView {
    void showDialog(String title, int req);
    void dismissDialog();

    void setTotCredits(int credits);
    void setLaudeVal(int val);

    CharSequence getEditTextVal();

    void onError(String error);

    void refreshApp();
}
