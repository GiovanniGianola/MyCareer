package com.example.mycareer.model;

import java.util.Date;

public class Course {
    private String name;
    private String credit;
    private Date date;
    private String score;

    public Course(){}

    public Course(String name, String score, String credit, Date date) {
        this.name = name;
        this.credit = credit;
        this.date = date;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getCredit() {
        return credit;
    }

    public Date getDate() {
        return date;
    }

    public String getScore() {
        return score;
    }
}

