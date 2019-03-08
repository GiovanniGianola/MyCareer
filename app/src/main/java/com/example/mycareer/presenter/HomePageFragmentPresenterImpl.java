package com.example.mycareer.presenter;

import androidx.annotation.NonNull;
import android.util.Log;

import com.example.mycareer.model.Course;
import com.example.mycareer.model.Profile;
import com.example.mycareer.model.Statistic;
import com.example.mycareer.utils.Costants;
import com.example.mycareer.view.HomePageFragmentView;
import com.example.mycareer.view.fragment.HomePageFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomePageFragmentPresenterImpl implements HomePageFragmentPresenter {
    private static final String TAG = HomePageFragmentPresenter.class.getSimpleName();
    private HomePageFragmentView homePageFragmentView;

    private Statistic stat;

    public HomePageFragmentPresenterImpl(){
        stat = Profile.getInstance().getStatistics();
        downloadCourses();
    }

    @Override
    public void attachView(HomePageFragment view) {
        homePageFragmentView = view;
    }

    @Override
    public void detachView() {
        homePageFragmentView = null;
    }

    @Override
    public void initCurrentAverage() {
        homePageFragmentView.setCurrentAverage(stat.getAvg());
    }

    @Override
    public void initPassedExams() {
        homePageFragmentView.setPassedExams(stat.getTotalCoursesDone());
    }

    @Override
    public void initMinDegree() {
        homePageFragmentView.setMinDegree(stat.getMinDgr());
    }

    @Override
    public void initMaxDegree() {
        homePageFragmentView.setMaxDegree(stat.getMaxDgr());
    }

    @Override
    public void initCfuDone() {
        if(stat.getTotalCfu() != 0)
            homePageFragmentView.setCfuDone((float)stat.getTotalCfu(), (float)stat.getTotalCfuDone());
    }

    @Override
    public void initGradeCount() {
        int [] gradeList = new int[14];
        List<Course> courseList = Profile.getInstance().getCourseList();

        if(courseList != null) {
            for (int i = 0; i < courseList.size(); i++) {
                int cNumber = convertToNumberInt(courseList.get(i).getScore());

                System.out.println(cNumber + " - " + courseList.get(i).getScore());

                if (cNumber != -1)
                    gradeList[cNumber - 18] += 1;
            }
            homePageFragmentView.setBarChartGrades(gradeList);
        }
    }

    private int convertToNumberInt(String num){
        int n = 0;
        switch(num){
            case Costants.Strings.NOT_DONE_YET:
                n = -1;
                break;
            case "30L":
                n = 31;
                break;
            default:
                n = Integer.parseInt(num);
        }
        return n;
    }

    @Override
    public void initTimeLineAvg() {
        List<Course> courseList = Profile.getInstance().getCourseList();
        List<Long> dates = new ArrayList<>();
        float[] avgList;
        if(courseList != null) {
            Collections.sort(courseList, new Comparator<Course>() {
                public int compare(Course o1, Course o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });

            for (int x = 0; x < courseList.size(); x++) {
                if(convertToNumberFloat(courseList.get(x).getScore()) != -1f) {
                    dates.add(courseList.get(x).getDate().getTime());
                }
            }

            for (int x = 0; x < courseList.size(); x++) {
                if(convertToNumberFloat(courseList.get(x).getScore()) == -1f)
                    courseList.remove(x--);
            }

            avgList = new float[courseList.size()];
            for (int x = 0; x < courseList.size(); x++) {
                List<Course> tempList = new ArrayList<>();
                for (int y = 0; y <= x; y++) {
                    tempList.add(courseList.get(y));
                }
                avgList[x] = computeAvg(tempList);
            }

            homePageFragmentView.setLineChartAvg(avgList, dates);
        }
    }

    private float computeAvg(List<Course> list){
        DecimalFormat df;
        float avg = 0;
        int sum_credit = 0;
        for (Course elem : list) {
            float score = convertToNumberFloat(elem.getScore());
            float credit = convertToNumberFloat(elem.getCredit());
            if(-1f != score){
                avg += score * credit;
                sum_credit += credit;
            }
        }
        df = new DecimalFormat("###.##");

        String avgStr = df.format(avg/sum_credit).replace(',', '.');

        return (sum_credit == 0) ? 0 : Float.parseFloat(avgStr);
    }

    private float convertToNumberFloat(String num){
        float n = 0;
        switch(num){
            case Costants.Strings.NOT_DONE_YET:
                n = -1f;
                break;
            case "30L":
                n = 30f;
                break;
            default:
                n =  Float.parseFloat(num);
        }
        return n;
    }

    private void downloadCourses(){
        FirebaseDatabase.getInstance().getReference("users/" + Profile.getInstance().getUserId()).child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Course> courseList= new ArrayList<>();

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Course c = data.getValue(Course.class);
                    courseList.add(c);
                }
                Profile.getInstance().setCourseList(courseList);
                uploadStats();
                homePageFragmentView.refreshData();
                Log.d(TAG, "Data Retrived: " + courseList);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Firebase Database Error: " + error.getMessage());
                homePageFragmentView.databaseError(error.getMessage());
            }
        });
    }

    private void uploadStats(){
        FirebaseDatabase.getInstance().getReference().child("statistics").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile.getInstance().getFirebaseReference().child("statistics").child("weightedAvg").setValue(stat.getAvg());
                Profile.getInstance().getFirebaseReference().child("statistics").child("totalCfu").setValue(stat.getTotalCfuDone() + "/" + stat.getTotalCfu());
                Profile.getInstance().getFirebaseReference().child("statistics").child("totalCourses").setValue(stat.getTotalCoursesDone() + "/" + stat.getTotalCourses());
                Profile.getInstance().getFirebaseReference().child("statistics").child("thScoreMin").setValue(stat.getMinDgr());
                Profile.getInstance().getFirebaseReference().child("statistics").child("thScoreMax").setValue(stat.getMaxDgr());
                Log.d(TAG, "Stats added to DB.");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "DatabaseError.");
            }
        });
    }
}
