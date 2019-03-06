package com.example.mycareer.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mycareer.R;
import com.example.mycareer.base.BaseFragment;
import com.example.mycareer.model.Course;
import com.example.mycareer.model.Profile;
import com.example.mycareer.presenter.CoursesFragementPresenterImpl;
import com.example.mycareer.presenter.CoursesFragmentPresenter;
import com.example.mycareer.utils.RVAdapter;
import com.example.mycareer.utils.Utils;
import com.example.mycareer.view.CourseFragmentView;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

public class CoursesFragment extends BaseFragment implements CourseFragmentView, View.OnClickListener{
    private static final String TAG = CoursesFragment.class.getSimpleName();
    private CoursesFragmentPresenter coursesFragmentPresenter;

    private RecyclerView mRecyclerView;
    private FloatingActionButton add_btn;
    private LinearLayoutManager llm;

    private RVAdapter rvAdapter;

    private EditText courseName;
    private EditText courseCredit;
    private CalendarView courseDate;
    private TextView errorField;
    private Calendar calendar;
    private Spinner courseGrade;
    private Dialog dialog;

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

        coursesFragmentPresenter = new CoursesFragementPresenterImpl();
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
            courseDate.setVisibility(View.GONE);
        else
            courseDate.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCalendarView(int year, int month, int day) {
        calendar.set(year, month, day, 0, 0);
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public Course getInsertCourse() {
        return new Course(
                courseName.getText().toString().trim(),
                courseGrade.getSelectedItem().toString(),
                courseCredit.getText().toString().trim(),
                calendar.getTime());
    }

    @Override
    public void updateListAdapter(int pos, Course c) {
        this.courseList.add(c);
        sortList();
        rvAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(pos);
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
        // custom dialog
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog);

        // set the custom dialog components
        courseName = dialog.findViewById(R.id.course_name);
        courseCredit = dialog.findViewById(R.id.course_credit);
        courseDate = dialog.findViewById(R.id.course_date);
        errorField = dialog.findViewById(R.id.error_field);
        calendar = Calendar.getInstance();
        courseGrade = dialog.findViewById(R.id.spinner_grade);

        coursesFragmentPresenter.setOnItemSelectedListenerSpinner(courseGrade);

        Button saveButton = dialog.findViewById(R.id.btn_save);
        Button cancelButton = dialog.findViewById(R.id.btn_cancel);
        coursesFragmentPresenter.setOnClickListenrSaveButton(saveButton);
        coursesFragmentPresenter.setOnClickListenrCancelButton(cancelButton);
        coursesFragmentPresenter.setOnDateChangeListenerCalendarView(courseDate);

        dialog.show();
    }

    @Nullable
    @Override
    public Context getContext() {
        return getView().getContext();
    }
}
