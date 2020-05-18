package com.createdinam.saloon.user.laterlist.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.createdinam.saloon.R;
import com.createdinam.saloon.user.itemdetails.SaloonItensDetailsActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LaterSalonAdapter extends RecyclerView.Adapter<LaterSalonAdapter.LaterListHolder> {
    LaterPagerSlider laterPagerSlider;
    ArrayList<LaterModel> mLaterList = new ArrayList<LaterModel>();
    Context mContext;
    Timer timer;

    public LaterSalonAdapter(ArrayList<LaterModel> mNowList, Context mContext) {
        this.mLaterList = mNowList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public LaterListHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.later_list_items_view,parent,false);
        return new LaterListHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final LaterListHolder holder, final int position) {
        // get data from list
        String id = mLaterList.get(position).getSalon_id();
        String sal_name = mLaterList.get(position).getSalon_name();
        String sal_distance = mLaterList.get(position).getDistance();
        String sal_address = mLaterList.get(position).getAddress();
        String sal_discount = mLaterList.get(position).getDiscount();
        String sal_rating = mLaterList.get(position).getRating();
        String sal_images = mLaterList.get(position).getImage();
        String sal_available = mLaterList.get(position).getAvailability();

        // set discount
        if(sal_discount.trim().matches("")){
            sal_discount = "0%";
            holder.later_sal_discount.setText("Discount "+sal_discount);
        }else{
            holder.later_sal_discount.setText("Discount Upto "+sal_discount);
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
        // set Pager Slider
        laterPagerSlider = new LaterPagerSlider(ListImagesSlider,mContext);
        holder.later_slider.setAdapter(laterPagerSlider);

        // add slider interval
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                holder.later_slider.post(new Runnable(){

                    @Override
                    public void run() {
                        holder.later_slider.setCurrentItem((holder.later_slider.getCurrentItem()+1)%ListImagesSlider.size());
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);

        // set data to list
        holder.later_sal_name.setText(sal_name);
        holder.later_sal_distance.setText(sal_distance);
        holder.later_sal_address.setText(sal_address);
        holder.later_sal_rating.setRating(Float.parseFloat(sal_rating));

        // add click event
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailsView = new Intent(mContext.getApplicationContext(), SaloonItensDetailsActivity.class);
                detailsView.putExtra("salon_id",""+mLaterList.get(position).getSalon_id());
                detailsView.putExtra("lat",""+mLaterList.get(position).getLatitude());
                detailsView.putExtra("long",""+mLaterList.get(position).getLongitude());
                detailsView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(detailsView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLaterList.size();
    }

    public class LaterListHolder extends RecyclerView.ViewHolder{
        ViewPager later_slider;
        TextView later_sal_name,later_sal_distance,later_sal_address,later_sal_discount;
        RatingBar later_sal_rating;
        public LaterListHolder(@NonNull View itemView) {
            super(itemView);
            later_sal_rating = itemView.findViewById(R.id.later_sal_rating);
            later_slider = itemView.findViewById(R.id.later_slider);
            later_sal_name = itemView.findViewById(R.id.later_sal_name);
            later_sal_distance = itemView.findViewById(R.id.later_sal_distance);
            later_sal_address = itemView.findViewById(R.id.later_sal_address);
            later_sal_discount = itemView.findViewById(R.id.later_sal_discount);
        }
    }
}
