package com.createdinam.saloon.global.calender;

public class TimeModel {

    int hrs;
    String AM_PM;

    public TimeModel(int hrs, String AM_PM) {
        this.hrs = hrs;
        this.AM_PM = AM_PM;
    }

    public int getHrs() {
        return hrs;
    }

    public void setHrs(int hrs) {
        this.hrs = hrs;
    }

    public String getAM_PM() {
        return AM_PM;
    }

    public void setAM_PM(String AM_PM) {
        this.AM_PM = AM_PM;
    }
}
