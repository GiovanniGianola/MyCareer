package com.example.mycareer.model;

import com.example.mycareer.utils.Constants;
import com.example.mycareer.utils.MyApplication;
import com.example.mycareer.utils.SharedPrefManager;
import com.example.mycareer.utils.UtilsConversions;

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

    public int maxCfu() {
        return SharedPrefManager.getIntPrefVal(MyApplication.getAppContext(), Constants.Strings.SPREF_TOT_CREDITS_KEY, totalCfu);
    }

    public int getCfuToBeDone() {
        return this.maxCfu() - totalCfuDone;
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
            double score = UtilsConversions.convertScoreToDouble(elem.getScore());
            double credit = UtilsConversions.convertScoreToDouble(elem.getCredit());
            if(-1D != score){
                avg += score * credit;
            }
        }
        df = new DecimalFormat("###.##");

        String avgStr = df.format(avg/this.getTotalCfuDone()).replace(',', '.');

        return (this.getTotalCfuDone() == 0) ? 0 : Double.parseDouble(avgStr);
    }

    private int computeMinDgr(){
        df = new DecimalFormat("###.###");
        double avg = getAvg();
        double grade = ((avg/30.0*110) + getMinTh());

        try {
            return Math.round(Float.parseFloat(df.format((grade > 110) ? 110 : grade)));
        }catch(java.lang.NumberFormatException e){
            return Math.round(Float.parseFloat(df.format((grade > 110) ? 110 : grade).replace(",", ".")));
        }
    }

    private int computeMaxDgr(){
        df = new DecimalFormat("###.###");
        double avg = getAvg();
        double grade = ((avg/30.0*110) + getMaxTh());

        try {
            return Math.round(Float.parseFloat(df.format((grade > 110) ? 110 : grade)));
        }catch(java.lang.NumberFormatException e){
            return Math.round(Float.parseFloat(df.format((grade > 110) ? 110 : grade).replace(",", ".")));
        }
    }

    private int computeTotalCfuDone(){
        int sum_credit = 0;
        for (Course elem : courseList) {
            double score = UtilsConversions.convertScoreToDouble(elem.getScore());
            double credit = UtilsConversions.convertScoreToDouble(elem.getCredit());
            if(-1D != score)
                sum_credit += credit;
        }

        return sum_credit;
    }

    private int computeTotalCfu(){
        int sum_credit = 0;
        for (Course elem : courseList) {
            double credit = UtilsConversions.convertScoreToDouble(elem.getCredit());
            sum_credit += credit;
        }
        return sum_credit;
    }

    private int computeTotalCoursesDone(){
        int count = 0;
        for (Course elem : courseList) {
            double score = UtilsConversions.convertScoreToDouble(elem.getScore());
            if(-1D != score)
                count++;
        }
        return count;
    }
}
