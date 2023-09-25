package com.hennamehndi.mehndipatterns.bridalmehndi.designs;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefrenceValue {
    Context context;

    public SharedPrefrenceValue(Context context) {
        this.context = context;
    }

    public static String IN_APP_PURCHASE_KEY = "is_in_app_purchased";
    public static final String PREF_FILE = "MyPref";
    public static SharedPreferences sharedPreferences;

    public void savePurchaseValueToPref(boolean value) {
        getPreferenceEditObject().putBoolean(IN_APP_PURCHASE_KEY, value).commit();
    }

    private boolean getPurchaseValueFromPref() {
        return getPreferenceObject().getBoolean(IN_APP_PURCHASE_KEY, false);
    }

    private SharedPreferences.Editor getPreferenceEditObject() {
        SharedPreferences pref =context.getSharedPreferences(PREF_FILE, 0);
        return pref.edit();
    }

    private SharedPreferences getPreferenceObject() {
        return context.getSharedPreferences(PREF_FILE, 0);
    }


    public boolean hasUserBoughtInApp() {
        // Retrieve the purchase status from preferences
        return getPurchaseValueFromPref();
    }
}
