package com.example.mycareer.presenter;

import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;

import com.example.mycareer.base.BaseFragmentPresenter;
import com.example.mycareer.model.Course;
import com.example.mycareer.view.fragment.CoursesFragment;

public interface CoursesFragmentPresenter extends BaseFragmentPresenter<CoursesFragment> {
    void initData();
    void checkTextViewNoCourses();
    void onScrolled(int dy, boolean isShown);

    void setOnItemSelectedListenerSpinner(Spinner spinner);
    void setOnClickListenrSaveButton(Button button);
    void setOnClickListenrCancelButton(Button button);
    void setOnDateChangeListenerCalendarView(CalendarView calendarView);

}
