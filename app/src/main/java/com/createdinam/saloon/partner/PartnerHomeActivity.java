package com.createdinam.saloon.partner;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.os.Bundle;

import com.createdinam.saloon.R;

import static android.content.Context.MODE_PRIVATE;
import static com.createdinam.saloon.global.Global.MY_PREFS_NAME;

public class PartnerHomeActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_home);
        prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();
    }
}
