package com.createdinam.saloon.user.homelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.createdinam.saloon.R;
import com.createdinam.saloon.user.SalonModel;

import java.util.ArrayList;

public class HotListAdapter extends RecyclerView.Adapter<HotListAdapter.HotListHolder> {

    ArrayList<SalonModel> sModels = new ArrayList<SalonModel>();
    Context mContext;

    public HotListAdapter(ArrayList<SalonModel> sModels, Context mContext) {
        this.sModels = sModels;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HotListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView =LayoutInflater.from(parent.getContext()).inflate(R.layout.hotlist_items,parent,false);
        return new HotListHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull HotListHolder holder, int position) {

        String name = sModels.get(position).getSalonName();
        String id = sModels.get(position).getSalonId();
        String s_id = sModels.get(position).getSalonUniqueId();
        String img_url = sModels.get(position).getImage();
        String discount = sModels.get(position).getDiscount();
        // set images
        holder.list_name.setText(name);
        Glide.with(holder.list_image.getContext()).load(img_url).into(holder.list_image);
    }

    @Override
    public int getItemCount() {
        return sModels!=null ?sModels.size() :0;
    }

    class HotListHolder extends RecyclerView.ViewHolder {
        ImageView list_image;
        TextView list_name;

        public HotListHolder(@NonNull View itemView) {
            super(itemView);
            list_image = itemView.findViewById(R.id.hot_list_image);
            list_name = itemView.findViewById(R.id.hot_list_name);
        }
    }
}
