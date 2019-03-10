package com.example.mycareer.view;

import com.example.mycareer.base.BaseFragmentView;
import com.example.mycareer.model.PredictionEntry;

import java.util.List;

public interface PredictionFragmentView extends BaseFragmentView {
    void setNumberPicker(int val);
    int getNumberPickerValue();

    void showDialog();
    void dismissDialog();

    void setCurrentAverage(double avg);
    void setMinAvg(double avg);
    void setMaxAvg(double avg);
    void setCredits(String credits);

    void setAdapter(List<PredictionEntry> predictionEntryList);
    void updateListAdapter(List<PredictionEntry> predictionEntryList);
}
