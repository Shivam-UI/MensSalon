package com.createdinam.saloon.global.calender;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.createdinam.saloon.R;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {
    private List<CalenderModel> mCalendar;
    private int recyclecount=0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tb_day, tb_date,tb_month;

        public MyViewHolder(View view) {
            super(view);
            tb_day = view.findViewById(R.id.day_1);
            tb_date = view.findViewById(R.id.date_1);
            tb_month = view.findViewById(R.id.month_1);
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CalenderModel calendar = mCalendar.get(position);
        // set date to calender
        holder.tb_day.setText(calendar.getDay());
        holder.tb_month.setText(calendar.getMonth());
        holder.tb_date.setText(calendar.getDate());
    }

    @Override
    public int getItemCount() {
        return mCalendar.size();
    }

    @Override
    public void onViewRecycled (MyViewHolder holder){
        recyclecount++;
    }

}
