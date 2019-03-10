package com.example.mycareer.presenter;

import android.util.Log;
import android.widget.NumberPicker;

import com.example.mycareer.R;
import com.example.mycareer.model.Course;
import com.example.mycareer.model.PredictionEntry;
import com.example.mycareer.model.Profile;
import com.example.mycareer.model.Statistic;
import com.example.mycareer.utils.Costants;
import com.example.mycareer.view.PredictionFragmentView;
import com.example.mycareer.view.fragment.PredictionFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PredictionFragmentPresenterImpl implements PredictionFragmentPresenter {
    private static final String TAG = PredictionFragmentPresenterImpl.class.getSimpleName();
    private PredictionFragmentView predictionFragmentView;

    private Statistic userStats;
    private Statistic newStats;
    private String[] data;
    private int numberPickerValue = 1;
    private List<PredictionEntry> predictionEntryList;

    private double minAvg;
    private double maxAvg;

    public PredictionFragmentPresenterImpl(){
        userStats = Profile.getInstance().getStatistics();
    }

    @Override
    public void attachView(PredictionFragment view) {
        predictionFragmentView = view;
    }

    @Override
    public void detachView() {
        predictionFragmentView = null;
    }

    @Override
    public void showCustomDialog() {

    }

    @Override
    public void setOnClickListenerCreditsButton() {
        predictionFragmentView.showDialog();
    }

    @Override
    public void setOnClickListenerSaveButton() {
        predictionFragmentView.dismissDialog();
        numberPickerValue = predictionFragmentView.getNumberPickerValue();

        initEntriesData();

        predictionFragmentView.setAdapter(predictionEntryList);
        predictionFragmentView.updateListAdapter(predictionEntryList);

        predictionFragmentView.setCredits(data[numberPickerValue-1]);

        predictionFragmentView.setMinAvg(minAvg);
        predictionFragmentView.setMaxAvg(maxAvg);
    }

    private void initEntriesData() {
        newStats = new Statistic();
        List<Course> newList = Profile.getInstance().getCourseList();

        Course newCourse = new Course();
        newCourse.setCredit(data[numberPickerValue-1]);

        predictionEntryList = new ArrayList<>();

        for(int i = 0; i < Costants.Strings.GRADES.length; i++){
            String grade = Costants.Strings.GRADES[i];

            newCourse.setScore(grade);
            newList.add(newCourse);

            newStats.setCourses(newList);

            double var = newStats.getAvg() - userStats.getAvg();
            predictionEntryList.add(new PredictionEntry(grade, newStats.getAvg(), var));

            newList.remove(newCourse);
        }
        minAvg = predictionEntryList.get(predictionEntryList.size()-1).getNewAvg();
        maxAvg = predictionEntryList.get(0).getNewAvg();
    }

    @Override
    public void setOnClickListenerCancelButton() {
        predictionFragmentView.dismissDialog();
    }

    @Override
    public void initNumberPicker(NumberPicker numberPicker) {
        int arraySize = Math.max(Profile.getInstance().getStatistics().getCfuToBeDone(), 20);
        data = new String[arraySize];

        for(int i = 1; i <= data.length; i ++)
            data[i-1] = String.format("%s", i);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(data.length);
        numberPicker.setDisplayedValues(data);
        numberPicker.setValue(1);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        // Set fading edge enabled
        numberPicker.setWrapSelectorWheel(false);

        // OnValueChangeListener
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
                System.out.println(oldVal + " - " + newVal);
                predictionFragmentView.setNumberPicker(newVal);
            }
        });
    }

    @Override
    public void loadDataInFragment() {
        predictionFragmentView.setCurrentAverage(userStats.getAvg());
    }
}
