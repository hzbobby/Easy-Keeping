package com.vividbobo.easy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SharedPrefsUtils {
    private static final String TAG = "SharedPrefsUtils";
    private static final String PREFS_NAME = "MyPrefs";
    public static final String SHARE_PREFS_CATEGORY_MAPPING = "categoryMapping";

    public static final String INITIAL_CATEGORY_MAPPING = "categoryMappingInitial";

    public static void saveMap(Context context, Map<String, String> map, String mapKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String mapString = gson.toJson(map);
        Log.d(TAG, "saveMap: mapSize: " + map.size());
        Log.d(TAG, "saveMap: mapString: " + mapString);
        editor.putString(mapKey, mapString);
        editor.commit();
    }

    public static Map<String, String> loadMap(Context context, String mapKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String mapString = prefs.getString(mapKey, null);
        if (mapString != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            return gson.fromJson(mapString, type);
        } else {
            return new HashMap<>();
        }
    }

    public static void saveBoolean(Context context, boolean value, String boolKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(boolKey, value);
        editor.commit();
    }

    public static boolean loadBoolean(Context context, String boolKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(boolKey, false);
    }
}