package com.example.mycareer.view;

import com.example.mycareer.base.BaseFragmentView;
import com.example.mycareer.model.Course;

import java.util.List;

public interface CourseFragmentView extends BaseFragmentView {
    void addDatas(List<Course> courseList);
    void checkTextViewNoCourses(boolean hasCourses);
    void showAddButton(boolean show);
    void setVisibilityCalendar(boolean visibility);
    void setCalendarView(int year, int month, int day);
    void dismissDialog();
    Course getInsertCourse();
    void updateListAdapter(int pos, Course c);

    void onErrorCreateCourse(String error);
    void onSuccessCreateCourse(String message);

    void onDatabaseError(String error);

    void onInvalidCourse();

}
