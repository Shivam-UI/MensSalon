package com.createdinam.saloon.global;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.createdinam.saloon.R;
import com.createdinam.saloon.user.UserHomeActivity;
import com.createdinam.saloon.user.nowlist.NowListActivity;

public class AlertErrorActivity extends AppCompatActivity {
    Button go_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_error);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_top));
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        // init
        go_home = findViewById(R.id.go_home);
        go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UserHomeActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), UserHomeActivity.class));
        finish();
    }
}
