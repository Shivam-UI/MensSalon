package com.createdinam.saloon.user.homelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.createdinam.saloon.R;
import com.createdinam.saloon.global.GlobalAppContextSingleton;
import com.createdinam.saloon.user.HomeListModel;
import com.createdinam.saloon.user.UserHomeActivity;
import com.daman.library.SimpleRatingBar;
import com.ouattararomuald.slider.ImageSlider;
import com.ouattararomuald.slider.SliderAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HListHolder> {
    ViewPagerSlider mViewPagerSlider;
    ArrayList<HomeListModel> mHlist = new ArrayList<HomeListModel>();
    Context mContext;
    Timer timer;
    private static final String TAG = "HomeListAdapter";
    public HomeListAdapter(ArrayList<HomeListModel> mHlist, Context mContext) {
        this.mHlist = mHlist;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_salon_list,parent,false);
        return new HListHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final HListHolder holder, int position) {
        String sid = mHlist.get(position).getSalon_id();
        String s_uid = mHlist.get(position).getSalon_unique_id();
        String sname = mHlist.get(position).getSalon_name();
        String sadd = mHlist.get(position).getAddress();
        String sdiscount = mHlist.get(position).getDiscount();
        String sdistance = mHlist.get(position).getDistance();
        String simage = mHlist.get(position).getImage();
        String srating = mHlist.get(position).getRating();

        String[] images = simage.split(", ");
        String url = "";
        final ArrayList<String> ListImagesSlider = new ArrayList<String>();
        for (int i = 0;i< images.length;i++){
            url = images[i].replaceAll("\\s","%20");
            Log.d("URL",""+url);
            ListImagesSlider.add(url);
        }

        // set adapter to view pager
        mViewPagerSlider = new ViewPagerSlider(mContext,images,ListImagesSlider);
        holder.salon_slider.setAdapter(mViewPagerSlider);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                holder.salon_slider.post(new Runnable(){

                    @Override
                    public void run() {
                        holder.salon_slider.setCurrentItem((holder.salon_slider.getCurrentItem()+1)%ListImagesSlider.size());
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);

        // set value
        if(sdiscount.trim().matches("")){
            sdiscount = "0%";
            holder.sal_discount.setText("Discount "+sdiscount);
        }else{
            holder.sal_discount.setText("Discount Upto "+sdiscount);
        }

        // set data
        holder.sal_name.setText(sname);
        holder.sal_add.setText(sadd);
        holder.sal_distnc.setText(sdistance);
        holder.sal_rating.setRating(Integer.parseInt(srating));
    }

    @Override
    public int getItemCount() {
        return mHlist!=null ?mHlist.size() :0;
    }

    public class HListHolder extends RecyclerView.ViewHolder{
        ViewPager salon_slider;
        TextView sal_name,sal_distnc,sal_add,sal_discount;
        RatingBar sal_rating;
        public HListHolder(@NonNull View itemView) {
            super(itemView);
            salon_slider = itemView.findViewById(R.id.salon_slider);
            sal_name = itemView.findViewById(R.id.salon_name);
            sal_discount = itemView.findViewById(R.id.salon_discount);
            sal_rating = itemView.findViewById(R.id.salon_rating);
            sal_add = itemView.findViewById(R.id.salon_address);
            sal_distnc = itemView.findViewById(R.id.saloon_distance);
            sal_rating.setNumStars(5);
        }
    }
}
