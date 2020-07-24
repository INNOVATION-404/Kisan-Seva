package com.kisanseva.kisanseva.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.kisanseva.kisanseva.R;
import com.kisanseva.kisanseva.basic.MainActivity;

import java.util.Locale;

public class SettingsActivity extends Activity implements View.OnClickListener,OnLocaleChangedListener{
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);
    Spinner language;
    Button saveSettings;
    int selectedIndex=0;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getActionBar().setTitle("Kisan Seva - Language Settings");
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#34af23")));
        language=(Spinner)findViewById(R.id.langselect);
        prefs = getSharedPreferences("kisanseva", 0);
        editor=prefs.edit();
        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedIndex=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        saveSettings=(Button)findViewById(R.id.save);
        saveSettings.setOnClickListener(this);
        //loadLocale();
        String language = prefs.getString("language", "");
        setLanguage(language);
        getActionBar().setTitle(getResources().getString(R.string.kisan_seva_settings));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:
                if(selectedIndex==0){
                    editor.putString("language","en");
                    editor.commit();
                    Toast.makeText(SettingsActivity.this,"Language Set to ENGLISH",Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(myIntent);
                    finish();
                }
                if(selectedIndex==1){
                    editor.putString("language","hi");
                    editor.commit();
                    Toast.makeText(SettingsActivity.this,"Language Set to HINDI",Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(myIntent);
                    finish();
                }
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
