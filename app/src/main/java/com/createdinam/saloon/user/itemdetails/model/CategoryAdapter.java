package com.createdinam.saloon.user.itemdetails.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.createdinam.saloon.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CatItemHolder> {

    Context mContext;
    ArrayList<CategoryModel> categoryModels = new ArrayList<CategoryModel>();

    public CategoryAdapter(ArrayList<CategoryModel> categoryModels,Context mContext) {
        this.mContext = mContext;
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public CatItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView =LayoutInflater.from(parent.getContext()).inflate(R.layout.category_details_list_items,parent,false);
        return new CatItemHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CatItemHolder holder, int position) {
        String name = categoryModels.get(position).getName();
        String ImagesUrl = categoryModels.get(position).getCategory_image();
        // set data to view
        holder.category_name.setText(name);
        Glide.with(holder.category_item_images.getContext()).load(ImagesUrl).error(R.drawable.splash_background_crop).into(holder.category_item_images);
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class  CatItemHolder extends RecyclerView.ViewHolder{
        CircleImageView category_item_images;
        TextView category_name;
        public CatItemHolder(@NonNull View itemView) {
            super(itemView);
            category_item_images = itemView.findViewById(R.id.category_item_images);
            category_name = itemView.findViewById(R.id.category_name);
        }
    }
}
