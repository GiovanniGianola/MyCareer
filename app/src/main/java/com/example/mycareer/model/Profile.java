package com.example.mycareer.model;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Profile implements Serializable {
    private String email;
    private String name;
    private String userId;
    private Uri photoUri;
    private boolean isAdmin;
    private Settings settings;

    private Statistic stats;

    private List<Course> courseList;

    private static Profile sSoleInstance;

    //private constructor.
    private Profile(){
        //Prevent form the reflection api.
        if (sSoleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.stats = new Statistic();
        this.settings = new Settings();
    }

    public static Profile getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new Profile();
        }
        return sSoleInstance;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void reset(){
        sSoleInstance = null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<Course> getCourseList() {
        List<Course> copyList = new ArrayList<>();
        if(courseList != null) {
            for (int i = 0; i < courseList.size(); i++)
                copyList.add(courseList.get(i));
            return copyList;
        }
        return null;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
        sortListAlphabetically();
        stats.setCourses(courseList);
    }

    public int addCourse(Course c){
        this.courseList.add(c);
        sortListAlphabetically();
        stats.setCourses(courseList);
        return courseList.indexOf(c);
    }

    public void removeCourse(Course c){
        this.courseList.remove(c);
        stats.setCourses(courseList);
    }

    public void updateCourse(Course c, int pos){
        this.courseList.remove(pos);
        addCourse(c);
        stats.setCourses(courseList);
    }

    public int getCourseIndex(Course c){
        for(int i = 0; i < courseList.size(); i++)
            if(c.getName().compareTo(courseList.get(i).getName()) == 0)
                return i;
        return  -1;
    }

    public DatabaseReference getFirebaseReference(){
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        return mFirebaseInstance.getReference("users/" + this.getUserId());
    }

    public Statistic getStatistics() {
        return stats;
    }

    private void sortListAlphabetically(){
        Collections.sort(courseList, new Comparator<Course>() {
            @Override
            public int compare(final Course object1, final Course object2) {
                return object1.getName().compareTo(object2.getName());
            }
        });
    }
}