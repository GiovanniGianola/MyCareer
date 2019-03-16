package com.example.mycareer.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.example.mycareer.utils.Costants;
import com.example.mycareer.utils.UtilsConversions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mycareer.R;
import com.example.mycareer.base.BaseFragment;
import com.example.mycareer.model.Course;
import com.example.mycareer.presenter.CoursesFragmentPresenterImpl;
import com.example.mycareer.presenter.CoursesFragmentPresenter;
import com.example.mycareer.adapter.RVAdapter;
import com.example.mycareer.utils.Utils;
import com.example.mycareer.view.CourseFragmentView;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CoursesFragment extends BaseFragment implements CourseFragmentView, View.OnClickListener{
    private static final String TAG = CoursesFragment.class.getSimpleName();
    private CoursesFragmentPresenter coursesFragmentPresenter;

    private RecyclerView mRecyclerView;
    private FloatingActionButton add_btn;
    private LinearLayoutManager llm;

    private RVAdapter rvAdapter;

    private TextView dialogTitle;
    private EditText courseName;
    private EditText courseCredit;
    private TextView errorField;
    private Calendar calendar;
    private Dialog dialog;
    private DatePicker courseDatePicker;
    private NumberPicker numberPicker;
    private int numberPickerValue = 1;

    private List<Course> courseList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("MyCourses");

        initUI();

        coursesFragmentPresenter = new CoursesFragmentPresenterImpl();
        coursesFragmentPresenter.attachView(this);
        coursesFragmentPresenter.checkTextViewNoCourses();
        coursesFragmentPresenter.initData();
    }

    private void initUI(){
        add_btn = getView().findViewById(R.id.add_btn);
        add_btn.setOnClickListener(this);
        mRecyclerView = getView().findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        llm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(llm);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                coursesFragmentPresenter.onScrolled(dy, add_btn.isShown());
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void addDatas(List<Course> list) {
        if(getActivity() != null) {
            this.courseList = list;
            rvAdapter = new RVAdapter(courseList, getActivity());
            rvAdapter.attachView(this);
            mRecyclerView.setAdapter(rvAdapter);
            rvAdapter.runLayoutAnimation(mRecyclerView);
        }
    }

    @Override
    public void checkTextViewNoCourses(boolean hasCourses) {
        if(hasCourses && getView() != null)
            getView().findViewById(R.id.no_course_show).setVisibility(View.GONE);
        else if(!hasCourses && getView() != null)
            getView().findViewById(R.id.no_course_show).setVisibility(View.VISIBLE);
    }

    @Override
    public void showAddButton(boolean show) {
        if(!show)
            add_btn.show();
        else
            add_btn.hide();
    }

    @Override
    public void setVisibilityCalendar(boolean visibility) {
        if(!visibility)
            courseDatePicker.setVisibility(View.GONE);
        else
            courseDatePicker.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCalendarView(int year, int month, int day) {
        calendar.set(year, month, day, 0, 0);
    }

    @Override
    public void setGradeNumberPicker(int val) {
        numberPickerValue = val;
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public void updateListAdapterOnNewCourse(int pos, Course c) {
        this.courseList.add(c);
        sortList();
        rvAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(pos);
    }

    @Override
    public void updateListAdapterOnUpdateCourse(int pos, Course c) {
        this.courseList.remove(pos);
        this.courseList.add(pos,c);
        rvAdapter.notifyDataSetChanged();
    }

    private void sortList(){
        Collections.sort(courseList, new Comparator<Course>() {
            @Override
            public int compare(final Course object1, final Course object2) {
                return object1.getName().compareTo(object2.getName());
            }
        });
    }

    @Override
    public void onErrorCreateCourse(String error) {
        Log.d(TAG, error);
        errorField.setVisibility(View.VISIBLE);
        errorField.setText(R.string.entry_exist);
    }

    @Override
    public void onSuccessCreateCourse(String message) {
        Log.d(TAG, "Course added to DB.");
        Utils.showMessage(getContext(), message);

    }

    @Override
    public void onSuccessUpdateCourse(String message) {
        Log.d(TAG, "Course updated in DB.");
        Utils.showMessage(getContext(), message);
    }

    @Override
    public void onDatabaseError(String error) {
        Log.d(TAG, error);
        Utils.showMessage(getContext(), error);
    }

    @Override
    public void onInvalidCourse() {
        errorField.setVisibility(View.VISIBLE);
        errorField.setText(R.string.field_mandatory);
    }

    @Override
    public void onClick(View v) {
        initCustomDialog(this.getResources().getString(R.string.add_course), null);
    }

    @Override
    public void initCustomDialog(String title, @Nullable final Course course){
        // custom dialog
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog);

        // set the custom dialog components
        dialogTitle = dialog.findViewById(R.id.dialog_title);
        courseName = dialog.findViewById(R.id.course_name);
        courseCredit = dialog.findViewById(R.id.course_credit);
        courseDatePicker = dialog.findViewById(R.id.datePicker_course_date);
        errorField = dialog.findViewById(R.id.error_field);
        calendar = Calendar.getInstance();
        numberPicker = dialog.findViewById(R.id.number_picker);

        dialogTitle.setText(title);

        coursesFragmentPresenter.initNumberPicker(numberPicker);
        coursesFragmentPresenter.setOnDatePickerListener(courseDatePicker);

        coursesFragmentPresenter.checkCourse(course);

        Button saveButton = dialog.findViewById(R.id.btn_save);
        Button cancelButton = dialog.findViewById(R.id.btn_cancel);
        if(course == null)
            coursesFragmentPresenter.setOnClickListenrSaveButton(saveButton, Costants.Strings.DIALOG_ADD_NEW_COURSE);
        else
            coursesFragmentPresenter.setOnClickListenrSaveButton(saveButton, Costants.Strings.DIALOG_UPDATE_COURSE);
        coursesFragmentPresenter.setOnClickListenrCancelButton(cancelButton);

        dialog.show();
    }

    @Override
    public void fillCustomDialog(Course course) {
        System.out.println("SCORE: " + (UtilsConversions.convertScoreToInt(course.getScore())-16));
        courseName.setText(course.getName());
        courseName.setEnabled(false);
        numberPicker.setValue(UtilsConversions.convertScoreToInt(course.getScore())-16);
        courseCredit.setText(course.getCredit());
    }

    @Override
    public void initAlerDialogOnDeleteCourse(Course course) {
        coursesFragmentPresenter.deleteCourse(course);
    }

    @Override
    public void onSuccessDeleteCourse(String message) {
        Log.d(TAG, "Course deleted in DB.");
        Utils.showMessage(getContext(), message);
    }

    @Override
    public void updateListAdapterOnDeleteCourse(Course course) {
        this.courseList.remove(course);
        rvAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public Context getContext() {
        return getView().getContext();
    }

    @Override
    public String getInsertCourseName(){
        return courseName.getText().toString().trim();
    }

    @Override
    public String getInsertCourseGrade(){
        return UtilsConversions.convertScoreToString(numberPickerValue + 16);
    }

    @Override
    public String getInsertCourseCredit(){
        return courseCredit.getText().toString().trim();
    }

    @Override
    public Date getInsertCourseDate(){
        return calendar.getTime();
    }
}
