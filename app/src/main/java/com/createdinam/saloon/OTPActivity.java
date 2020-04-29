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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.createdinam.saloon.global.CustomLoader;
import com.createdinam.saloon.partner.PartnerHomeActivity;
import com.createdinam.saloon.user.UserHomeActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.createdinam.saloon.global.Global.MY_PREFS_NAME;
import static com.createdinam.saloon.global.Global.VerifyNumber;
import static com.createdinam.saloon.global.Global.VerifyOTP;
import static com.createdinam.saloon.global.Global.access_token;

public class OTPActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    EditText enter_otp;
    Button submit_otp,resend_otp;
    private static String user_otp,user_type,user_otp_id,user_id;
    RequestQueue requestQueue;
    private static CustomLoader customLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        customLoader = new CustomLoader(OTPActivity.this);
        Intent intent = getIntent();
        user_type = intent.getStringExtra("type");
        user_otp = intent.getStringExtra("otp");
        user_otp_id = intent.getStringExtra("otp_id");
        user_id = intent.getStringExtra("userid");
        enter_otp = findViewById(R.id.enter_otp);
        enter_otp.setText(user_otp);
        submit_otp = findViewById(R.id.submit_otp);
        resend_otp = findViewById(R.id.resend_otp);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_top));
        }
        submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customLoader.startLoadingDailog();
                if (enter_otp.getText().toString().trim().matches("")||enter_otp.getText().toString().trim().length()==0){
                    enter_otp.setError("Enter Valid OTP");
                }else{
                    setOTPVarification(enter_otp.getText().toString().trim(),user_otp_id,user_type,user_id);
                }
            }
        });

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResendOTP();
            }
        });
    }

    private void setResendOTP() {

    }

    private void setOTPVarification(final String otp, final String id,final String type,final String u_id) {
        final StringRequest varify_otp = new StringRequest(Request.Method.POST, VerifyOTP, new Response.Listener<String>() {
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
                    if (obj.getString("status").matches("true")){
                        customLoader.stopLoadingDailog();
                        editor.putBoolean("is_user",true);
                        editor.putString("user_type",type.trim());
                        editor.putString("user_id",u_id.trim());
                        editor.commit();
                        if (type.matches("User")){
                            startActivity(new Intent(OTPActivity.this, UserHomeActivity.class));
                            finish();
                        }else if(type.matches("partner")){
                            startActivity(new Intent(OTPActivity.this, PartnerHomeActivity.class));
                            finish();
                        }
                    }else {
                        Toast.makeText(OTPActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    Log.d("error",""+ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",""+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("access_token", "" + access_token);
                param.put("otp", "" + otp);
                param.put("id", "" + id);
                return param;
            }
        };
        requestQueue.add(varify_otp);
    }
}
