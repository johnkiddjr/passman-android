package es.wolfi.app.passman;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsCache {
    protected static SharedPreferences sharedPreferences = null;
    protected static JSONObject cache = new JSONObject();

    public void loadSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static void clear() {
        cache = new JSONObject();
    }

    public static String getString(String key, String fallback) {
        try {
            if (!cache.has(key)) {
                cache.put(key, sharedPreferences.getString(key, fallback));
            }

            return cache.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fallback;
    }

    public static int getInt(String key, int fallback) {
        try {
            if (!cache.has(key)) {
                cache.put(key, sharedPreferences.getInt(key, fallback));
            }

            return cache.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fallback;
    }

    public static boolean getBoolean(String key, boolean fallback) {
        try {
            if (!cache.has(key)) {
                cache.put(key, sharedPreferences.getBoolean(key, fallback));
            }

            return cache.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fallback;
    }

    public static void runTimingTest(Context context) {
        SettingsCache.clear();

        long beforeOldMethod = System.nanoTime();
        PreferenceManager.getDefaultSharedPreferences(context).getString(SettingValues.PASSWORD_GENERATOR_SETTINGS.toString(), null);
        long diffOldMethod = System.nanoTime() - beforeOldMethod;

        long beforeNewMethodFirst = System.nanoTime();
        SettingsCache.getString(SettingValues.PASSWORD_GENERATOR_SETTINGS.toString(), null);
        long diffNewMethodFirst = System.nanoTime() - beforeNewMethodFirst;

        long beforeNewMethodSecond = System.nanoTime();
        SettingsCache.getString(SettingValues.PASSWORD_GENERATOR_SETTINGS.toString(), null);
        long diffNewMethodSecond = System.nanoTime() - beforeNewMethodSecond;

        /*
        Log.d("diffOldMethod", String.valueOf(diffOldMethod));
        Log.d("diffNewMethodFirst", String.valueOf(diffNewMethodFirst));
        Log.d("diffNewMethodSecond", String.valueOf(diffNewMethodSecond));
        */

        Log.d("First Cache Call", String.format("Speedup %s", (int) (Math.abs(1 - (double) diffNewMethodFirst / (double) diffOldMethod) * 100)) + "%");
        Log.d("Second Cache Call", String.format("Speedup %s", (int) (Math.abs(1 - (double) diffNewMethodSecond / (double) diffOldMethod) * 100)) + "%");
    }
}
