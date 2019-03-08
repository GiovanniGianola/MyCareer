package com.example.mycareer.view;

import com.example.mycareer.base.BaseFragmentView;
import com.example.mycareer.model.Course;

import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;

public interface CourseFragmentView extends BaseFragmentView {
    void addDatas(List<Course> courseList);
    void checkTextViewNoCourses(boolean hasCourses);
    void showAddButton(boolean show);
    void setVisibilityCalendar(boolean visibility);
    void setCalendarView(int year, int month, int day);
    void setGradeNumberPicker(int val);
    void dismissDialog();
    void updateListAdapterOnNewCourse(int pos, Course c);
    void updateListAdapterOnUpdateCourse(int pos, Course c);

    void onErrorCreateCourse(String error);
    void onSuccessCreateCourse(String message);

    void onSuccessUpdateCourse(String message);

    void onDatabaseError(String error);

    void onInvalidCourse();

    String getInsertCourseName();
    String getInsertCourseGrade();
    String getInsertCourseCredit();
    Date getInsertCourseDate();

    void initCustomDialog(String title, @Nullable final Course course);
    void fillCustomDialog(Course course);

    void initAlerDialogOnDeleteCourse(Course course);
    void onSuccessDeleteCourse(String message);
    void updateListAdapterOnDeleteCourse(Course course);

}
