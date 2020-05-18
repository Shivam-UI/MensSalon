package com.createdinam.saloon.global.calender;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.createdinam.saloon.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomCalender {
    private static List<CalenderModel> calendarList = new ArrayList<>();
    private static List<TimeModel> timerList = new ArrayList<>();
    private static RecyclerView recyclerView, recycler_timer_picker;
    private static RelativeLayout set_cancel_lay, set_date_lay;
    private static CalendarAdapter mAdapter;
    private static TimerAdapter mTimerAdapter;
    Dialog mDialog;
    Activity mActivity;
    public CustomCalender(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public CustomCalender() {

    }

    public void showCalendar() {
        mDialog = new Dialog(mActivity, R.style.CalendarDialog);
        mDialog.setContentView(R.layout.calendar_dailog_picker);
        //mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        startCustomCalendarView();
        startTimerAsPerCalanderDate();
        mDialog.show();
    }

    public void hideCalendar() {
        set_cancel_lay = mDialog.findViewById(R.id.set_cancel_lay);
        set_cancel_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.hide();
            }
        });

        set_date_lay = mDialog.findViewById(R.id.set_date_lay);
        set_date_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity, "You Select Date", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startCustomCalendarView() {
        hideCalendar();
        calendarList.clear();
        recyclerView = mDialog.findViewById(R.id.recycler_view);
        recycler_timer_picker = mDialog.findViewById(R.id.recycler_timer_picker);
        mAdapter = new CalendarAdapter(calendarList);
        mTimerAdapter = new TimerAdapter(timerList);
        //recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        final RecyclerView.LayoutManager mTimerLayoutManager = new LinearLayoutManager(mActivity.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recycler_timer_picker.setLayoutManager(mTimerLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = mLayoutManager.getChildCount();
                for (int i = 0; i < totalItemCount; i++) {
                    View childView = recyclerView.getChildAt(i);
                    TextView childTextView = (TextView) (childView.findViewById(R.id.day_1));
                    String childTextViewText = (String) (childTextView.getText());
                    if (childTextViewText.equals("Sun"))
                        childTextView.setTextColor(Color.RED);
                    else
                        childTextView.setTextColor(Color.BLACK);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        prepareCalendarData();
    }

    private int getCurrentItem() {
        return ((LinearLayoutManager) recyclerView.getLayoutManager())
                .findLastCompletelyVisibleItemPosition();
    }

    private void setCurrentItem(int position, int incr) {
        position = position + incr;
        if (position < 0)
            position = 0;
        recyclerView.smoothScrollToPosition(position);
    }

    /*
     * Prepares sample data to provide data set to adapter
     */
    private void prepareCalendarData() {
        // run a for loop for all the next 30 days of the current month starting today
        // initialize my calendar Data and get Instance
        // get next to get next set of date etc.. which can be used to populate MyCalendarList in for loop
        CalendarData m_calendar = new CalendarData(0);
        for (int i = 0; i < 30; i++) {
            CalenderModel calendar = new CalenderModel(m_calendar.getWeekDay(), String.valueOf(m_calendar.getDay()), String.valueOf(m_calendar.getMonth()), String.valueOf(m_calendar.getYear()), i);
            m_calendar.getNextWeekDay(1);
            calendarList.add(calendar);
        }
        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
    }

    public void startTimerAsPerCalanderDate() {
        timerList.clear();
        Date calendar = Calendar.getInstance().getTime();
        for (int i = 1; i < 24; i++) {
            String time_type = "";
            if (i < 12) {
                time_type = "AM";
            } else {
                time_type = "PM";
            }
            TimeModel timeModel = new TimeModel(i, time_type);
            timerList.add(timeModel);
            Log.d("hours", " " + calendar.getHours() + " : " + time_type + " " + timerList.size());
        }
        recycler_timer_picker.setAdapter(mTimerAdapter);
        mTimerAdapter.notifyDataSetChanged();
    }
}
