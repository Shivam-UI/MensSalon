package com.createdinam.saloon.user.itemdetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.createdinam.saloon.user.itemdetails.model.CategoryAdapter;
import com.createdinam.saloon.user.itemdetails.model.CategoryModel;
import com.createdinam.saloon.user.itemdetails.model.DescriptionModel;
import com.createdinam.saloon.user.itemdetails.model.GridViewAdapter;
import com.createdinam.saloon.user.laterlist.LaterBookingList;
import com.createdinam.saloon.user.laterlist.model.LaterModel;
import com.createdinam.saloon.user.laterlist.model.LaterSalonAdapter;
import com.createdinam.saloon.user.nowlist.NowListActivity;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.createdinam.saloon.global.Global.MY_PREFS_NAME;

public class SaloonItensDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private static String USER_ID,SALON_ID = "",SAL_LAT="",SAL_LONG="",VIDEO_URI="",FLAG="",TIME="";
    private static CustomLoader customLoader;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    RequestQueue requestQueue;
    // bottom navigation res
    RelativeLayout btn_lay_now, btn_lay_map, btn_lay_list, btn_lay_later, btn_home_list;
    // top navigation
    ImageView back_button;
    TextView logo_imageView, txt_later, txt_now;
    // recycler view items
    RecyclerView category_list_holder;
    GridView category_items_grid;
    // SimpleDateFormat
    SimpleDateFormat simpleDateFormat;
    // save data to ArrayList
    ArrayList<CategoryModel> mCategoryData = new ArrayList<CategoryModel>();
    // enable view
    TextView salon_des_name,salon_des_address,salon_des_time,salon_des_location,salon_description;
    SimpleExoPlayerView desc_vide_details;
    PlayerView playerView;
    SimpleExoPlayer exoPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloon_itens_details);
        Intent intent = getIntent();
        SALON_ID = intent.getStringExtra("salon_id");
        SAL_LAT = intent.getStringExtra("lat");
        SAL_LONG = intent.getStringExtra("long");
        FLAG = intent.getStringExtra("flag");
        TIME = intent.getStringExtra("time");
        Log.d("we_found","-> "+SALON_ID+" - "+SAL_LAT+" - "+SAL_LONG);
        prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();
        customLoader = new CustomLoader(SaloonItensDetailsActivity.this);
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
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        // init items
        salon_des_name = findViewById(R.id.salon_des_name);
        salon_des_address = findViewById(R.id.salon_des_address);
        salon_des_time = findViewById(R.id.salon_des_time);
        salon_des_location = findViewById(R.id.salon_des_location);
        salon_description = findViewById(R.id.salon_description);
        playerView = findViewById(R.id.desc_vide_details);
        desc_vide_details = findViewById(R.id.desc_vide_details);
        category_list_holder = findViewById(R.id.category_list_holder);
        category_items_grid = findViewById(R.id.category_items_grid);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        category_list_holder.setLayoutManager(mLayoutManager);
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
        btn_lay_now.setVisibility(View.INVISIBLE);
        logo_imageView.setText("Ah.. Thats all we got..");
        txt_now.setText("Book Now");
        txt_later.setText("secure pay");

        if (InitFunction.getInstance(getApplicationContext()).isNetworkAvaliable(SaloonItensDetailsActivity.this)) {
            customLoader.startLoadingDailog();
            // start method for list
            setEnableDetailsView(SALON_ID,SAL_LAT,SAL_LONG);
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void setEnableDetailsView(final String id, final String lat, final String log) {
        StringRequest nowListRequest = new StringRequest(Request.Method.POST, Global.Saloon_items_Details, new Response.Listener<String>() {
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
                        JSONObject objData = obj.getJSONObject("data");
                        JSONObject detailslist = objData.getJSONObject("description");
                        Log.d("size", "" + detailslist.length());
                        // increase time by 1hr
                        Date date = simpleDateFormat.parse(TIME);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.HOUR, 1);
                        // get time in 12hrs
                        String timenow = simpleDateFormat.format(calendar.getTime());
                        // set Data to view
                        salon_des_name.setText(detailslist.getString("salon_name"));
                        salon_des_address.setText(detailslist.getString("address"));
                        //salon_des_time.setText(detailslist.getString("availability"));
                        salon_des_time.setText(timenow);
                        salon_des_location.setText(detailslist.getString("distance"));
                        salon_description.setText(detailslist.getString("salon_description"));
                        // clear time
                        TIME = "";
                        // set Player
                        TrackSelector trackSelector = new DefaultTrackSelector();
                        exoPlayer = ExoPlayerFactory.newSimpleInstance(SaloonItensDetailsActivity.this, trackSelector);
                        playerView.setPlayer(exoPlayer);
                        exoPlayer.setPlayWhenReady(true);
                        exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                        if (exoPlayer.isLoading()){
                            Log.d("","Loading..");
                        }else{
                            Log.d("","Playing..");
                        }
                        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(SaloonItensDetailsActivity.this, Util.getUserAgent(SaloonItensDetailsActivity.this, "VideoPlayer"));
                        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(detailslist.getString("image")));
                        exoPlayer.prepare(videoSource);
                        // set category
                        JSONArray categoryList = objData.getJSONArray("categories");
                        for(int i = 0; i < categoryList.length();i++){
                            CategoryModel categoryModel = new CategoryModel();
                            JSONObject listObj = categoryList.getJSONObject(i);
                            // create category
                            categoryModel.setId(listObj.getString("id"));
                            categoryModel.setName(listObj.getString("name"));
                            categoryModel.setCategory_image(listObj.getString("category_image"));
                            categoryModel.setSalon_uniqueid(listObj.getString("salon_uniqueid"));
                            mCategoryData.add(categoryModel);
                        }
                        category_list_holder.setAdapter(new CategoryAdapter(mCategoryData,SaloonItensDetailsActivity.this));
                        // set grid view
                        category_items_grid.setAdapter(new GridViewAdapter(mCategoryData,SaloonItensDetailsActivity.this));
                        customLoader.stopLoadingDailog();
                    } else {
                        Log.d("error-1", "No Data Found");
                        customLoader.stopLoadingDailog();
                    }
                } catch (Exception ex) {
                    Log.d("error-2", ex.getMessage());
                    customLoader.stopLoadingDailog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error-3", "onResponse: " + error.getMessage());
                customLoader.stopLoadingDailog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("access_token", Global.access_token);
                param.put("lat",lat);
                param.put("long", log);
                param.put("salon_id", id);
                return param;
            }
        };
        requestQueue.add(nowListRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                setProfileView();
                break;
            case R.id.btn_lay_now:
                //setNowView();
                break;
            case R.id.btn_lay_map:
                setMapView();
                break;
            case R.id.btn_lay_list:
                setListView();
                break;
            case R.id.btn_lay_later:
                setSecurePay();
                break;
        }
    }

    private void setSecurePay() {
        Toast.makeText(this, "Payment Not Implemented Yet!", Toast.LENGTH_SHORT).show();
    }

    private void setMapView() {
        CustomCalender customCalender = new CustomCalender(this);
        customCalender.showCalendar();
    }

    private void setProfileView() {
        startActivity(new Intent(SaloonItensDetailsActivity.this, ProfileActivity.class));
        finish();
    }

    private void setListView() {
        startActivity(new Intent(SaloonItensDetailsActivity.this, UserHomeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (FLAG.trim().toLowerCase().matches("now")){
            startActivity(new Intent(SaloonItensDetailsActivity.this, NowListActivity.class));
            FLAG="";
            finish();
        }else if(FLAG.trim().toLowerCase().matches("later")){
            startActivity(new Intent(SaloonItensDetailsActivity.this,LaterBookingList.class));
            FLAG="";
            finish();
        }
    }
}
