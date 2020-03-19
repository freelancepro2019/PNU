package com.collage.pnuapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;

import java.util.Locale;

public class SharedPrefDueDate {
    // LogCat tag
    private static String TAG = SharedPrefDueDate.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "StudentAPPAtentdance";

    private static final String KEY_DUE_DATE = "KEY_DUE_DATE";
    private static final String PERSON_ID_CODE = "PERSON_ID";
    private static final String UserToken = "UserToken";
    private static final String UserType = "UserType";
    private static final String UserId= "UserId";
    private static final String lang= "lang";

    public SharedPrefDueDate(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setPersonId(int numSub) {

        editor.putInt(PERSON_ID_CODE, numSub);

        // commit changes
        editor.commit();

    }


    public void setKEY_DUE_DATE(String code) {
        editor.putString(KEY_DUE_DATE, code);

        // commit changes
        editor.commit();
    }


    public void setUserToken(String token) {
        editor.putString(UserToken, token);
        // commit changes
        editor.commit();
    }
    public String getUserToken() {
        return pref.getString(UserToken, "");
    }

    public void setUserId(String id) {
        editor.putString(UserId, id);
        // commit changes
        editor.commit();
    }
    public String getUserId() {
        return pref.getString(UserId, "");
    }



    public void setUserType(int type) {
        editor.putInt(UserType, type);
        // commit changes
        editor.commit();
    }
    public int getUserType() {
        return pref.getInt(UserType, 0);
    }



    public String getKEY_DUE_DATE() {
        return pref.getString(KEY_DUE_DATE, "null");
    }

    public int getpersonID() {
        return pref.getInt(PERSON_ID_CODE, 0);
    }




    public void setEnglish(){
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        _context.getResources().updateConfiguration(config, _context.getResources().getDisplayMetrics());
        editor.putString("lan", "en");
        editor.apply();

    }
    public void setArabic(){
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        _context.getResources().updateConfiguration(config, _context.getResources().getDisplayMetrics());
        editor.putString("lan", "ar");
        editor.apply();

    }

    public void setArabic_firsr(){
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        _context.getResources().updateConfiguration(config, _context.getResources().getDisplayMetrics());
        editor.putString("lan_", "ar2");
        editor.apply();

    }

    /**
     * here to get the user id  from the shared prefrence
     * @return the user id
     */
    public String  getLanguage() {
        return pref.getString("lan","en");
    }

    public String  getLanguage__first() {
        return pref.getString("lan","null");
    }


    public String  getLanguage__first_arabic() {
        return pref.getString("lan_","ar1");
    }



}
