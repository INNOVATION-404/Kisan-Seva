package com.kisanseva.kisanseva.basic;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kisanseva.kisanseva.R;
import com.kisanseva.kisanseva.entities.Farmer;
import com.kisanseva.kisanseva.entities.GeoLocation;
import com.kisanseva.kisanseva.firedb.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RegistrationActivity extends Activity implements View.OnClickListener, LocationListener, OnLocaleChangedListener {
    private static final String TAG = RegistrationActivity.class.getSimpleName();

    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private TextView loginlink, userfullname, email, pin, confirmpin, mobile;
    private String userId;
    private Button button;
    private boolean userexists = false;
    private SharedPreferences prefs;
    protected LocationManager locationManager;
    protected Context context;
    String provider;
    Dialog newDialog, myDialog;
    TextView msg, review;
    ImageView text_close, text_closing;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        checkAndRequestPermissions();
        setContentView(R.layout.activity_registration);
        mFirebaseInstance = DatabaseUtils.getDBinstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("farmers");
        loginlink = (TextView) findViewById(R.id.login);
        loginlink.setOnClickListener(this);
        userfullname = (TextView) findViewById(R.id.userfullname);
        email = (TextView) findViewById(R.id.email);
        pin = (TextView) findViewById(R.id.pin);
        confirmpin = (TextView) findViewById(R.id.confirmpin);
        mobile = (TextView) findViewById(R.id.phone);
        button = (Button) findViewById(R.id.register);
        button.setOnClickListener(this);
        prefs = getApplicationContext().getSharedPreferences("kisanseva", 0); // 0 - for private mode
        if (prefs.contains("login")) {

            Intent myIntent = new Intent(this, MainActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(myIntent);
            finish();
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException ex) {
            System.out.println("Exception " + ex.getMessage());
        }
        String language = prefs.getString("language", "");
        setLanguage(language);

    }


    /**
     * Checks if there is any missing permissions, and
     * requests runtime permission if needed.
     */
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                Intent myIntent = new Intent(this, LoginActivity.class);
                startActivity(myIntent);
                break;
            case R.id.register:
                String uname = userfullname.getText().toString();
                String emailtxt = email.getText().toString();
                String mobiletxt = mobile.getText().toString();
                String pintxt = pin.getText().toString();
                String confirmpintxt = confirmpin.getText().toString();
                if(uname.length()<3){
                    userfullname.setError(getResources().getString(R.string.user_fullname_error));
                    break;
                }

                if(!isEmailValid(emailtxt)) {
                    email.setError(getResources().getString(R.string.email_error));
                    break;
                }
                if(mobiletxt.length()!=10) {
                    mobile.setError(getResources().getString(R.string.mobile_number_error));
                    break;
                }
                if(!isPasswordValid(pintxt)){
                    pin.setError(getResources().getString(R.string._4_digit_pin_error));
                    break;
                }
                if(!isPasswordValid(confirmpintxt)){
                    confirmpin.setError(getResources().getString(R.string._4_digit_pin_error));
                    break;
                }
                if(!(pintxt.equals(confirmpintxt))){
                    pin.setError(getResources().getString(R.string.match_pin_error));
                    break;
                }
                Farmer farmer = new Farmer();
                farmer.setEmail(emailtxt);
                farmer.setName(uname);
                farmer.setPhone(mobiletxt);
                farmer.setPin(pintxt);
                GeoLocation g = new GeoLocation(0.00, 0.00);
                try {
                    Criteria criteria = new Criteria();
                    criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setAltitudeRequired(true);
                    criteria.setBearingRequired(true);
                    criteria.setSpeedRequired(true);
                    provider = locationManager.getBestProvider(criteria, true);
                    Location location = locationManager.getLastKnownLocation(provider);
                    if (location != null) {
                        g.setLatitude(location.getLatitude());
                        g.setLongitude(location.getLongitude());
                        Log.d(TAG, g.toString());
                    } else {
                        Log.d(TAG, g.toString());
                    }
                } catch (SecurityException ex) {
                    Log.d(TAG, ex.getMessage());
                }
                farmer.setLocation(g);
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userNameRef = rootRef.child("farmers").child(mobiletxt);

                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            //create new user
                            userexists = false;
                            Log.d(TAG, "User does not exists");
                        } else {
                            Log.d(TAG, "User Exists");
                            userexists = true;
                            Farmer fm = dataSnapshot.getValue(Farmer.class);
                            System.out.println(fm);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
                    }
                };
                userNameRef.addListenerForSingleValueEvent(eventListener);
                userId = mobiletxt;
                // mFirebaseDatabase.orderByChild("mobile").equalTo("phone");
                if (TextUtils.isEmpty(userId)) {
                    userId = mFirebaseDatabase.push().getKey();
                }
                if (userexists) {
                    mobile.setError("Mobile number already registered!");
                    break;
                }
                mFirebaseDatabase.child(userId).setValue(farmer);
                myDialog = new Dialog(this);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopUp();
                    }
                });
                break;

        }
    }

    public void showPopUp() {
        myDialog.setContentView(R.layout.custompop);
        text_close = (ImageView) myDialog.findViewById(R.id.text_close);
        Button btn2 = (Button) myDialog.findViewById(R.id.loginbtn);
        msg = (TextView) myDialog.findViewById(R.id.login);
        text_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(home);
                finish();
            }
        });

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() == 4;
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
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
