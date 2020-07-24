package com.kisanseva.kisanseva.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kisanseva.kisanseva.R;
import com.kisanseva.kisanseva.adapters.CustomSpinnetAdapter;
import com.kisanseva.kisanseva.adapters.HomeGridAdapter;
import com.kisanseva.kisanseva.basic.MainActivity;
import com.kisanseva.kisanseva.entities.Land;
import com.kisanseva.kisanseva.firedb.DatabaseUtils;

import java.util.Locale;

public class MixedCropping extends Activity implements OnLocaleChangedListener {
    private static final String TAG = MixedCropping.class.getSimpleName();

    private SharedPreferences prefs;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    private TextView mTextMessage;
    private Dialog mSplashDialog;
    GridView androidGridView;

    String[] gridString = {
            "wheat", "durum_wheat", "barley", "oat", "rye_mustard", "yellow_mustard",
            "linseed", "kusum", "rabi_maize", "baby_corn", "gram", "peas", "lentil", "rabbi_beans", "barseem",
    };
    String[] Urls = {
            "Wheat.html", "DurumWheat.html", "Barley.html", "Oats.html", "Rye_and_mustard.html", "Yellow_mustard.html",
            "Linseed.html", "Kusum.html", "Rabi_Maize.html", "Baby_Corn.html", "Gram.html", "Peas.html", "Lentil.html", "Rabbi_Beans.html",
            "Barseem.html",
    };
    String[] Names = {
            "mcwheat", "mcdurum_wheat", "mcbarley", "mcoat", "mcrye_mustard", "mcyellow_mustard",
            "mclinseed", "mckusum", "mcrabi_maize", "mcbaby_corn", "mcgram", "mcpeas", "mclentil", "mcrabbi_beans", "mcbarseem",  };
    int[] gridViewImageId = {
            R.drawable.wheat, R.drawable.durum_wheat, R.drawable.barley, R.drawable.oats,
            R.drawable.blackmustard, R.drawable.yellow_mustard,
            R.drawable.linseed, R.drawable.kusum, R.drawable.maize,
            R.drawable.babycorn, R.drawable.gram, R.drawable.peas, R.drawable.lentils, R.drawable.rabbu_beans, R.drawable.barseem,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixed_cropping);

        gridString[0]=getResources().getString(R.string.wheat);
        gridString[1]=getResources().getString(R.string.durum_wheat);
        gridString[2]=getResources().getString(R.string.barley);
        gridString[3]=getResources().getString(R.string.oat);
        gridString[4]=getResources().getString(R.string.rye_mustard);
        gridString[5]=getResources().getString(R.string.yellow_mustard);
        gridString[6]=getResources().getString(R.string.linseed);
        gridString[7]=getResources().getString(R.string.kusum);
        gridString[8]=getResources().getString(R.string.rabi_maize);
        gridString[9]=getResources().getString(R.string.baby_corn);
        gridString[10]=getResources().getString(R.string.gram);
        gridString[11]=getResources().getString(R.string.peas);
        gridString[12]=getResources().getString(R.string.lentil);
        gridString[13]=getResources().getString(R.string.rabbi_beans);
        gridString[14]=getResources().getString(R.string.barseem);

        //mFirebaseInstance = DatabaseUtils.getDBinstance();
        //getDefaultsValuesFromFirebase();

        HomeGridAdapter adapterViewAndroid = new HomeGridAdapter(MixedCropping.this, gridString, gridViewImageId);
        androidGridView = (GridView) findViewById(R.id.grid_view_crops);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogBox(false, null, position);
            }
       /* mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);*/
        });
        prefs = getApplicationContext().getSharedPreferences("kisanseva", 0); // 0 - for private mode
        // ;
        setLanguage(prefs.getString("language", ""));
        getActionBar().setTitle(getResources().getString(R.string.kisan_seva_mixed_cropping));

    }


    private void showDialogBox(boolean b, Object o, int i) {
        Log.e(TAG, "show Dialog box");
        //Create a new Dialog with a custom style SplashScreen
        mSplashDialog = new Dialog(this, R.style.SplashScreen);
        // Apply no titlebar to the dialo   g
        mSplashDialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
        // Set the layout to the dialog created
        mSplashDialog.setContentView(R.layout.activity_mixed_cropping2);
        TextView titles=(TextView) mSplashDialog.findViewById(R.id.title_info);
        Names[0]=getResources().getString(R.string.mcwheat);
        Names[1]=getResources().getString(R.string.mcdurum_wheat);
        Names[2]=getResources().getString(R.string.mcbarley);
        Names[3]=getResources().getString(R.string.mcoat);
        Names[4]=getResources().getString(R.string.mcrye_mustard);
        Names[5]=getResources().getString(R.string.mcyellow_mustard);
        Names[6]=getResources().getString(R.string.mclinseed);
        Names[7]=getResources().getString(R.string.mckusum);
        Names[8]=getResources().getString(R.string.mcrabi_maize);
        Names[9]=getResources().getString(R.string.mcbaby_corn);
        Names[10]=getResources().getString(R.string.mcgram);
        Names[11]=getResources().getString(R.string.mcpeas);
        Names[12]=getResources().getString(R.string.mclentil);
        Names[13]=getResources().getString(R.string.mcrabbi_beans);
        Names[14]=getResources().getString(R.string.mcbarseem);

        titles.setText(Names[i]);
        WebView browser = (WebView) mSplashDialog.findViewById(R.id.viewing);

        browser.loadUrl("file:///android_asset/"+Urls[i]);
        // Set fullscreen flags for the dialog
        mSplashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Set Layout params for the dialog
        mSplashDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // Set cancelable as true
        mSplashDialog.setCancelable(true);
        // Show the dialog
        mSplashDialog.show();
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