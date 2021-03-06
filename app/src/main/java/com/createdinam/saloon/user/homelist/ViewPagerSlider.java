package com.createdinam.saloon.user.homelist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.createdinam.saloon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPagerSlider extends PagerAdapter {
    private static final String TAG = "ViewPagerSlider";
    Context mContext;
    String[] mImages;
    LayoutInflater mInflater;
    ArrayList<String> urlImages;

    public ViewPagerSlider(Context context, String[] mImages, ArrayList<String> imgURL) {
        this.mContext = context;
        this.mImages = mImages;
        this.urlImages = imgURL;
    }

    @Override
    public int getCount() {
        return urlImages.size();
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

        final ImageView imageView;
        imageView = (ImageView)itemView.findViewById(R.id.slider_items_view);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        try{

            //Glide.with(mContext.getApplicationContext()).load(Uri.parse(urlImages.get(position))).error(R.drawable.splash_background).into(imageView);

            //Picasso.with(mContext.getApplicationContext()).load(urlImages.get(position)).into(imageView);

            Glide.with(mContext).load(urlImages.get(position))
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
