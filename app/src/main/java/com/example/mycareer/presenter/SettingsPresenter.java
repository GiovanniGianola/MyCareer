package com.example.mycareer.presenter;

import com.example.mycareer.base.BasePresenter;
import com.example.mycareer.view.activity.SettingsActivity;

public interface SettingsPresenter extends BasePresenter<SettingsActivity> {
    void setOnClickListenerCardViewTotCredits();
    void setOnClickListenerCardViewLaudeVal();

    void setOnClickListenerTextViewOk(int dialogRequest);
    void setOnClickListenerTextViewCancel();

    void setOnClickListenerButtonSave();
}
