package com.createdinam.saloon.user.homelist;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.createdinam.saloon.R;
import com.squareup.picasso.Picasso;

public class ViewPagerSlider extends PagerAdapter {
    private static final String TAG = "ViewPagerSlider";
    Context mContext;
    String[] mImages;
    LayoutInflater mInflater;

    public ViewPagerSlider(Context context, String[] mImages) {
        this.mContext = context;
        this.mImages = mImages;
    }

    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        mInflater = (LayoutInflater) mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mInflater.inflate(R.layout.view_pager_slider_items,container,false);

        ImageView imageView;
        imageView = (ImageView)itemView.findViewById(R.id.slider_items_view);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//        imageView.setMinimumHeight(height);
//        imageView.setMinimumWidth(width);
        try{
            Picasso.with(mContext.getApplicationContext())
                    .load(mImages[position])
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        }catch (Exception ex){
            Log.d(TAG, "instantiateItem: "+ex.getMessage());
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }
}
