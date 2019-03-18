package com.example.mycareer.presenter;

import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.mycareer.base.BaseFragmentPresenter;
import com.example.mycareer.model.Course;
import com.example.mycareer.view.fragment.CoursesFragment;
import com.shawnlin.numberpicker.NumberPicker;

public interface CoursesFragmentPresenter extends BaseFragmentPresenter<CoursesFragment> {
    void initData();
    void checkTextViewNoCourses();
    void onScrolled(int dy, boolean isShown);

    void setOnDatePickerListener(int year, int month, int dayOfMonth);
    void setOnClickListenerAddBtn();
    void setOnClickListenerEditBtn(Course c);
    void setOnClickListenerDeleteBtn(Course c);
    void setOnClickListenerTextViewSave(String dialogRequest);
    void setOnClickListenerTextViewCancel();
    void setOnValueChangedListenerNumberPicker(int newVal);

    void deleteCourse(Course course);

}
