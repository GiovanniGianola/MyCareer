package com.example.mycareer.presenter;

import android.widget.NumberPicker;

import com.example.mycareer.base.BaseFragmentPresenter;
import com.example.mycareer.view.fragment.PredictionFragment;

public interface PredictionFragmentPresenter extends BaseFragmentPresenter<PredictionFragment> {
    void showCustomDialog();

    void setOnClickListenerCreditsButton();
    void setOnClickListenerSaveButton();
    void setOnClickListenerCancelButton();

    void initNumberPicker(NumberPicker numberPicker);
    void loadDataInFragment();
}
