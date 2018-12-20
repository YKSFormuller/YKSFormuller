package com.yksformuller.model;

public class Formula {
    private String formulAdi, konuAdi, resimurl;

    public Formula() {

    }

    public Formula(String formulAdi, String konuAdi, String resimurl) {
        this.formulAdi = formulAdi;
        this.konuAdi = konuAdi;
        this.resimurl = resimurl;
    }

    public String getFormulAdi() {
        return formulAdi;
    }

    public void setFormulAdi(String formulAdi) {
        this.formulAdi = formulAdi;
    }

    public String getKonuAdi() {
        return konuAdi;
    }

    public void setKonuAdi(String konuAdi) {
        this.konuAdi = konuAdi;
    }

    public String getResimurl() {
        return resimurl;
    }

    public void setResimurl(String resimurl) {
        this.resimurl = resimurl;
    }
}
