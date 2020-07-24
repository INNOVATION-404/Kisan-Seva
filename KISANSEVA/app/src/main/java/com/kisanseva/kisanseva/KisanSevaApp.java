package com.kisanseva.kisanseva;

import android.app.Application;
import android.content.Context;

/**
 * Created by Saikrishna on 19/02/2019.
 */

public class KisanSevaApp extends Application{
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
