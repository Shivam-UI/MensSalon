package com.createdinam.saloon.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.createdinam.saloon.NumberActivity;
import com.createdinam.saloon.R;
import com.createdinam.saloon.global.CustomLoader;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.createdinam.saloon.global.Global.MY_PREFS_NAME;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    RequestQueue requestQueue;
    private static CustomLoader customLoader;
    // assign
    TextView btn_profile,btn_history,btn_talk_to_us,btn_faqs,btn_spendgenie,btn_preference;
    ImageView logout_btn,back_to_home;
    CircleImageView update_profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        customLoader = new CustomLoader(ProfileActivity.this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_top));
        }
        prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();
        // init
        btn_profile = findViewById(R.id.btn_profile);
        btn_history = findViewById(R.id.btn_history);
        btn_talk_to_us = findViewById(R.id.btn_talk_to_us);
        btn_faqs = findViewById(R.id.btn_faqs);
        btn_spendgenie = findViewById(R.id.btn_spendgenie);
        btn_preference = findViewById(R.id.btn_preference);
        logout_btn = findViewById(R.id.logout_btn);
        update_profile_image = findViewById(R.id.update_profile_image);
        back_to_home = findViewById(R.id.back_to_home);
        // onclick
        btn_profile.setOnClickListener(this);
        btn_history.setOnClickListener(this);
        btn_talk_to_us.setOnClickListener(this);
        btn_faqs.setOnClickListener(this);
        btn_spendgenie.setOnClickListener(this);
        btn_preference.setOnClickListener(this);
        logout_btn.setOnClickListener(this);
        update_profile_image.setOnClickListener(this);
        back_to_home.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_profile:
                setProfileViewEnable();
                break;
            case R.id.btn_history:
                setHistoryViewEnable();
                break;
            case R.id.btn_talk_to_us:
                setTalkViewEnable();
                break;
            case R.id.btn_faqs:
                setTalkViewEnable();
                break;
            case R.id.btn_spendgenie:
                setSpendgenieViewEnable();
                break;
            case R.id.btn_preference:
                setPreferenceViewEnable();
                break;
            case R.id.logout_btn:
                setLogoutViewEnable();
                break;
            case R.id.update_profile_image:
                setUpdateProfileViewEnable();
                break;
            case R.id.back_to_home:
                setBackToHomeViewEnable();
                break;
        }
    }

    private void setProfileViewEnable() {
        startActivity(new Intent(ProfileActivity.this,UpdateProfileActivity.class));
        finish();
    }
    private void setHistoryViewEnable() {
        Toast.makeText(this, "History Clicked!", Toast.LENGTH_SHORT).show();
    }
    private void setTalkViewEnable() {
        Toast.makeText(this, "Talk Clicked!", Toast.LENGTH_SHORT).show();
    }
    private void setFaqsViewEnable() {
        Toast.makeText(this, "Faqs Clicked!", Toast.LENGTH_SHORT).show();
    }
    private void setSpendgenieViewEnable() {
        Toast.makeText(this, "Spendgenie Clicked!", Toast.LENGTH_SHORT).show();
    }
    private void setPreferenceViewEnable() {
        Toast.makeText(this, "Preference Clicked!", Toast.LENGTH_SHORT).show();
    }
    private void setLogoutViewEnable() {
        Toast.makeText(this, "Logout Clicked!", Toast.LENGTH_SHORT).show();
    }
    private void setUpdateProfileViewEnable() {
        Toast.makeText(this, "Update Profile Clicked!", Toast.LENGTH_SHORT).show();
    }
    private void setBackToHomeViewEnable() {
        startActivity(new Intent(ProfileActivity.this,UserHomeActivity.class));
        finish();
    }
}
