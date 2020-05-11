package com.createdinam.saloon.user.homelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.createdinam.saloon.R;
import com.createdinam.saloon.global.GlobalAppContextSingleton;
import com.createdinam.saloon.user.HomeListModel;
import com.createdinam.saloon.user.UserHomeActivity;
import com.ouattararomuald.slider.ImageSlider;
import com.ouattararomuald.slider.SliderAdapter;

import java.util.ArrayList;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HListHolder> {
    ViewPagerSlider mViewPagerSlider;
    ArrayList<HomeListModel> mHlist = new ArrayList<HomeListModel>();
    Context mContext;
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
    public void onBindViewHolder(@NonNull HListHolder holder, int position) {
        String sid = mHlist.get(position).getSalon_id();
        String s_uid = mHlist.get(position).getSalon_unique_id();
        String sname = mHlist.get(position).getSalon_name();
        String sadd = mHlist.get(position).getAddress();
        String sdiscount = mHlist.get(position).getDiscount();
        String sdistance = mHlist.get(position).getDistance();
        String simage = mHlist.get(position).getImage();
        String srating = mHlist.get(position).getRating();

        Log.d("rating",""+srating);
        String[] images = simage.split(",");
        String[] ListImagesSlider;
        for (String SliderImages: images){
            SliderImages.replaceAll("\\s"," ");
            Log.d(TAG, "onBindViewHolder: "+SliderImages);
        }

        mViewPagerSlider = new ViewPagerSlider(mContext,images);

        // set value
        if(sdiscount.trim().matches("")){
            sdiscount = "0%";
        }

        // set data
        holder.sal_name.setText(sname);
        holder.sal_add.setText(sadd);
        holder.sal_discount.setText("Discount "+sdiscount);
        holder.sal_distnc.setText(sdistance);
        holder.sal_rating.setRating(Integer.parseInt(srating));
        holder.salon_slider.setAdapter(mViewPagerSlider);
    }

    private void setFlipperImage(Uri res, ViewFlipper mv) {
        Log.i("Set Filpper Called", res+"");
        ImageView image = new ImageView(mContext.getApplicationContext());
        image.setImageURI(res);
        mv.addView(image);
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
        }
    }
}
