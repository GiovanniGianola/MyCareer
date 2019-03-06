package com.example.mycareer.model;

import java.text.DecimalFormat;
import java.util.List;

public class Statistic {

    private List<Course> courseList = null;
    private DecimalFormat df;
    private double avg;
    private int minTh;
    private int maxTh;
    private int minDgr;
    private int maxDgr;
    private int totalCfuDone;
    private int totalCfu;
    private int totalExams;

    public Statistic(List<Course> courseList){
        this.courseList = courseList;
        this.minTh = 0;
        this.maxTh = 7;

        updateStats();
    }

    public Statistic(){
        this.minTh = 0;
        this.maxTh = 7;
    }

    private void updateStats(){
        if(courseList != null && !courseList.isEmpty()) {
            this.totalCfuDone = computeTotalCfuDone();
            this.totalCfu = computeTotalCfu();
            this.totalExams = computeTotalCoursesDone();
            this.avg = computeAvg();
            this.minDgr = computeMinDgr();
            this.maxDgr = computeMaxDgr();
        }
    }

    public double getAvg() {
        return this.avg;
    }

    public int getMinTh() {
        return minTh;
    }

    public int getMaxTh() {
       return maxTh;
    }

    public int getMinDgr() {
        return this.minDgr;
    }

    public int getMaxDgr() {
        return this.maxDgr;
    }

    public int getTotalCfuDone() {
        return totalCfuDone;
    }

    public int getTotalCfu() {
        return this.totalCfu;
    }

    public int getTotalCoursesDone() {
        return this.totalExams;
    }

    public int getTotalCourses() {
        return courseList.size();
    }

    public void setCourses(List<Course> courses) {
        this.courseList = courses;
        updateStats();
    }

    public void setMinTh(int minTh) {
        this.minTh = minTh;
        updateStats();
    }

    public void setMaxTh(int maxTh) {
        this.maxTh = maxTh;
        updateStats();
    }

    /*------------- private methods --------------*/

    private double computeAvg(){
        double avg = 0;
        for (Course elem : courseList) {
            double score = convertToNumber(elem.getScore());
            double credit = convertToNumber(elem.getCredit());
            if(-1D != score){
                avg += score * credit;
            }
        }
        df = new DecimalFormat("###.##");

        return (this.getTotalCfuDone() == 0) ? 0 : Double.parseDouble(df.format(avg/this.getTotalCfuDone()));
    }

    private double convertToNumber(String num){
        double n = 0;
        switch(num){
            case "Not done yet":
                n = -1D;
                break;
            case "30L":
                n = 30D;
                break;
            default:
                n = Double.parseDouble(num);
        }
        return n;
    }

    private int computeMinDgr(){
        df = new DecimalFormat("###.##");
        double avg = getAvg();
        double grade = (int) ((avg/30.0*110) + getMinTh());

        return Integer.parseInt(df.format((grade > 110) ? 110 : grade));
    }

    private int computeMaxDgr(){
        df = new DecimalFormat("###.##");
        double avg = getAvg();
        double grade = (int) ((avg/30.0*110) + getMaxTh());

        return Integer.parseInt(df.format((grade > 110) ? 110 : grade));
    }

    private int computeTotalCfuDone(){
        int sum_credit = 0;
        for (Course elem : courseList) {
            double score = convertToNumber(elem.getScore());
            double credit = convertToNumber(elem.getCredit());
            if(-1D != score)
                sum_credit += credit;
        }

        return sum_credit;
    }

    private int computeTotalCfu(){
        int sum_credit = 0;
        for (Course elem : courseList) {
            double credit = convertToNumber(elem.getCredit());
            sum_credit += credit;
        }
        return sum_credit;
    }

    private int computeTotalCoursesDone(){
        int count = 0;
        for (Course elem : courseList) {
            double score = convertToNumber(elem.getScore());
            if(-1D != score)
                count++;
        }
        return count;
    }
}
