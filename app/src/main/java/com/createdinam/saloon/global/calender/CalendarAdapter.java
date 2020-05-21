package com.createdinam.saloon.global.calender;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.createdinam.saloon.R;
import com.createdinam.saloon.global.Global;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {
    private List<CalenderModel> mCalendar;
    private int recyclecount = 0;
    int selectedPosition = -1;
    CustomCalender customCalender;
    // month in int convert
    int monthNumber;
    String MY_DATE = "";
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tb_day, tb_date, tb_month;
        private CardView mCardView;

        public MyViewHolder(View view) {
            super(view);
            tb_day = view.findViewById(R.id.day_1);
            tb_date = view.findViewById(R.id.date_1);
            tb_month = view.findViewById(R.id.month_1);
            mCardView = view.findViewById(R.id.date_container);
        }
    }

    public CalendarAdapter(List<CalenderModel> mCalendar) {
        this.mCalendar = mCalendar;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calender_list_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        customCalender = new CustomCalender();
        final CalenderModel calendar = mCalendar.get(position);
        // set date to calender
        holder.tb_day.setText(calendar.getDay());
        holder.tb_month.setText(calendar.getMonth());
        holder.tb_date.setText(calendar.getDate());
        // set color to card view
        holder.mCardView.setCardBackgroundColor(Color.parseColor("#000000"));
        if (selectedPosition == position) {
            holder.mCardView.setCardBackgroundColor(Color.parseColor("#26c0a2"));
            holder.tb_day.setTextColor(Color.parseColor("#ffffff"));
            holder.tb_month.setTextColor(Color.parseColor("#ffffff"));
            holder.tb_date.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.mCardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
            holder.tb_day.setTextColor(Color.parseColor("#000000"));
            holder.tb_month.setTextColor(Color.parseColor("#000000"));
            holder.tb_date.setTextColor(Color.parseColor("#000000"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                Global.date_picker="";
                try{
                    //put your month name here
                    Date date = new SimpleDateFormat("MMM").parse(calendar.getMonth());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    monthNumber=cal.get(Calendar.MONTH);
                    monthNumber = monthNumber+1;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                Log.d("select_date ",""+calendar.getDate()+" / "+monthNumber+" "+" / "+calendar.getYear());
                Global.date_picker = calendar.getDate()+"-"+monthNumber+"-"+calendar.getYear();
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return mCalendar.size();
    }

    @Override
    public void onViewRecycled(MyViewHolder holder) {
        recyclecount++;
    }

}
