package com.createdinam.saloon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.createdinam.saloon.global.CustomLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.createdinam.saloon.global.Global.MY_PREFS_NAME;
import static com.createdinam.saloon.global.Global.VerifyNumber;
import static com.createdinam.saloon.global.Global.access_token;

public class NumberActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    TextView enter_number;
    Button submit_number;
    RequestQueue requestQueue;
    private static CustomLoader customLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        customLoader = new CustomLoader(NumberActivity.this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_top));
        }
        prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();
        enter_number = findViewById(R.id.enter_number);
        submit_number = findViewById(R.id.submit_number);
        submit_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enter_number.getText().toString().matches("") || enter_number.getText().toString().length() != 10) {
                    enter_number.setError("Enter Valid Number");
                } else {
                    String Number = enter_number.getText().toString().trim();
                    getOTPWithPhone(Number);
                }
            }
        });
    }

    private void getOTPWithPhone(final String num) {
        customLoader.startLoadingDailog();
        final StringRequest generate_otp = new StringRequest(Request.Method.POST, VerifyNumber, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    int maxLogSize = 1000;
                    for (int i = 0; i <= response.length() / maxLogSize; i++) {
                        int start1 = i * maxLogSize;
                        int end = (i + 1) * maxLogSize;
                        end = end > response.length() ? response.length() : end;
                        Log.d("response", response.toString());
                    }
                    if (obj.get("status").toString().matches("true")) {
                        JSONObject otpdata = obj.getJSONObject("data");
                        String OTP = otpdata.getString("otp");
                        String OTP_ID = otpdata.getString("otpid");
                        String preference_status = otpdata.getString("preference_status");
                        String user_id = otpdata.getString("userid");
                        String type = otpdata.getString("directory");
                        Intent intent = new Intent(NumberActivity.this, OTPActivity.class);
                        intent.putExtra("otp", OTP);
                        intent.putExtra("otp_id", OTP_ID);
                        intent.putExtra("userid", user_id);
                        intent.putExtra("type", type);
                        startActivity(intent);
                        finish();
                        customLoader.stopLoadingDailog();
                    }

                } catch (Exception ex) {
                    ex.getMessage();
                    customLoader.stopLoadingDailog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "" + error.getMessage());
                customLoader.stopLoadingDailog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("access_token", "" + access_token);
                param.put("phone", "" + num);
                return param;
            }
        };
        requestQueue.add(generate_otp);
    }
}
