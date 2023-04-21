package com.vividbobo.easy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.internal.PreferenceImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SharePreferenceUtil {
    public final static String CURRENCY_CODE_SP = "currency_code_sp";
    public final static String CURRENCY_RATE_SP = "currency_rate_sp";
    private final static String KEY_CODE = "code";
    private final static String KEY_RATE = "rateMap";
    private final static String DEFAULT_CODE = "CNY";


    // get base code
    public static String getCode(Context context) {
        return context.getSharedPreferences(CURRENCY_CODE_SP, Context.MODE_PRIVATE).getString(KEY_CODE, DEFAULT_CODE);
    }

    public static void setCode(Context context, String code) {
        context.getSharedPreferences(CURRENCY_CODE_SP, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_CODE, code)
                .apply();
    }

    // get the rate map from sp file
    public static Map<String, Float> getRateMap(Context context) {
        String json = context.getSharedPreferences(CURRENCY_RATE_SP, Context.MODE_PRIVATE)
                .getString(KEY_RATE, null);
        if (json == null) return null;
        Type type = new TypeToken<HashMap<String, Float>>() {
        }.getType();
        Gson gson = new Gson();
        Map<String, Float> stringFloatMap = gson.fromJson(json, type);
        if (stringFloatMap != null) return stringFloatMap;
        else return new HashMap<>();
    }

    public static void setRateMap(Context context, Map<String, Float> rateMap) {
        Gson gson = new Gson();
        String json = gson.toJson(rateMap);
        if (json == null || json.isEmpty()) return;
        context.getSharedPreferences(CURRENCY_RATE_SP, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_RATE, json)
                .apply();
    }


}
