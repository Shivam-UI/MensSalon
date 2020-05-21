package com.createdinam.saloon.user.nowlist.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.createdinam.saloon.R;
import com.createdinam.saloon.user.HomeListModel;
import com.createdinam.saloon.user.itemdetails.SaloonItensDetailsActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class NowSalonAdapter extends RecyclerView.Adapter<NowSalonAdapter.NowListHolder>{
    NowPagerSlider pagerSlider;
    ArrayList<NowModel> mNowList = new ArrayList<NowModel>();
    Context mContext;
    Activity activity;
    // set time init
    SimpleDateFormat simpleDateFormat;
    String time;
    Calendar calander;

    Timer timer;
    public NowSalonAdapter(ArrayList<NowModel> mNowList, Context mContext,Activity mActivity) {
        this.mNowList = mNowList;
        this.mContext = mContext;
        this.activity = mActivity;
    }

    @NonNull
    @Override
    public NowListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.now_list_view_items,parent,false);
        return new NowListHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final NowListHolder holder, final int position) {
        // get data from list
        String id = mNowList.get(position).getSalon_id();
        String sal_name = mNowList.get(position).getSalon_name();
        String sal_distance = mNowList.get(position).getDistance();
        String sal_address = mNowList.get(position).getAddress();
        String sal_discount = mNowList.get(position).getDiscount();
        String sal_rating = mNowList.get(position).getRating();
        String sal_images = mNowList.get(position).getImage();
        String sal_available = mNowList.get(position).getAvailability();

        // set discount
        if(sal_discount.trim().matches("")){
            sal_discount = "0%";
            holder.nw_sal_discount.setText("Discount "+sal_discount);
        }else{
            holder.nw_sal_discount.setText("Discount Upto "+sal_discount);
        }

        // remove and split links
        String[] images = sal_images.split(", ");
        String url = "";
        final ArrayList<String> ListImagesSlider = new ArrayList<String>();
        for (int i = 0;i< images.length;i++){
            url = images[i].replaceAll("\\s","%20");
            //Log.d("URL",""+url);
            ListImagesSlider.add(url);
        }
        // set to pager view
        pagerSlider = new NowPagerSlider(ListImagesSlider,mContext,activity);
        holder.mNow_slider.setAdapter(pagerSlider);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                holder.mNow_slider.post(new Runnable(){

                    @Override
                    public void run() {
                        holder.mNow_slider.setCurrentItem((holder.mNow_slider.getCurrentItem()+1)%ListImagesSlider.size());
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);

        // set data to list
        holder.nw_sal_name.setText(sal_name);
        holder.now_sal_distance.setText(sal_distance);
        holder.nw_sal_address.setText(sal_address);
        holder.nw_sal_rating.setRating(Float.parseFloat(sal_rating));

        // set selected click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set current time
                calander = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("hh:mm a");
                time = simpleDateFormat.format(calander.getTime());
                onTimeSet(time);

                Intent detailsView = new Intent(mContext.getApplicationContext(), SaloonItensDetailsActivity.class);
                detailsView.putExtra("salon_id",""+mNowList.get(position).getSalon_id());
                detailsView.putExtra("lat",""+mNowList.get(position).getLatitude());
                detailsView.putExtra("long",""+mNowList.get(position).getLongitude());
                detailsView.putExtra("time",""+time);
                detailsView.putExtra("flag","now");
                detailsView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(detailsView);
                //((Activity)(mContext)).finish();
                // clear time
                time = "";
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNowList.size();
    }

    public class NowListHolder extends RecyclerView.ViewHolder{
        ViewPager mNow_slider;
        TextView nw_sal_name,now_sal_distance,nw_sal_address,nw_sal_discount;
        RatingBar nw_sal_rating;
        public NowListHolder(@NonNull View itemView) {
            super(itemView);
            nw_sal_rating = itemView.findViewById(R.id.nw_sal_rating);
            nw_sal_name = itemView.findViewById(R.id.nw_sal_name);
            now_sal_distance = itemView.findViewById(R.id.now_sal_distance);
            nw_sal_address = itemView.findViewById(R.id.nw_sal_address);
            nw_sal_discount = itemView.findViewById(R.id.nw_sal_discount);
            mNow_slider = itemView.findViewById(R.id.now_slider);
        }
    }

    public void onTimeSet(String mTime) {

        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);//First argument will be the format
        //String timeFormat = simpleDateFormat.format(mTime);
        Log.d("time is",mTime);

    }
}
