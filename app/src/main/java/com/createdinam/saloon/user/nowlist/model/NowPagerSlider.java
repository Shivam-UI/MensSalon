package com.createdinam.saloon.user.nowlist.model;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.createdinam.saloon.R;

import java.util.ArrayList;

public class NowPagerSlider extends PagerAdapter {
    LayoutInflater mInflater;
    ArrayList<String> imageList;
    Context mContext;
    AlertDialog mAlertDialog;

    public NowPagerSlider(ArrayList<String> imageList, Context mContext) {
        this.imageList = imageList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        mInflater = (LayoutInflater) mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mInflater.inflate(R.layout.view_pager_slider_items, container, false);
        final ImageView imageView;
        imageView = (ImageView) itemView.findViewById(R.id.slider_items_view);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        // set images to pager
        try {
            Glide.with(mContext).load(imageList.get(position))
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .fitCenter()
                    .error(R.drawable.splash_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(imageView);

        } catch (Exception ex) {
            Log.d("error", "instantiateItem: " + ex.getMessage());
        }
        // click event
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create alert dialog
                Dialog mdialog = new Dialog(mContext);
                mdialog.setContentView(R.layout.alert_image_dailog);
                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                // set view
                ImageView iv = mdialog.findViewById(R.id.slider_single_images);
                Glide.with(mContext).load(imageList.get(position))
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .fitCenter()
                        .error(R.drawable.splash_background)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(iv);
                // assign list to view
                mdialog.setCancelable(false);
                // show the dialog
                //mdialog.show();
                Log.d("slider_image", "->" + imageList.get(position));
            }
        });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
