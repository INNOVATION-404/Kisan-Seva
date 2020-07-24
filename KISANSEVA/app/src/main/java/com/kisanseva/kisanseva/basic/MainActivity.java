package com.kisanseva.kisanseva.basic;


import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.kisanseva.kisanseva.R;
import com.kisanseva.kisanseva.adapters.HomeGridAdapter;
import com.kisanseva.kisanseva.entities.GeoLocation;
import com.kisanseva.kisanseva.reminder.ReminderActivity;
import com.kisanseva.kisanseva.ui.ChatBot;
import com.kisanseva.kisanseva.ui.ExpenseActivity;
import com.kisanseva.kisanseva.ui.GovernmentPolicies;
import com.kisanseva.kisanseva.ui.HelpActivity;
import com.kisanseva.kisanseva.ui.IncomeActivity;
import com.kisanseva.kisanseva.ui.LandsActivity;
import com.kisanseva.kisanseva.ui.LoanActivity;
import com.kisanseva.kisanseva.ui.MapsActivity;
import com.kisanseva.kisanseva.ui.MarketSellingPrices;
import com.kisanseva.kisanseva.ui.MixedCropping;
import com.kisanseva.kisanseva.ui.Notifications;
import com.kisanseva.kisanseva.ui.Profile;
import com.kisanseva.kisanseva.ui.SettingsActivity;
import com.kisanseva.kisanseva.ui.Statistics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends Activity implements LocationListener, OnLocaleChangedListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String JOB_TAG = "MyJobService";

    GridView androidGridView;
    WebView wimage;
    TextView wdesc, wtemp, wcity, wind;
    String[] gridViewString = {
            "Daily Expenses", "Record Income", "Loan Tracker", "Lands", "Advices", "Retailing", "Notifications", "Profile", "Government Policies",
            "Mixed Cropping", "Assistance", "Reminder", "Settings", "Help","Statistics","Market Selling",
    };
    String[] tips={"Procure certified seed of High Yielding Varieties/Hybrids recommended for the area from authorized agencies/dealers.",
            "Treat the seed with recommended fungicides for minimizing the disease incidence.",
            "Sow the pre-germinated seed of paddy in nursery bed as per guidelines prescribed by the State Agricultural University. ",
            "Use recommended doses of farm yard manure, macro and micro nutrients in nursery beds at the time of sowing.",
            "Nursery raising for planting systems like SRI, mechanical and general practice should be according to scientific recommendation.",
            "Get the soil tested for nutrient status and recommendations. ",
            "Follow the general recommendations for major and micro nutrients prescribed by the State Agricultural University.",
            "Apply lime/liming material to acid soils @ 2-4 quintals/ha or as per recommendation of the State Agricultural University at the time of field preparation.",
            "Mini kits of rice varieties/hybrids are being distributed under NFSM. Interested farmers may contact officers of the State Department of Agriculture."};
    int[] gridViewImageId = {
            R.drawable.icons8budget48, R.drawable.icons8requestmoney48,
            R.drawable.icons8cashinhand48, R.drawable.icons8fertileland48, R.drawable.icons8communication48,
            R.drawable.icons8shop48, R.drawable.icons8notification48,
            R.drawable.icons8usermale48, R.drawable.icons8museum48, R.drawable.icons8corn48,
            R.drawable.icons8consultation48, R.drawable.icons8alarmclock48, R.drawable.icons8services48,
            R.drawable.icons8questionmark48,R.drawable.icons8combochart48,R.drawable.icons8carrot48,
    };
    Class[] classes = {ExpenseActivity.class, IncomeActivity.class, LoanActivity.class, LandsActivity.class, LandsActivity.class, MapsActivity.class,
            Notifications.class, Profile.class, GovernmentPolicies.class, MixedCropping.class, ChatBot.class, ReminderActivity.class, com.kisanseva.kisanseva.ui.Settings.class, HelpActivity.class,Statistics.class,MarketSellingPrices.class};
    private GoogleMap mMap;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);
    protected LocationManager locationManager;
    private static final int REQUEST_PERMISSION_CODE = 12345;
    String provider;
    private static final String[] REQUIRED_PERMISSION_LIST = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };
    private List<String> missingPermission = new ArrayList<>();

    public String URL = "https://weatherbit-v1-mashape.p.rapidapi.com/current";
    public String WEATHER_IMAGE_URL = "https://www.weatherbit.io/static/img/icons/";
    public static final String WEATHERBIT_HEADER_NAME = "X-RapidAPI-Key";
    public static final String WEATHERBIT_HEADER_VALUE = "c98f2e8b67msh456ca760b3166eap1f4830jsn28b6b7955a4f";
    TextView tipoftheday;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridViewString=new String[16];

        gridViewString[0]=getResources().getString(R.string.daily_expenses);
        gridViewString[1]=getResources().getString(R.string.record_income);
        gridViewString[2]=getResources().getString(R.string.loan_tracker);
        gridViewString[3]=getResources().getString(R.string.lands);
        gridViewString[4]=getResources().getString(R.string.advices);
        gridViewString[5]=getResources().getString(R.string.retailing);
        gridViewString[6]=getResources().getString(R.string.notifications);
        gridViewString[7]=getResources().getString(R.string.profile);
        gridViewString[8]=getResources().getString(R.string.government_policies);
        gridViewString[9]=getResources().getString(R.string.mixed_cropping);
        gridViewString[10]=getResources().getString(R.string.assistance);
        gridViewString[11]=getResources().getString(R.string.reminder);
        gridViewString[12]=getResources().getString(R.string.settings);
        gridViewString[13]=getResources().getString(R.string.help);
        gridViewString[14]=getResources().getString(R.string.statistics_graphs);
        gridViewString[15]=getResources().getString(R.string.market_selling);

        tips=new String[9];
        tips[0]=getResources().getString(R.string.tip_1);
        tips[1]=getResources().getString(R.string.tip_2);
        tips[2]=getResources().getString(R.string.tip_3);
        tips[3]=getResources().getString(R.string.tip_4);
        tips[4]=getResources().getString(R.string.tip_5);
        tips[5]=getResources().getString(R.string.tip_6);
        tips[6]=getResources().getString(R.string.tip_7);
        tips[7]=getResources().getString(R.string.tip_8);
        tips[8]=getResources().getString(R.string.tip_9);
        Log.d(TAG, "OnCreate : started the process");
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        checkAndRequestPermissions();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException ex) {
            Log.d(TAG, "Exception " + ex.getMessage());
        }


        GeoLocation g = new GeoLocation(0.00, 0.00);
        Log.d(TAG, "OnClick : what happens on click");
        try {
            Criteria criteria = new Criteria();
            criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(true);
            criteria.setSpeedRequired(true);
            //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                Log.d(TAG, "OnClick : latitude present and longitude present");
                g.setLatitude(location.getLatitude());
                g.setLongitude(location.getLongitude());
                Log.d(TAG, g.toString());
            } else {
                Log.d(TAG, g.toString());
                Log.d(TAG, "OnClick : whatever");
            }
        } catch (SecurityException ex) {
            Log.d(TAG, "OnClick : now that's an exception");
            Log.d(TAG, ex.getMessage());
        }
        //Log.d(TAG,"OnClick : latitude present and longitude present");
        ActionBar bar = getActionBar();
        bar.setTitle("Kisan Seva");
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        prefs = getApplicationContext().getSharedPreferences("kisanseva", 0); // 0 - for private mode
        if (!prefs.contains("login")) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(myIntent);
            finish();
        }
        HomeGridAdapter adapterViewAndroid = new HomeGridAdapter(MainActivity.this, gridViewString, gridViewImageId);
        androidGridView = (GridView) findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                Intent myIntent = new Intent(MainActivity.this, classes[i]);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(myIntent);
                // Toast.makeText(MainActivity.this, "HomeGrid Item: " + gridViewString[+i], Toast.LENGTH_LONG).show();
            }
        });
        String latitude = String.valueOf(g.getLatitude());
        String longitude = String.valueOf(g.getLongitude());

        try {
            String urltobecalled = URL.concat("?lat=").concat(latitude).concat("&lon=").concat(longitude);
            run(urltobecalled);
        } catch (IOException e) {
            e.printStackTrace();
        }
        wimage = (WebView) findViewById(R.id.weatherimage);
        wdesc = (TextView) findViewById(R.id.wdesc);
        wtemp = (TextView) findViewById(R.id.wtemp);
        wcity = (TextView) findViewById(R.id.city);
        wind=(TextView)findViewById(R.id.wind);
        Random random = new Random();
        int upperBound=tips.length;
        int lowerBound=0;
        int randomNumber = random.nextInt(upperBound - lowerBound) + lowerBound;
        tipoftheday=(TextView)findViewById(R.id.tipoftheday);
        tipoftheday.setText(""+tips[randomNumber]);
        String language = prefs.getString("language", "");
        setLanguage(language);
        getActionBar().setTitle(getResources().getString(R.string.app_name));

    }

    private void checkAndRequestPermissions() {
        // Check for permissions
        for (String eachPermission : REQUIRED_PERMISSION_LIST) {
            if (ContextCompat.checkSelfPermission(this, eachPermission) != PackageManager.PERMISSION_GRANTED) {
                missingPermission.add(eachPermission);
            }
        }
        // Request for missing permissions
        if (missingPermission.isEmpty()) {

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            System.out.println("Need to grant the permissions!");
            ActivityCompat.requestPermissions(this,
                    missingPermission.toArray(new String[missingPermission.size()]),
                    REQUEST_PERMISSION_CODE);
        }

    }

    private void logOut() {
        prefs = getApplicationContext().getSharedPreferences("kisanseva", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(myIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem settings = menu.findItem(R.id.settings);
        settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Log.e(TAG, "Clicked on Menu Item");
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(myIntent);
                return false;
            }
        });
        MenuItem item = menu.findItem(R.id.logout);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                logOut();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    void run(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().header(WEATHERBIT_HEADER_NAME, WEATHERBIT_HEADER_VALUE)
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(myResponse);
                        try {
                            JSONObject weather = new JSONObject(myResponse);
                            JSONArray weatherdata = weather.getJSONArray("data");
                            JSONObject weatherObject = weatherdata.getJSONObject(0);
                            weather = weatherObject.getJSONObject("weather");
                            wimage.loadUrl(WEATHER_IMAGE_URL + weather.get("icon") + ".png");
                            wcity.setText(weatherObject.getString("city_name"));
                            wdesc.setText(weather.getString("description"));
                            wtemp.setText(weatherObject.getString("temp"));
                            wind.setText(weatherObject.getString("wind_cdir_full"));
                        } catch (Exception ex) {
                            System.out.println("run Exception " + ex.getMessage());
                        }
                    }
                });

            }
        });
    }
    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
    @Override
    public void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(newBase));
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Override
    public Resources getResources() {
        return localizationDelegate.getResources(super.getResources());
    }

    @Override
    public void onBeforeLocaleChanged() {
    }

    @Override
    public void onAfterLocaleChanged() {
    }


    public final void setLanguage(String language) {
        localizationDelegate.setLanguage(this, language);
    }

    public final void setLanguage(Locale locale) {
        localizationDelegate.setLanguage(this, locale);
    }

    public final void setDefaultLanguage(String language) {
        localizationDelegate.setDefaultLanguage(language);
    }

    public final void setDefaultLanguage(Locale locale) {
        localizationDelegate.setDefaultLanguage(locale);
    }



}
