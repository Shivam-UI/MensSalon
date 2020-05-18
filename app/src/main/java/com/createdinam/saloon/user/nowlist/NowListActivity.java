package com.createdinam.saloon.user.nowlist;

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
import com.createdinam.saloon.global.AlertErrorActivity;
import com.createdinam.saloon.global.CustomLoader;
import com.createdinam.saloon.global.Global;
import com.createdinam.saloon.global.InitFunction;
import com.createdinam.saloon.user.ProfileActivity;
import com.createdinam.saloon.user.UserHomeActivity;
import com.createdinam.saloon.user.laterlist.LaterBookingList;
import com.createdinam.saloon.user.nowlist.model.NowModel;
import com.createdinam.saloon.user.nowlist.model.NowSalonAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.createdinam.saloon.R.id.home_salon_list_view;
import static com.createdinam.saloon.global.Global.MY_PREFS_NAME;

public class NowListActivity extends AppCompatActivity implements View.OnClickListener {

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
    RecyclerView ry_now_list_items;
    // save data to ArrayList
    private ArrayList<NowModel> NowListDB = new ArrayList<NowModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_list);
        prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();
        customLoader = new CustomLoader(NowListActivity.this);
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
        ry_now_list_items = findViewById(R.id.ry_now_list_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        ry_now_list_items.setLayoutManager(layoutManager);
        ry_now_list_items.setHasFixedSize(true);
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
        btn_lay_now.setBackgroundColor(Color.GRAY);
        logo_imageView.setText("Hmm Are you ready");
        txt_now.setText("Book Now");
        txt_later.setText("Book Later");
        if (InitFunction.getInstance(getApplicationContext()).isNetworkAvaliable(NowListActivity.this)) {
            //customLoader.startLoadingDailog();
            setNowListView();
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
        startActivity(new Intent(NowListActivity.this, LaterBookingList.class));
        finish();
    }

    private void setMapView() {
        Toast.makeText(this, "Map", Toast.LENGTH_SHORT).show();
    }

    private void setListView() {
        startActivity(new Intent(NowListActivity.this,UserHomeActivity.class));
        finish();
    }

    private void setNowView() {

    }

    private void setNowListView(){
        StringRequest nowListRequest = new StringRequest(Request.Method.POST, Global.Saloon_Now_List, new Response.Listener<String>() {
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
                    if (obj.getString("status").matches("true")) {
                        JSONArray homelist = obj.getJSONArray("data");
                        for (int i = 0; i < homelist.length(); i++) {
                            NowModel nowModel = new NowModel();
                            JSONObject listObj = homelist.getJSONObject(i);
                            nowModel.setSalon_id(listObj.getString("salon_id"));
                            nowModel.setSalon_unique_id(listObj.getString("salon_unique_id"));
                            nowModel.setSalon_name(listObj.getString("salon_name"));
                            nowModel.setImage(listObj.getString("image"));
                            nowModel.setDiscount(listObj.getString("discount"));
                            nowModel.setRating(listObj.getString("rating"));
                            nowModel.setAddress(listObj.getString("address"));
                            nowModel.setLatitude(listObj.getString("latitude"));
                            nowModel.setLongitude(listObj.getString("longitude"));
                            nowModel.setAvailability(listObj.getString("availability"));
                            nowModel.setDistance(listObj.getString("distance"));
                            nowModel.setFeatured_flag(listObj.getString("featured_flag"));
                            NowListDB.add(nowModel);
                        }
                        ry_now_list_items.setAdapter(new NowSalonAdapter(NowListDB,getApplicationContext()));
                        customLoader.stopLoadingDailog();
                    }else{

                    }
                }catch (Exception ex){
                    Log.d("error-", ex.getLocalizedMessage());
                    startActivity(new Intent(getApplicationContext(), AlertErrorActivity.class));
                    finish();
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
                Log.d("now_param",param.toString());
                return param;
            }
        };
        requestQueue.add(nowListRequest);
    }

    private void setProfileView() {
        startActivity(new Intent(NowListActivity.this,ProfileActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(NowListActivity.this,UserHomeActivity.class));
        finish();
    }
}
