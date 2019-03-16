package com.example.mycareer.presenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.mycareer.R;
import com.example.mycareer.model.Course;
import com.example.mycareer.model.Profile;
import com.example.mycareer.utils.Costants;
import com.example.mycareer.utils.UtilsConversions;
import com.example.mycareer.view.CourseFragmentView;
import com.example.mycareer.view.fragment.CoursesFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.RequiresApi;

public class CoursesFragmentPresenterImpl implements CoursesFragmentPresenter {
    private static final String TAG = CoursesFragmentPresenterImpl.class.getSimpleName();
    private CourseFragmentView courseFragmentView;

    public CoursesFragmentPresenterImpl(){
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
        if(Profile.getInstance().getCourseList() != null)
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
    public void setOnClickListenrSaveButton(Button button, String purpose) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("grade: " + courseFragmentView.getInsertCourseGrade());

                final Course c = new Course(
                        courseFragmentView.getInsertCourseName(),
                        courseFragmentView.getInsertCourseGrade(),
                        courseFragmentView.getInsertCourseCredit(),
                        courseFragmentView.getInsertCourseDate()
                );

                if(evaluateCourse(c.getName(),c.getCredit())) {
                    Profile.getInstance().getFirebaseReference().child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            switch (purpose){
                                case Costants.Strings.DIALOG_ADD_NEW_COURSE:
                                    addNewCourse(dataSnapshot, c);
                                    break;
                                case Costants.Strings.DIALOG_UPDATE_COURSE:
                                    updateCourse(dataSnapshot, c);
                                    break;
                            }
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setOnDatePickerListener(DatePicker courseDatePicker) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        courseDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);
                courseFragmentView.setCalendarView(year, month, dayOfMonth);
            }
        });
    }

    @Override
    public void initNumberPicker(NumberPicker numberPicker) {
        // Using string values
        // IMPORTANT! setMinValue to 1 and call setDisplayedValues after setMinValue and setMaxValue
        String[] data = courseFragmentView.getContext().getResources().getStringArray(R.array.grade_arrays);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(data.length);
        numberPicker.setDisplayedValues(data);
        numberPicker.setValue(1);
        // Set fading edge enabled
        numberPicker.setFadingEdgeEnabled(true);
        // Set scroller enabled
        numberPicker.setScrollerEnabled(true);
        // Set wrap selector wheel
        numberPicker.setWrapSelectorWheel(false);
        // OnValueChangeListener
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
                System.out.println(oldVal + " - " + newVal);
                setCalendarVisibility(newVal);
                courseFragmentView.setGradeNumberPicker(newVal);
            }
        });

    }

    private void setCalendarVisibility(int numberPickerIndex){
        if(numberPickerIndex == 1)
            courseFragmentView.setVisibilityCalendar(false);
        else
            courseFragmentView.setVisibilityCalendar(true);
    }

    int pos = 0;
    @Override
    public void checkCourse(Course course) {
        if(course != null) {
            courseFragmentView.fillCustomDialog(course);
            pos = Profile.getInstance().getCourseIndex(course);

            setCalendarVisibility(UtilsConversions.convertScoreToInt(course.getScore())-16);
            System.out.println("score index: " + (UtilsConversions.convertScoreToInt(course.getScore())-16));
        }
    }

    @Override
    public void deleteCourse(Course course) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(courseFragmentView.getContext());
        alertDialogBuilder.setTitle("Delete " + course.getName());
        alertDialogBuilder
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Profile.getInstance().getFirebaseReference().child("courses").child(course.getName()).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Profile.getInstance().removeCourse(course);
                                        courseFragmentView.onSuccessDeleteCourse("Course " + course.getName() + " deleted.");
                                        courseFragmentView.updateListAdapterOnDeleteCourse(course);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //
                                    }
                                });
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void updateUser(Course c) {
        int idx = Profile.getInstance().addCourse(c);
        courseFragmentView.updateListAdapterOnNewCourse(idx, c);
    }

    private boolean evaluateCourse(String name, String credit){
        if(TextUtils.isEmpty(name.trim()) || TextUtils.isEmpty(credit.trim()))
            return false;
        return true;
    }

    private void addNewCourse(DataSnapshot dataSnapshot, Course c){
        for(DataSnapshot data: dataSnapshot.getChildren()){
            if (data.getKey().equals(c.getName())) {
                courseFragmentView.onErrorCreateCourse("Course Already in DB");
                return;
            }
        }
        Profile.getInstance().getFirebaseReference().child("courses").child(c.getName()).setValue(c);
        Log.d(TAG, "Course added in DB.");

        updateUser(c);
        courseFragmentView.checkTextViewNoCourses(true);
        courseFragmentView.onSuccessCreateCourse("Course " + c.getName() + " added!");
    }

    private void updateCourse(DataSnapshot dataSnapshot, Course c){
        for(DataSnapshot data: dataSnapshot.getChildren()){
            if (data.getKey().equals(c.getName())) {
                Profile.getInstance().getFirebaseReference().child("courses").child(c.getName()).setValue(c);
                Log.d(TAG, "Course updated in DB.");

                Profile.getInstance().updateCourse(c, pos);
                courseFragmentView.onSuccessUpdateCourse("Course " + c.getName() + " updated.");
                courseFragmentView.updateListAdapterOnUpdateCourse(pos,c);
                return;
            }
        }
    }
}
