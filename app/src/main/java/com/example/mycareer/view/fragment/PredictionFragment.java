package com.example.mycareer.view.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mycareer.R;
import com.example.mycareer.adapter.PredictionsAdapter;
import com.example.mycareer.base.BaseFragment;
import com.example.mycareer.model.PredictionEntry;
import com.example.mycareer.presenter.PredictionFragmentPresenter;
import com.example.mycareer.presenter.PredictionFragmentPresenterImpl;
import com.example.mycareer.view.PredictionFragmentView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PredictionFragment extends BaseFragment implements PredictionFragmentView {
    private static final String TAG = PredictionFragment.class.getSimpleName();
    PredictionFragmentPresenter predictionFragmentPresenter;

    private FloatingActionButton credits_btn;
    private LinearLayoutManager llm;

    private Dialog dialog;

    private TextView textView_avgScore;

    private TextView textView_maxAvg;
    private TextView textView_minAvg;

    private TextView textView_gradeListProjectionTitle;
    private RecyclerView mRecyclerView;
    private PredictionsAdapter predictionsAdapter;

    private NumberPicker numberPicker;
    private Button saveButton;
    private Button cancelButton;
    private int numberPickerValue = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prediction, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Predictions");

        predictionFragmentPresenter = new PredictionFragmentPresenterImpl();
        predictionFragmentPresenter.attachView(this);

        initUI();

        predictionFragmentPresenter.loadDataInFragment();

        initCustomDialog();
        initListeners();

        showDialog();
    }

    private void initUI(){
        credits_btn = getView().findViewById(R.id.credits_btn);

        textView_avgScore = getView().findViewById(R.id.textView_avgScore);

        textView_maxAvg = getView().findViewById(R.id.textView_maxAvg);
        textView_minAvg = getView().findViewById(R.id.textView_minAvg);

        textView_gradeListProjectionTitle = getView().findViewById(R.id.textView_gradeListProjectionTitle);
    }

    public void initCustomDialog(){
        // custom dialog
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_predictions);

        numberPicker = dialog.findViewById(R.id.numberPicker);
        saveButton = dialog.findViewById(R.id.btn_save);
        cancelButton = dialog.findViewById(R.id.btn_cancel);

        predictionFragmentPresenter.initNumberPicker(numberPicker);
    }

    private void initListeners(){
        credits_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predictionFragmentPresenter.setOnClickListenerCreditsButton();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predictionFragmentPresenter.setOnClickListenerSaveButton();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predictionFragmentPresenter.setOnClickListenerCancelButton();
            }
        });;
    }

    @Override
    public void setNumberPicker(int val) {
        numberPickerValue = val;
    }

    @Override
    public int getNumberPickerValue() {
        return numberPickerValue;
    }

    @Override
    public void showDialog() {
        dialog.show();
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public void setCurrentAverage(double avg) {
        textView_avgScore.setText(String.format("%.2f", avg));
    }

    @Override
    public void setMinAvg(double avg) {
        textView_minAvg.setTextColor(Color.RED);
        textView_minAvg.setText(String.format("%.2f", avg));
    }

    @Override
    public void setMaxAvg(double avg) {
        textView_maxAvg.setTextColor(Color.GREEN);
        textView_maxAvg.setText(String.format("%.2f", avg));
    }

    @Override
    public void setCredits(String credits) {
        textView_gradeListProjectionTitle.setText(String.format("%s: %s", "Credits of the next exam", credits));
    }

    @Override
    public void setAdapter(List<PredictionEntry> predictionEntryList) {
        if (mRecyclerView == null) {
            mRecyclerView = getView().findViewById(R.id.recycler_view_predictions);
            predictionsAdapter = new PredictionsAdapter(getActivity());

            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            llm = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(llm);

            mRecyclerView.setAdapter(predictionsAdapter);
            predictionsAdapter.runLayoutAnimation(mRecyclerView);
        }
    }

    @Override
    public void updateListAdapter(List<PredictionEntry> predictionEntryList) {
        predictionsAdapter.setDataModel(predictionEntryList);
        predictionsAdapter.notifyDataSetChanged();
        predictionsAdapter.runLayoutAnimation(mRecyclerView);
    }
}
