package com.zupp.component.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.zupp.component.AppHelper;
import com.zupp.component.httpclient.GsonHelper;
import com.zupp.component.models.AppUser;
import com.zupp.component.models.CustomerData;

/**
 * Created by riteshdubey on 2/17/17.
 */

public class AppUserSession {

    private static String PREFS_FILE_NAME = "app_data_prefs";

    public static final String CUSTOMER_DETAIL = "customer_data";
    public static final String USER_DETAIL = "user_data";
    public static final String FIRST_LAUNCH = "first_launch";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String DEVICE_TOKEN = "device_token";

    public static final String LOGIN_STATUS = "isLoggedIn";

    Context mContext;

    private static AppUserSession mInstance = null;

    public AppUserSession(Context iContext) {
        this.mContext = iContext;
    }

    public static synchronized AppUserSession getInstance() {
        if (mInstance == null) {
            mInstance = new AppUserSession(AppHelper.getInstance().getContext());
        }
        return mInstance;
    }

    public void setLoginStatus(boolean isLoggedIn) {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = aMyPrefs.edit();
        prefsEditor.putBoolean(LOGIN_STATUS, isLoggedIn);
        prefsEditor.commit();
    }

    public boolean getLoginStatus() {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return aMyPrefs.getBoolean(LOGIN_STATUS, false);
    }

    public void setProfileDetail(AppUser iData) {
        String aUserData = GsonHelper.toJson(iData);
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = aMyPrefs.edit();
        prefsEditor.putString(USER_DETAIL, aUserData);
        prefsEditor.apply();
        if (!TextUtils.isEmpty(iData.access_token)) {
            saveString(ACCESS_TOKEN, iData.access_token);
            setLoginStatus(true);
        }
    }

    public AppUser getProfileData() {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return (AppUser) GsonHelper.getGson(aMyPrefs.getString(USER_DETAIL, ""), AppUser.class);
    }

    public void setCustomerDetail(CustomerData iData) {
        String aUserData = GsonHelper.toJson(iData);
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = aMyPrefs.edit();
        prefsEditor.putString(CUSTOMER_DETAIL, aUserData);
        prefsEditor.apply();
    }

    public CustomerData getCustomerDetail() {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return (CustomerData) GsonHelper.getGson(aMyPrefs.getString(CUSTOMER_DETAIL, ""), CustomerData.class);
    }





    public void setAccessToken(String iData) {
        if(!TextUtils.isEmpty(iData)) {
            saveString(ACCESS_TOKEN, iData);
        }
    }

    public String getAccessToken() {
        return getString(ACCESS_TOKEN);
    }

    public void setFirstLaunch(boolean iData) {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = aMyPrefs.edit();
        prefsEditor.putBoolean(FIRST_LAUNCH, iData);
        prefsEditor.commit();
    }

    public boolean getFirstLaunch() {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return aMyPrefs.getBoolean(FIRST_LAUNCH, true);
    }

    ///--------------------------------------------------------------------------------------

    public void saveString(String iKey, String iValue) {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = aMyPrefs.edit();
        prefsEditor.putString(iKey, iValue);
        prefsEditor.commit();
    }

    public void saveBoolean(String iKey, boolean iValue) {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = aMyPrefs.edit();
        prefsEditor.putBoolean(iKey, iValue);
        prefsEditor.commit();
    }

    public void saveInteger(String iKey, int iValue) {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = aMyPrefs.edit();
        prefsEditor.putInt(iKey, iValue);
        prefsEditor.commit();
    }

    public String getString(String iKey) {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return aMyPrefs.getString(iKey, "");
    }

    public boolean getBoolean(String iKey) {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return aMyPrefs.getBoolean(iKey, false);
    }

    public int getInteger(String iKey) {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return aMyPrefs.getInt(iKey, 0);
    }

    ///--------------------------------------------------------------------------------------

    public void clearSavedData() {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = aMyPrefs.edit();
        prefsEditor.clear().commit();
    }

    public void clearCustomerData() {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = aMyPrefs.edit();
        prefsEditor.remove(CUSTOMER_DETAIL);
        prefsEditor.commit();
    }
}
