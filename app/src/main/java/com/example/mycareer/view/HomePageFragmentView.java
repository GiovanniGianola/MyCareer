package com.example.mycareer.view;

import com.example.mycareer.base.BaseFragmentView;

import java.util.Date;
import java.util.List;

public interface HomePageFragmentView extends BaseFragmentView {
    void refreshData();
    void setCurrentAverage(Double avg);
    void setPassedExams(Integer number);
    void databaseError(String error);

    void setMinDegree(Integer number);
    void setMaxDegree(Integer number);

    void setCfuDone(Float totalCfu, Float totalCfuDone);
    void setBarChartGrades(int[] gradesCount);
    void setLineChartAvg(float[] avgList, List<Long> dateList);
}
