package com.createdinam.saloon.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.createdinam.saloon.NumberActivity;
import com.createdinam.saloon.R;
import com.createdinam.saloon.global.CustomLoader;
import com.createdinam.saloon.user.itemdetails.SaloonItensDetailsActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.createdinam.saloon.global.Global.MY_PREFS_NAME;
import static com.createdinam.saloon.global.Global.Show_profile;
import static com.createdinam.saloon.global.Global.Update_profile;
import static com.createdinam.saloon.global.Global.access_token;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private long lastPressedTime;
    private static final int PERIOD = 2000;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    RequestQueue requestQueue;
    private static CustomLoader customLoader;
    TextInputEditText edt_name, edt_birthday, edt_anniversary, edt_partner_bday, edt_papa_bday, edt_mumma_bday;
    ImageView save_profile_btn, back_button;
    private static String U_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        customLoader = new CustomLoader(UpdateProfileActivity.this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_top));
        }
        prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();
        U_ID = prefs.getString("user_id", "");
        // define id's
        save_profile_btn = findViewById(R.id.save_profile_btn);
        back_button = findViewById(R.id.back_button);
        edt_name = findViewById(R.id.edt_name);
        edt_birthday = findViewById(R.id.edt_birthday);
        edt_anniversary = findViewById(R.id.edt_anniversary);
        edt_partner_bday = findViewById(R.id.edt_partner_bday);
        edt_papa_bday = findViewById(R.id.edt_papa_bday);
        edt_mumma_bday = findViewById(R.id.edt_mumma_bday);
        // init
        save_profile_btn.setOnClickListener(this);
        back_button.setOnClickListener(this);
        // show profile
        viewUserDetails(U_ID);
    }

    private void viewUserDetails(String uId) {
        customLoader.startLoadingDailog();
        StringRequest profile_update = new StringRequest(Request.Method.POST, Show_profile, new Response.Listener<String>() {
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
                    Log.d("data", "" + obj.getString("status"));
                    if (obj.getString("status").matches("true")) {
                        JSONObject jsonObject = obj.getJSONObject("data");
                        String full_name = jsonObject.getString("full_name");
                        String birthday = jsonObject.getString("birthday");
                        String anniversary = jsonObject.getString("anniversary");
                        String spouse_birthday = jsonObject.getString("spouse_birthday");
                        String mumma_birthday = jsonObject.getString("mumma_birthday");
                        String papa_birthday = jsonObject.getString("papa_birthday");
                        edt_name.setText(full_name);
                        edt_birthday.setText(birthday);
                        edt_anniversary.setText(anniversary);
                        edt_partner_bday.setText(spouse_birthday);
                        edt_papa_bday.setText(papa_birthday);
                        edt_mumma_bday.setText(mumma_birthday);
                        customLoader.stopLoadingDailog();
                    } else {
                        Toast.makeText(UpdateProfileActivity.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Log.d("error", "" + ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("access_token", "" + access_token);
                param.put("userid", "" + U_ID);
                return param;
            }
        };
        requestQueue.add(profile_update);
    }

    public Boolean checkDateFormat(String date){
        if (date == null || !date.matches("^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$"))
            return false;
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        try {
            format.parse(date);
            return true;
        }catch (ParseException e){
            return false;
        }
    }

    public boolean isUserValidate() {
        if (Objects.requireNonNull(edt_name.getText()).toString().length() == 0) {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Objects.requireNonNull(edt_birthday.getText()).toString().length() == 0) {
            Toast.makeText(this, "invalid Birth day", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Objects.requireNonNull(edt_anniversary.getText()).toString().length() == 0) {
            Toast.makeText(this, "Enter Anniversary", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Objects.requireNonNull(edt_partner_bday.getText()).toString().length() == 0) {
            Toast.makeText(this, "Enter Partner Bday", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Objects.requireNonNull(edt_papa_bday.getText()).toString().length() == 0) {
            Toast.makeText(this, "Enter Papa bday", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Objects.requireNonNull(edt_mumma_bday.getText()).toString().length() == 0) {
            Toast.makeText(this, "Enter Mumma bday", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                backToDashboard();
                break;
            case R.id.save_profile_btn:
                saveUserDetails();
                break;
        }
    }

    private void backToDashboard() {
        startActivity(new Intent(UpdateProfileActivity.this, UserHomeActivity.class));
        finish();
    }

    private void saveUserDetails() {
        customLoader.startLoadingDailog();
        if (isUserValidate()) {
            StringRequest profile_update = new StringRequest(Request.Method.POST, Update_profile, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("data", "" + response);
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
                            String message = obj.getString("message").trim();
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
                            builder.setMessage(message);
                            builder.setPositiveButton(R.string.txt_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(UpdateProfileActivity.this,UserHomeActivity.class));
                                    finish();
                                }
                            });
                            builder.setNegativeButton(R.string.txt_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else{
                            Toast.makeText(UpdateProfileActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                            customLoader.stopLoadingDailog();
                        }
                    } catch (Exception ex) {
                        Log.d("error",""+ex.getMessage());
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
                    param.put("userid", "" + U_ID);
                    param.put("fullname", "" + edt_name.getText().toString().trim());
                    param.put("papa_bday", "" + edt_papa_bday.getText().toString().trim());
                    param.put("mumma_bday", "" + edt_mumma_bday.getText().toString().trim());
                    param.put("bday", "" + edt_birthday.getText().toString().trim());
                    param.put("partner_bday", "" + edt_partner_bday.getText().toString().trim());
                    param.put("anniversary", "" + edt_anniversary.getText().toString().trim());
                    Log.d("param", "" + param.toString());
                    return param;
                }
            };
            requestQueue.add(profile_update);
        } else {
            customLoader.stopLoadingDailog();
        }
    }

    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
                        builder.setMessage("Do You Really Want To Exit");
                        builder.setPositiveButton(R.string.txt_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.setNegativeButton(R.string.txt_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Press again to exit.",
                                Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    } */

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UpdateProfileActivity.this,ProfileActivity.class));
        finish();
    }
}
