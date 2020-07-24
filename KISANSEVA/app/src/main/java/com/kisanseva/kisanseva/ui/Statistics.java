package com.kisanseva.kisanseva.ui;

import android.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.*;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.kisanseva.kisanseva.R;


public class Statistics extends Activity implements OnLocaleChangedListener {
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    private BarChart barchart1;
    private BarChart barchart2;
    private BarChart barchart3;
    private Button b1;
    private Button b2;
    private Button b3;
    private LinearLayout l1;
    private LinearLayout l2;
    private LinearLayout l3;
    private SharedPreferences prefs;


    @Override protected void onCreate (Bundle savedInstanceState){
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        b1=(Button) findViewById(R.id.buttoday);
        b2=(Button) findViewById(R.id.butweek);
        b3=(Button) findViewById(R.id.butmon);
        barchart1=findViewById(R.id.barIdtoday);
        barchart2=findViewById(R.id.barIdweek);
        barchart3=findViewById(R.id.barIdmonth);
        l1=findViewById(R.id.laytod);
        l2=findViewById(R.id.layweek);
        l3=findViewById(R.id.laymonth);
        BarDataSet bds1=new BarDataSet(datavalues1(),"DataSet1");
        BarData bd1=new BarData();
        bd1.addDataSet(bds1);
        barchart1.setData(bd1);
        barchart1.invalidate();
        l1.setVisibility(View.GONE);
        // l1.setVisibility(View.VISIBLE);
        //    l2.setVisibility(View.GONE);
        //  l3.setVisibility(View.GONE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarDataSet bds1=new BarDataSet(datavalues1(),"DataSet1");
                BarData bd1=new BarData();
                bd1.addDataSet(bds1);
                barchart1.setData(bd1);
                barchart1.invalidate();
                l1.setVisibility(View.VISIBLE);
                l2.setVisibility(View.GONE);
                l3.setVisibility(View.GONE);
                v.setBackgroundColor(000000);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarDataSet bds2=new BarDataSet(datavalues2(),"DataSet2");
                BarData bd2=new BarData();
                bd2.addDataSet(bds2);
                barchart2.setData(bd2);
                barchart2.invalidate();
                l2.setVisibility(View.VISIBLE);
                l1.setVisibility(View.GONE);
                l3.setVisibility(View.GONE);
                v.setBackgroundColor(010000);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BarDataSet bds3=new BarDataSet(datavalues3(),"DataSet3");
                BarData bd3=new BarData();
                bd3.addDataSet(bds3);
                barchart3.setData(bd3);
                barchart3.invalidate();
                l3.setVisibility(View.VISIBLE);
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.GONE);
                v.setBackgroundColor(000100);
            }
        });
        prefs = getApplicationContext().getSharedPreferences("kisanseva", 0); // 0 - for private mode
        String language = prefs.getString("language", "");
        setLanguage(language);
        getActionBar().setTitle(getResources().getString(R.string.kisan_seva_statistics));

    }
    private ArrayList<BarEntry> datavalues1(){
        ArrayList<BarEntry>dataVals1=new ArrayList<BarEntry>();
        dataVals1.add(new BarEntry(4,1));
        dataVals1.add(new BarEntry(3,2));
        dataVals1.add(new BarEntry(2,3));
        dataVals1.add(new BarEntry(1,4));
        return dataVals1;
    }


    private ArrayList<BarEntry> datavalues2(){
        ArrayList<BarEntry>dataVals2=new ArrayList<BarEntry>();
        dataVals2.add(new BarEntry(6,2));
        dataVals2.add(new BarEntry(3,3));
        dataVals2.add(new BarEntry(2,5));
        dataVals2.add(new BarEntry(1,7));
        return dataVals2;
    }


    private ArrayList<BarEntry> datavalues3(){
        ArrayList<BarEntry>dataVals=new ArrayList<BarEntry>();
        dataVals.add(new BarEntry(5,1));
        dataVals.add(new BarEntry(4,2));
        dataVals.add(new BarEntry(3,3));
        dataVals.add(new BarEntry(2,5));
        return dataVals;
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
