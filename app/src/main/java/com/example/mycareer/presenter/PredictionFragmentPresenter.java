package com.example.mycareer.presenter;

import android.widget.NumberPicker;

import com.example.mycareer.base.BaseFragmentPresenter;
import com.example.mycareer.view.fragment.PredictionFragment;

public interface PredictionFragmentPresenter extends BaseFragmentPresenter<PredictionFragment> {
    void setOnClickListenerCreditsButton();
    void setOnClickListenerTextViewSave();
    void setOnClickListenerTextViewCancel();

    void setOnValueChangedListenerNumberPicker(int newVal);

    void initNumberPickerValues();
    void loadDataInFragment();
}
