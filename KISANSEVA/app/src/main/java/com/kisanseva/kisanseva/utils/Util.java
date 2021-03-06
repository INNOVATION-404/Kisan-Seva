package com.kisanseva.kisanseva.utils;

import android.preference.PreferenceManager;
import android.widget.EditText;

import com.kisanseva.kisanseva.KisanSevaApp;
import com.kisanseva.kisanseva.R;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Util {
    public static final String UserId="userKey";
    public static String formatDateToString(Date date, String pattern) {
        DateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static boolean isEmptyField(EditText et) {
        return (et.getText() == null || et.getText().toString().isEmpty());
    }

/*
    public static String getFormattedCurrency(float number) {
        String countryCode = PreferenceManager.getDefaultSharedPreferences(KisanSevaApp.getContext()).getString(KisanSevaApp.getContext().getString(R.string.pref_country_key), KisanSevaApp.getContext().getString(R.string.default_country));
        Locale locale = new Locale("EN", countryCode);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        String formattedNumber = numberFormat.format(number);
        String symbol = numberFormat.getCurrency().getSymbol(locale);
        return formattedNumber.replace(symbol, symbol+" ");
    }
*/
    public static String getCurrentDateFormat() {
        return PreferenceManager.getDefaultSharedPreferences(KisanSevaApp.getContext()).getString(KisanSevaApp.getContext().getString(R.string.date_format_key), KisanSevaApp.getContext().getString(R.string.default_date_format));
    }

}
