package com.kisanseva.kisanseva.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;


import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kisanseva.kisanseva.R;
import com.kisanseva.kisanseva.entities.GeoLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener,OnLocaleChangedListener {
    private SharedPreferences prefs;

    private GoogleMap mMap;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);
    private static final String TAG = MapsActivity.class.getSimpleName();
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private GoogleApiClient mGoogleApiClient;
    String provider;
    GeoLocation g=new GeoLocation(0.00,0.00);;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;
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

    private static final int REQUEST_PERMISSION_CODE = 12345;
    private List<String> missingPermission = new ArrayList<>();
    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        prefs = getApplicationContext().getSharedPreferences("kisanseva", 0); // 0 - for private mode

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d(TAG,"OnCreate : started the process");
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        catch (SecurityException ex){
            System.out.println("Exception "+ex.getMessage());
        }
        ImageView i1=(ImageView)findViewById(R.id.img1);
        i1.setImageDrawable(getResources().getDrawable(R.drawable.img11));


        ImageView i2=(ImageView)findViewById(R.id.img2);
        i2.setImageDrawable(getResources().getDrawable(R.drawable.img23));

        ImageView i3=(ImageView)findViewById(R.id.img3);
        i3.setImageDrawable(getResources().getDrawable(R.drawable.img33));

        ImageView i4=(ImageView)findViewById(R.id.img4);
        i4.setImageDrawable(getResources().getDrawable(R.drawable.img44));

        ImageView i5=(ImageView)findViewById(R.id.img5);
        i5.setImageDrawable(getResources().getDrawable(R.drawable.img55));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        String language = prefs.getString("language", "");
        setLanguage(language);
        getActionBar().setTitle(getResources().getString(R.string.kisan_seva_maps));


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        GeoLocation g=new GeoLocation(0.00,0.00);
        Log.d(TAG,"OnClick : what happens on click");
        try{
            Criteria criteria = new Criteria();
            criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(true);
            criteria.setSpeedRequired(true);
            //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            if(location!=null) {
                g.setLatitude(location.getLatitude());
                g.setLongitude(location.getLongitude());
                Log.d(TAG, g.toString());
            }else {
                Log.d(TAG, g.toString());
            }
        }
        catch (SecurityException ex){
            Log.d(TAG,ex.getMessage());
        }
        Log.d(TAG,"OnClick : latitude present and longitude present");

        /*Intent myIntent = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(myIntent);*/

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng current_loc = new LatLng(g.getLatitude(),g.getLongitude());
        mMap.addMarker(new MarkerOptions().position(current_loc).title("Marker of Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current_loc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(g.getLatitude(),g.getLongitude()),16.0f));

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

    public final Locale getCurrentLanguage() {
        return localizationDelegate.getLanguage(this);
    }

    // Just override method locale change event
    @Override
    public void onBeforeLocaleChanged() {
    }

    @Override
    public void onAfterLocaleChanged() {
    }
}
