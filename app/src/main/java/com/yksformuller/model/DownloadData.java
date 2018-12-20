package com.yksformuller.model;

public class DownloadData {
    private String formulaName;
    private String subjectName;
    private byte [] imageURL;


    public DownloadData(){

    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public byte[] getImageURL() {
        return imageURL;
    }

    public void setImageURL(byte[] imageURL) {
        this.imageURL = imageURL;
    }
}
