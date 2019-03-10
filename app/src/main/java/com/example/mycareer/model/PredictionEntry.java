package com.example.mycareer.model;

public class PredictionEntry {

    private String grade;
    private double newAvg;
    private double variation;

    public PredictionEntry(String grade, double newAvg, double variation){
        this.grade = grade;
        this.newAvg = newAvg;
        this.variation = variation;
    }

    public String getGrade() {
        return grade;
    }

    public double getNewAvg() {
        return newAvg;
    }

    public double getVariation() {
        return variation;
    }
}
