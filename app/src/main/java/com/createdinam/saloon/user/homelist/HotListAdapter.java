package com.createdinam.saloon.user.homelist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
    private Activity parentActivity;
    ArrayList<SalonModel> sModels = new ArrayList<SalonModel>();
    Context mContext;
    AlertDialog alertDialog;

    public HotListAdapter(ArrayList<SalonModel> sModels, Context mContext, Activity pActivity) {
        this.sModels = sModels;
        this.mContext = mContext;
        this.parentActivity = pActivity;
    }

    @NonNull
    @Override
    public HotListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotlist_items, parent, false);
        return new HotListHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull HotListHolder holder, int position) {

        final String name = sModels.get(position).getSalonName();
        String id = sModels.get(position).getSalonId();
        String s_id = sModels.get(position).getSalonUniqueId();
        String img_url = sModels.get(position).getImage();
        final String discount = sModels.get(position).getDiscount();
        // set images
        //holder.list_name.setText(name);
        Log.d("image","added");
        Glide.with(holder.list_image.getContext()).load(img_url).error(R.drawable.splash_background_crop).into(holder.list_image);
        Glide.with(holder.hot_list_image_two.getContext()).load(img_url).error(R.drawable.splash_background_crop).into(holder.hot_list_image_two);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                LayoutInflater inflater = parentActivity.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.short_description_popup,null));
                builder.setCancelable(true);
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog = builder.create();
                alertDialog.setTitle(name);
                alertDialog.setMessage(name + discount);
                //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sModels != null ? sModels.size() : 0;
    }

    class HotListHolder extends RecyclerView.ViewHolder {
        ImageView list_image,hot_list_image_two;
        //TextView list_name;

        public HotListHolder(@NonNull View itemView) {
            super(itemView);
            list_image = itemView.findViewById(R.id.hot_list_image);
            hot_list_image_two = itemView.findViewById(R.id.hot_list_image_two);
        }
    }
}
