package com.createdinam.saloon.global.calender;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.createdinam.saloon.R;
import java.util.List;

public class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.TimerHolder> {
    int selectedPosition = -1;
    private List<TimeModel> mTimer;

    public TimerAdapter(List<TimeModel> timer) {
        this.mTimer = timer;
    }

    @NonNull
    @Override
    public TimerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_picker_items, parent, false);
        return new TimerAdapter.TimerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerHolder holder, final int position) {
        final TimeModel timeModel = mTimer.get(position);
        holder.txt_hours.setText(""+timeModel.getHrs());
        holder.txt_AM_PM.setText(""+timeModel.AM_PM);
        //holder.time_container.setCardBackgroundColor(Color.parseColor("#000000"));
        if (selectedPosition == position) {
            holder.time_container.setCardBackgroundColor(Color.parseColor("#26c0a2"));
            holder.txt_hours.setTextColor(Color.parseColor("#ffffff"));
            holder.txt_AM_PM.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.time_container.setCardBackgroundColor(Color.parseColor("#ffffff"));
            holder.txt_hours.setTextColor(Color.parseColor("#000000"));
            holder.txt_AM_PM.setTextColor(Color.parseColor("#000000"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTimer.size();
    }

    public class TimerHolder extends RecyclerView.ViewHolder{
        TextView txt_hours,txt_AM_PM;
        CardView time_container;
        public TimerHolder(@NonNull View itemView) {
            super(itemView);
            txt_hours = itemView.findViewById(R.id.txt_hours);
            txt_AM_PM = itemView.findViewById(R.id.txt_AM_PM);
            time_container = itemView.findViewById(R.id.time_container);
        }
    }
}
