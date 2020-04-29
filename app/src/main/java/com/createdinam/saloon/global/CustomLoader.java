package com.createdinam.saloon.global;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import com.createdinam.saloon.R;

public class CustomLoader {
    Activity mActivity;
    AlertDialog mAlertDialog;

    public CustomLoader(Activity activity){
        mActivity = activity;
    }

    public void startLoadingDailog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater =  mActivity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_alert_dailog,null));
        builder.setCancelable(false);
        mAlertDialog = builder.create();
        mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mAlertDialog.show();
    }

    public void stopLoadingDailog(){
        mAlertDialog.dismiss();
    }
}
