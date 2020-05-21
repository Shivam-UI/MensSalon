package com.createdinam.saloon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.createdinam.saloon.global.InitFunction;
import com.createdinam.saloon.partner.PartnerHomeActivity;
import com.createdinam.saloon.user.UserHomeActivity;

import static com.createdinam.saloon.global.Global.MY_PREFS_NAME;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1500;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_top));
        }
        // Hide status bar
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (InitFunction.getInstance(getApplicationContext()).isNetworkAvaliable(SplashActivity.this)) {
                    if (prefs.getBoolean("is_user", false)) {
                        if (prefs.getString("user_type", "").toLowerCase().matches("user")) {
                            startActivity(new Intent(SplashActivity.this, UserHomeActivity.class));
                            finish();
                        } else if (prefs.getString("user_type", "").toLowerCase().matches("partner")) {
                            startActivity(new Intent(SplashActivity.this, PartnerHomeActivity.class));
                            finish();
                        }
                    } else {
                        startActivity(new Intent(SplashActivity.this, NumberActivity.class));
                        finish();
                    }
                }else {
                    Toast.makeText(SplashActivity.this, "You are not online!!!!", Toast.LENGTH_SHORT).show();
                    Log.v("Home", "############################You are not online!!!!");
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
