package com.example.mycareer.presenter;

import com.example.mycareer.base.BaseFragmentPresenter;
import com.example.mycareer.view.fragment.HomePageFragment;

public interface HomePageFragmentPresenter extends BaseFragmentPresenter<HomePageFragment> {
    void initCurrentAverage();
    void initPassedExams();

    void initMinDegree();
    void initMaxDegree();

    void initCfuDone();
    void initGradeCount();
    void initTimeLineAvg();
}
