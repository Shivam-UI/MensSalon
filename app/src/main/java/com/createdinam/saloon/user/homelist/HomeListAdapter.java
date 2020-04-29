package com.createdinam.saloon.user.homelist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.createdinam.saloon.R;
import com.createdinam.saloon.user.HomeListModel;

import java.util.ArrayList;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HListHolder> {

    ArrayList<HomeListModel> mHlist = new ArrayList<HomeListModel>();
    Context mContext;

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

        Log.d("discount",""+sdiscount);

        // set value
        if(sdiscount.trim().matches("")){
            sdiscount = "0%";
        }

        // set data
        holder.sal_name.setText(sname);
        holder.sal_add.setText(sadd);
        holder.sal_discount.setText("Discount "+sdiscount);
        holder.sal_distnc.setText(sdistance);
        holder.sal_rating.setNumStars(Integer.parseInt(srating));
    }

    @Override
    public int getItemCount() {
        return mHlist!=null ?mHlist.size() :0;
    }

    public class HListHolder extends RecyclerView.ViewHolder{
        ImageView sal_img;
        TextView sal_name,sal_distnc,sal_add,sal_discount;
        RatingBar sal_rating;
        public HListHolder(@NonNull View itemView) {
            super(itemView);
            sal_img = itemView.findViewById(R.id.salon_image);
            sal_name = itemView.findViewById(R.id.salon_name);
            sal_discount = itemView.findViewById(R.id.salon_discount);
            sal_rating = itemView.findViewById(R.id.salon_rating);
            sal_add = itemView.findViewById(R.id.salon_address);
            sal_distnc = itemView.findViewById(R.id.saloon_distance);
        }
    }
}
