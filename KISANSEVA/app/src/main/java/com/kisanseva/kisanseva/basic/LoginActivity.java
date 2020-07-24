package com.kisanseva.kisanseva.basic;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.kisanseva.kisanseva.firedb.DatabaseUtils;
import com.kisanseva.kisanseva.utils.Util;

import java.util.Locale;


public class LoginActivity extends Activity implements View.OnClickListener,OnLocaleChangedListener{
    private static final String TAG = LoginActivity.class.getSimpleName();
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Button login;
    private TextView register;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseInstance = DatabaseUtils.getDBinstance();
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("farmers");
        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(this);
        register=(TextView)findViewById(R.id.register);
        register.setOnClickListener(this);
        prefs = getApplicationContext().getSharedPreferences("kisanseva", 0); // 0 - for private mode
        if(prefs.contains("login")){

            Intent myIntent = new Intent(this, MainActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(myIntent);
            finish();
            String language = prefs.getString("language", "");
            setLanguage(language);
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.register:
                Intent myIntent = new Intent(this, RegistrationActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(myIntent);
                finish();
            case R.id.login:
                Log.d(TAG,"Login Clicked");
                final EditText username=(EditText)findViewById(R.id.phone);
                Log.d(TAG,username.getText().toString());
                DatabaseReference userNameRef = mFirebaseDatabase.child(username.getText().toString());
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) {
                            //create new user
                            Log.d(TAG,"User does not exists");
                        }
                        else {
                            Log.d(TAG,"User Exists");
                            EditText pin=(EditText)findViewById(R.id.pin);
                            Farmer fm=dataSnapshot.getValue(Farmer.class);
                            System.out.println(fm);
                            if(fm!=null) {
                                String actualpin=fm.getPin()+"";
                                if (!actualpin.equals(pin.getText().toString())) {
                                    Log.d(TAG, "Incorrect PIN");
                                    pin.setError("Incorrect PIN");

                                } else {
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("login", true);
                                    editor.putString(Util.UserId,username.getText().toString());
                                    editor.commit();
                                    if (prefs.contains("login")) {

                                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(myIntent);
                                        finish();
                                    }
                                    System.out.println(fm);
                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
                    }
                };
                userNameRef.addListenerForSingleValueEvent(eventListener);
                break;
        }
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

