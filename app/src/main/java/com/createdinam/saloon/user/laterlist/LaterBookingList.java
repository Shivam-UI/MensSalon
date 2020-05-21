package com.createdinam.saloon.user.laterlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.createdinam.saloon.R;
import com.createdinam.saloon.global.CustomLoader;
import com.createdinam.saloon.global.Global;
import com.createdinam.saloon.global.InitFunction;
import com.createdinam.saloon.global.calender.CustomCalender;
import com.createdinam.saloon.user.ProfileActivity;
import com.createdinam.saloon.user.UserHomeActivity;
import com.createdinam.saloon.user.laterlist.model.LaterModel;
import com.createdinam.saloon.user.laterlist.model.LaterSalonAdapter;
import com.createdinam.saloon.user.nowlist.NowListActivity;
import com.createdinam.saloon.user.nowlist.model.NowModel;
import com.createdinam.saloon.user.nowlist.model.NowSalonAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

import static com.createdinam.saloon.global.Global.MY_PREFS_NAME;

public class LaterBookingList extends AppCompatActivity implements View.OnClickListener {
    private static String USER_ID;
    private static CustomLoader customLoader;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    RequestQueue requestQueue;
    // bottom navigation res
    RelativeLayout btn_lay_now, btn_lay_map, btn_lay_list, btn_lay_later,btn_home_list;
    // top navigation
    ImageView back_button;
    TextView logo_imageView,txt_later,txt_now;
    // recycler view items
    RecyclerView book_later_list;
    // save data to ArrayList
    private ArrayList<LaterModel> LaterBookingL = new ArrayList<LaterModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_later_booking_list);
        prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();
        customLoader = new CustomLoader(LaterBookingList.this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_top));
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        USER_ID = prefs.getString("user_id", "");
        // inti list
        book_later_list = findViewById(R.id.book_later_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        book_later_list.setLayoutManager(layoutManager);
        book_later_list.setItemAnimator(new SlideInUpAnimator());
        book_later_list.setHasFixedSize(true);
        // init other layout component
        back_button = findViewById(R.id.back_button);
        btn_lay_now = findViewById(R.id.btn_lay_now);
        btn_lay_map = findViewById(R.id.btn_lay_map);
        btn_lay_list = findViewById(R.id.btn_lay_list);
        btn_lay_later = findViewById(R.id.btn_lay_later);
        btn_home_list = findViewById(R.id.btn_home_list);
        logo_imageView = findViewById(R.id.logo_imageView);
        txt_later = findViewById(R.id.txt_later);
        txt_now = findViewById(R.id.txt_now);
        // bind click listener
        back_button.setOnClickListener(this);
        btn_lay_now.setOnClickListener(this);
        btn_lay_map.setOnClickListener(this);
        btn_lay_list.setOnClickListener(this);
        btn_lay_later.setOnClickListener(this);
        // set UI update
        btn_lay_later.setBackgroundColor(Color.GRAY);
        logo_imageView.setText("Umm. May be later");
        txt_now.setText("Book Now");
        txt_later.setText("Book Later");

        if (InitFunction.getInstance(getApplicationContext()).isNetworkAvaliable(LaterBookingList.this)) {
            customLoader.startLoadingDailog();
            // start method for list
            setLaterBookingList();
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                setProfileView();
                break;
            case R.id.btn_lay_now:
                setNowView();
                break;
            case R.id.btn_lay_map:
                setMapView();
                break;
            case R.id.btn_lay_list:
                setListView();
                break;
            case R.id.btn_lay_later:
                setLaterView();
                break;
        }
    }

    private void setLaterView() {
       // Toast.makeText(this, "Later", Toast.LENGTH_SHORT).show();
    }

    private void setMapView() {
        CustomCalender customCalender = new CustomCalender(this);
        customCalender.showCalendar();
    }

    private void setListView() {
        startActivity(new Intent(LaterBookingList.this, UserHomeActivity.class));
        finish();
    }

    private void setNowView() {
        startActivity(new Intent(LaterBookingList.this,NowListActivity.class));
        finish();
    }

    private void setLaterBookingList(){
        StringRequest nowListRequest = new StringRequest(Request.Method.POST, Global.Saloon_Later_List, new Response.Listener<String>() {
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
                        //Log.d("response", response.toString());
                    }
                    if (obj.getString("status").matches("true")) {
                        JSONArray homelist = obj.getJSONArray("data");
                        for (int i = 0; i < homelist.length(); i++) {
                            LaterModel laterModel = new LaterModel();
                            JSONObject listObj = homelist.getJSONObject(i);
                            laterModel.setSalon_id(listObj.getString("salon_id"));
                            laterModel.setSalon_unique_id(listObj.getString("salon_unique_id"));
                            laterModel.setSalon_name(listObj.getString("salon_name"));
                            laterModel.setImage(listObj.getString("image"));
                            laterModel.setDiscount(listObj.getString("discount"));
                            laterModel.setRating(listObj.getString("rating"));
                            laterModel.setAddress(listObj.getString("address"));
                            laterModel.setLatitude(listObj.getString("latitude"));
                            laterModel.setLongitude(listObj.getString("longitude"));
                            laterModel.setAvailability(listObj.getString("availability"));
                            laterModel.setDistance(listObj.getString("distance"));
                            laterModel.setFeatured_flag(listObj.getString("featured_flag"));
                            LaterBookingL.add(laterModel);
                        }
                        book_later_list.setAdapter(new LaterSalonAdapter(LaterBookingL,getApplicationContext()));
                        customLoader.stopLoadingDailog();
                    }else{
                        Log.d("error", "No Data Found");
                        customLoader.stopLoadingDailog();
                    }
                }catch (Exception ex){
                    Log.d("error", ex.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customLoader.stopLoadingDailog();
                Log.d("error", "onResponse: "+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("access_token",Global.access_token);
                param.put("lat", "28.467539");
                param.put("long", "77.082108");
                param.put("datetime", "31-03-2020 6:00PM");
                return param;
            }
        };
        requestQueue.add(nowListRequest);
    }

    private void setProfileView() {
        startActivity(new Intent(LaterBookingList.this, ProfileActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LaterBookingList.this,UserHomeActivity.class));
        finish();
    }
}
