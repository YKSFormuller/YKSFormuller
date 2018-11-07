package com.yksformuller.model;

public class YKSDate {
    private String examName, date;

    public YKSDate(){

    }

    public YKSDate(String examName, String date){
        this.examName=examName;
        this.date=date;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
