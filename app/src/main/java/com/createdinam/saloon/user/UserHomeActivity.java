package com.createdinam.saloon.user;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.createdinam.saloon.MainActivity;
import com.createdinam.saloon.R;
import com.createdinam.saloon.global.CustomLoader;
import com.createdinam.saloon.global.InitFunction;
import com.createdinam.saloon.global.LinearLayoutManagerWithSmoothScroller;
import com.createdinam.saloon.global.calender.CustomCalender;
import com.createdinam.saloon.user.homelist.HomeListAdapter;
import com.createdinam.saloon.user.homelist.HotListAdapter;
import com.createdinam.saloon.user.laterlist.LaterBookingList;
import com.createdinam.saloon.user.nowlist.NowListActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;
import static com.createdinam.saloon.R.id.home_salon_list_view;
import static com.createdinam.saloon.global.Global.MY_PREFS_NAME;
import static com.createdinam.saloon.global.Global.Saloon_Hot_deal_List;
import static com.createdinam.saloon.global.Global.Saloon_list;
import static com.createdinam.saloon.global.Global.access_token;

public class UserHomeActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
    // default param
    private long lastPressedTime;
    private static final int PERIOD = 2000;
    private static String USER_ID;
    private static CustomLoader customLoader;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    RequestQueue requestQueue;
    // bottom navigation res
    RelativeLayout btn_lay_now, btn_lay_map, btn_lay_list, btn_lay_later,btn_home_list;
    // top navigation
    ImageView back_button;
    // location init
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    // ArrayList
    ArrayList<SalonModel> hot_list = new ArrayList<SalonModel>();
    ArrayList<HomeListModel> home_list = new ArrayList<HomeListModel>();
    RecyclerView hot_salon_list;
    RecyclerView home_list_rey;
    Runnable runnable;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();
        customLoader = new CustomLoader(UserHomeActivity.this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_top));
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        USER_ID = prefs.getString("user_id", "");
        // list init
        home_list_rey = findViewById(home_salon_list_view);
        // init
        hot_salon_list = findViewById(R.id.hot_salon_list);
        back_button = findViewById(R.id.back_button);
        btn_lay_now = findViewById(R.id.btn_lay_now);
        btn_lay_map = findViewById(R.id.btn_lay_map);
        btn_lay_list = findViewById(R.id.btn_lay_list);
        btn_lay_later = findViewById(R.id.btn_lay_later);
        btn_home_list = findViewById(R.id.btn_home_list);
        // bind click listener
        back_button.setOnClickListener(this);
        btn_lay_now.setOnClickListener(this);
        btn_lay_map.setOnClickListener(this);
        btn_lay_list.setOnClickListener(this);
        btn_lay_later.setOnClickListener(this);
        // init hot list
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        GridLayoutManager lManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        hot_salon_list.setLayoutManager(lManager);
        home_list_rey.setLayoutManager(layoutManager);
        if (InitFunction.getInstance(getApplicationContext()).isNetworkAvaliable(UserHomeActivity.this)) {
            customLoader.startLoadingDailog();
            getUserCurrentLocation();
            startHomeViewContent(USER_ID);
            startHotDealViewContent(USER_ID);
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500000, 0, this);
    }

    private void startHomeViewContent(final String userId) {
        StringRequest SaloonList = new StringRequest(Request.Method.POST, Saloon_list, new Response.Listener<String>() {
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
                            HomeListModel homeModel = new HomeListModel();
                            JSONObject listObj = homelist.getJSONObject(i);
                            homeModel.setSalon_id(listObj.getString("salon_id"));
                            homeModel.setSalon_unique_id(listObj.getString("salon_unique_id"));
                            homeModel.setSalon_name(listObj.getString("salon_name"));
                            homeModel.setAddress(listObj.getString("address"));
                            homeModel.setLatitude(listObj.getString("latitude"));
                            homeModel.setLongitude(listObj.getString("longitude"));
                            homeModel.setRating(listObj.getString("rating"));
                            homeModel.setImage(listObj.getString("image"));
                            homeModel.setDiscount(listObj.getString("discount"));
                            homeModel.setDistance(listObj.getString("distance"));
                            home_list.add(homeModel);
                        }
                        home_list_rey.setAdapter(new HomeListAdapter(home_list,UserHomeActivity.this));
                        //hot_salon_list.setAdapter(new HotListAdapter(hot_list,UserHomeActivity.this));
                        customLoader.stopLoadingDailog();
                    }
                    //Log.d("sal_lst", "" + response);
                } catch (Exception ex) {
                    Log.d("error", "" + ex.getMessage());
                    customLoader.stopLoadingDailog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("saloon_error", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("access_token", "" + access_token);
                param.put("lat", "28.467539");
                param.put("long", "77.082108");
                return param;
            }
        };
        requestQueue.add(SaloonList);
    }

    private void startHotDealViewContent(final String userId) {
        final StringRequest SaloonHotList = new StringRequest(Request.Method.POST, Saloon_Hot_deal_List, new Response.Listener<String>() {
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
                        JSONArray hotlist = obj.getJSONArray("data");
                        for (int i = 0; i < hotlist.length(); i++) {
                            SalonModel salonModel = new SalonModel();
                            JSONObject listObj = hotlist.getJSONObject(i);
                            salonModel.setSalonId(listObj.getString("salon_id"));
                            salonModel.setSalonUniqueId(listObj.getString("salon_unique_id"));
                            salonModel.setSalonName(listObj.getString("salon_name"));
                            salonModel.setImage(listObj.getString("image"));
                            salonModel.setDiscount(listObj.getString("discount"));
                            hot_list.add(salonModel);
                        }
                        hot_salon_list.setAdapter(new HotListAdapter(hot_list,UserHomeActivity.this));
                        autoScroll();
                        customLoader.stopLoadingDailog();
                    }
                } catch (Exception ex) {
                    Log.d("error", "" + ex.getMessage());
                    customLoader.stopLoadingDailog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("sal_hot_error", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("access_token", "" + access_token);
                param.put("lat", "28.467539");
                param.put("long", "77.082108");
                return param;
            }
        };
        requestQueue.add(SaloonHotList);
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
        startActivity(new Intent(UserHomeActivity.this, LaterBookingList.class));
        finish();
    }

    private void setMapView() {
        Toast.makeText(this, "Map", Toast.LENGTH_SHORT).show();
    }

    private void setListView() {
        Toast.makeText(this, "List", Toast.LENGTH_SHORT).show();
    }

    private void setNowView() {
        startActivity(new Intent(UserHomeActivity.this, NowListActivity.class));
        finish();
    }

    private void setProfileView() {
        startActivity(new Intent(UserHomeActivity.this, ProfileActivity.class));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserHomeActivity.this);
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
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("location", "lat -" + location.getLatitude() + "  lon -" + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("status_change", "" + provider + " |-| " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("loc_enable", "" + provider.trim());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("loc_disable", "" + provider);
    }

    public void autoScroll() {
        final int speedScroll = 4000;
        handler = new Handler();
        runnable = new Runnable() {
            int count = -1;
            @Override
            public void run() {
                if (count < hot_salon_list.getAdapter().getItemCount()) {
                    hot_salon_list.smoothScrollToPosition(++count);
                    handler.postDelayed(this, speedScroll);
                }
                if (count == hot_salon_list.getAdapter().getItemCount()) {
                    //hot_salon_list.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(UserHomeActivity.this,0,false));
                    hot_salon_list.smoothScrollToPosition(++count);
                    handler.postDelayed(this, speedScroll);
                }
            }
        };
        handler.post(runnable);
    }
}
