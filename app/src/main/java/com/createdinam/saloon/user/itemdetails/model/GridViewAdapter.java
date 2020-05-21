package com.createdinam.saloon.user.itemdetails.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.createdinam.saloon.R;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context ctx;
    private ImageView ivGallery;
    private TextView textView;
    ArrayList<CategoryModel> arrayList = new ArrayList<CategoryModel>();
    Context mContext;

    public GridViewAdapter(ArrayList<CategoryModel> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.category_details_list_items, parent, false);
        ivGallery = (ImageView) itemView.findViewById(R.id.category_item_images);
        textView = (TextView) itemView.findViewById(R.id.category_name);
        // set data to view
        textView.setText(arrayList.get(position).getName());
        // set images to pager
        Glide.with(ivGallery.getContext()).load(arrayList.get(position).category_image).error(R.drawable.splash_background_crop).into(ivGallery);
        return itemView;
    }
}
