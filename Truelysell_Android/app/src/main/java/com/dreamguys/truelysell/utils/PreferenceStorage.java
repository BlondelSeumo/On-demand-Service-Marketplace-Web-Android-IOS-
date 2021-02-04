package com.dreamguys.truelysell.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PreferenceStorage extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    //public static final String PREFS_NAME = AppConstants.PREFS_NAME;
    /**
     * The time before.
     */
    static long timeBefore = 0;
    /**
     * The ctx.
     */
    static Context ctx;
    /**
     * The shared preferences.
     */
    private static SharedPreferences sharedPreferences;
    /**
     * The app ctx.
     */
    private static Context appCtx;

    /**
     * Instantiates a new preference storage.
     *
     * @param context the context
     */
    public PreferenceStorage(Context context) {
        ctx = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    /**
     * Gets the key.
     *
     * @param key the key
     * @return the string value
     */
    public static String getKey(String key) {
        checksharedPreferencesNull();

        String strVal = null;
        try {
            strVal = sharedPreferences.getString(key, null);
        } catch (Exception e) {
        }
        return strVal;
    }

    /**
     * Checkshared preferences null.
     */
    private static void checksharedPreferencesNull() {
        if (sharedPreferences == null && appCtx != null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appCtx);
            sharedPreferences.registerOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener) appCtx);
        }
    }

    /**
     * Gets the int key.
     *
     * @param key the key
     * @return the string value
     */
    public static int getIntKey(String key) {
        checksharedPreferencesNull();
        int strVal = 0;
        try {
            strVal = sharedPreferences.getInt(key, 0);
        } catch (Exception e) {
        }
        return strVal;
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or false.
     * Throws ClassCastException if there is a preference with this
     * name that is not a boolean.
     */
    public static Boolean getBoolKey(String key) {
        checksharedPreferencesNull();
        boolean strVal = false;
        try {
            strVal = sharedPreferences.getBoolean(key, false);
        } catch (Exception e) {
        }
        return strVal;
    }

    /**
     * Gets the string set key.
     *
     * @param key the key
     * @return the string set key
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<String> getStringSetKey(String key) {
        checksharedPreferencesNull();
        ArrayList<String> strVal = null;
        try {
            strVal = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString(key,
                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
        }
        return strVal;
    }

    /**
     * Sets the key.
     *
     * @param key   the key to set
     * @param value the value
     */
    public static void setKey(String key, long value) {
        checksharedPreferencesNull();
        try {
            sharedPreferences.edit().putLong(key, value).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the key.
     *
     * @param key   the key
     * @param value the value
     */
    public static void setKey(String key, String value) {
        checksharedPreferencesNull();
        try {
            sharedPreferences.edit().putString(key, value).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the key.
     *
     * @param key   the key to set
     * @param value the boolean value
     */
    public static void setKey(String key, Boolean value) {
        checksharedPreferencesNull();
        try {
            sharedPreferences.edit().putBoolean(key, value).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the key.
     *
     * @param key   the key to set
     * @param value the int value
     */
    public static void setKey(String key, int value) {
        checksharedPreferencesNull();
        try {
            sharedPreferences.edit().putInt(key, value).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the key.
     *
     * @param key   the key
     * @param value the value
     */
    public static void setKey(String key, ArrayList<?> value) {
        checksharedPreferencesNull();
        try {
            sharedPreferences.edit().putString(key, ObjectSerializer.serialize(value)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the all.
     *
     * @return the all
     */
    public static Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    /**
     * Gets the long key.
     *
     * @param key the key
     * @return the long key
     */
    public static long getLongKey(String key) {
        checksharedPreferencesNull();
        long strVal = 0L;
        try {
            strVal = sharedPreferences.getLong(key, 0L);
        } catch (Exception e) {
        }
        return strVal;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<HashMap<String, String>> getHashMapSetKey(String key) {
        checksharedPreferencesNull();
        ArrayList<HashMap<String, String>> strVal = new ArrayList<HashMap<String, String>>();
        try {
            strVal = (ArrayList<HashMap<String, String>>) ObjectSerializer.deserialize(sharedPreferences.getString(key,
                    ObjectSerializer.serialize(new ArrayList<ArrayList<HashMap<String, String>>>())));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return strVal;
    }

    public static void removeKey(String key) {
        checksharedPreferencesNull();
        try {
            sharedPreferences.edit().remove(key).commit();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public static void clearPref() {
        checksharedPreferencesNull();
        try {
            sharedPreferences.edit().clear().commit();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see android.content.ContextWrapper#getApplicationContext()
     */
    @Override
    public Context getApplicationContext() {
        PreferenceStorage.appCtx = super.getApplicationContext();
        return super.getApplicationContext();
    }

    /*
     * (non-Javadoc)
     * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#
     * onSharedPreferenceChanged(android.content.SharedPreferences,
     * java.lang.String)
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        checksharedPreferencesNull();
    }

}

