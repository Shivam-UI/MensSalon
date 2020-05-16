package com.createdinam.saloon.global.calender;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarData {
    private int startday, currentmonth, currentyear, dayofweek,hrs,hrs12;
    private String stringDayofWeek;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("E");
    private Calendar calendar;

    public CalendarData(int start) {
        this.calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, start);
        setThis();
    }


    private void setThis() {
        this.startday = calendar.get(Calendar.DAY_OF_MONTH);
        this.currentmonth = calendar.get(Calendar.MONTH);
        this.currentyear = calendar.get(Calendar.YEAR);
        this.dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
        this.stringDayofWeek = dateFormat.format(calendar.getTime());
        this.hrs = calendar.get(Calendar.HOUR_OF_DAY);
        this.hrs12 = calendar.get(Calendar.HOUR);
    }

    public void getNextWeekDay(int nxt) {
        calendar.add(Calendar.DATE, nxt);
        setThis();
    }

    public String getWeekDay() {
        return this.stringDayofWeek;
    }

    public int getYear() {
        return this.currentyear;
    }

    public int getMonth() {
        return this.currentmonth;
    }

    public int getDay() {
        return this.startday;
    }

    public int getHours(){ return this.hrs; }
}
