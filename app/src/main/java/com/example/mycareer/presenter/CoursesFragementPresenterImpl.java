package com.example.mycareer.presenter;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycareer.R;
import com.example.mycareer.model.Course;
import com.example.mycareer.model.Profile;
import com.example.mycareer.view.CourseFragmentView;
import com.example.mycareer.view.fragment.CoursesFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CoursesFragementPresenterImpl implements CoursesFragmentPresenter {
    private static final String TAG = CoursesFragementPresenterImpl.class.getSimpleName();
    private CourseFragmentView courseFragmentView;

    public CoursesFragementPresenterImpl(){
        for(int i = 0; i< Profile.getInstance().getCourseList().size();i++)
            System.out.println(Profile.getInstance().getCourseList().get(i).getName());
    }

    @Override
    public void attachView(CoursesFragment view) {
        courseFragmentView = view;
    }

    @Override
    public void detachView() {
        courseFragmentView = null;
    }

    @Override
    public void initData() {
        List<Course> courseList = Profile.getInstance().getCourseList();
        courseFragmentView.addDatas(courseList);
    }

    @Override
    public void checkTextViewNoCourses() {
        courseFragmentView.checkTextViewNoCourses((Profile.getInstance().getCourseList().size() > 0));
    }

    @Override
    public void onScrolled(int dy, boolean isShown) {
        if(dy<0 && !isShown){
            courseFragmentView.showAddButton(false);
        } else if(dy>0 && isShown){
            courseFragmentView.showAddButton(true);
        }
    }


    @Override
    public void setOnItemSelectedListenerSpinner(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int index = parentView.getSelectedItemPosition();
                if(index == 0)
                    courseFragmentView.setVisibilityCalendar(false);
                else
                    courseFragmentView.setVisibilityCalendar(true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    @Override
    public void setOnClickListenrSaveButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Course c = courseFragmentView.getInsertCourse();
                System.out.println(evaluateCourse(c.getName(),c.getCredit()));
                System.out.println(c.getName() + " - " + c.getCredit());
                if(evaluateCourse(c.getName(),c.getCredit())) {
                    Profile.getInstance().getFirebaseReference().child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot data: dataSnapshot.getChildren()){
                                if (data.getKey().equals(c.getName())) {
                                    courseFragmentView.onErrorCreateCourse("Course Already in DB");
                                    return;
                                }
                            }
                            Profile.getInstance().getFirebaseReference().child("courses").child(c.getName()).setValue(c);
                            updateUser(c);
                            courseFragmentView.checkTextViewNoCourses(true);
                            courseFragmentView.onSuccessCreateCourse("Course " + c.getName() + " added!");
                            courseFragmentView.dismissDialog();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(TAG, "DatabaseError.");
                            courseFragmentView.onDatabaseError("Database Error: " + databaseError.getMessage());
                        }
                    });
                }else{
                    courseFragmentView.onInvalidCourse();
                }
            }
        });
    }

    @Override
    public void setOnClickListenrCancelButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Cancel btn Pressed: dismiss.");
                courseFragmentView.dismissDialog();
            }
        });
    }

    @Override
    public void setOnDateChangeListenerCalendarView(CalendarView calendarView) {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                courseFragmentView.setCalendarView(year, month, day);
            }
        });
    }

    private void updateUser(Course c) {
        int idx = Profile.getInstance().addCourse(c);
        courseFragmentView.updateListAdapter(idx, c);
    }

    private boolean evaluateCourse(String name, String credit){
        if(TextUtils.isEmpty(name.trim()) || TextUtils.isEmpty(credit.trim()))
            return false;
        return true;
    }
}
