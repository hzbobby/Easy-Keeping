package com.vividbobo.easy.utils;

import android.content.Context;

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
    private final static String KEY_LEGER = "leger";
    private final static String KEY_ACCOUNT = "account";
    private final static String DEFAULT_CODE = "CNY";


    // get base code
    public static String getCode(Context context) {
        return context.getSharedPreferences(CURRENCY_CODE_SP, Context.MODE_PRIVATE).getString(KEY_CODE, DEFAULT_CODE);
    }

    public static void setCode(Context context, String code) {
        context.getSharedPreferences(CURRENCY_CODE_SP, Context.MODE_PRIVATE).edit().putString(KEY_CODE, code).apply();
    }

    // get the rate map from sp file
    public static Map<String, Float> getRateMap(Context context) {
        String json = context.getSharedPreferences(CURRENCY_RATE_SP, Context.MODE_PRIVATE).getString(KEY_RATE, null);
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
        context.getSharedPreferences(CURRENCY_RATE_SP, Context.MODE_PRIVATE).edit().putString(KEY_RATE, json).apply();
    }

//    public static void setLeger(Context context, Leger leger) {
//        Gson gson = new Gson();
//        String json = gson.toJson(leger);
//        if (json == null || json.isEmpty()) return;
//        context.getSharedPreferences(KEY_LEGER, Context.MODE_PRIVATE).edit().putString(KEY_LEGER, json).apply();
//    }

//    public static Leger getLeger(Context context) {
//        String json = context.getSharedPreferences(KEY_LEGER, Context.MODE_PRIVATE).getString(KEY_LEGER, null);
//        if (json == null) return null;
//        Type type = new TypeToken<Leger>() {
//        }.getType();
//        Gson gson = new Gson();
//        Leger leger = gson.fromJson(json, type);
//        return leger;
//    }


//    public static Account getAccount(Context context) {
//        String json = context.getSharedPreferences(KEY_ACCOUNT, Context.MODE_PRIVATE).getString(KEY_ACCOUNT, null);
//        if (json == null || json.isEmpty()) {
//            return null;
//        }
//        Gson gson = new Gson();
//        Type type = new TypeToken<Account>() {
//
//        }.getType();
//        return gson.fromJson(json, type);
//    }

//    public static void setAccount(Context context, Account account) {
//        Gson gson = new Gson();
//        String json = gson.toJson(account);
//        if (json == null || json.isEmpty()) {
//            return;
//        }
//        context.getSharedPreferences(KEY_ACCOUNT, Context.MODE_PRIVATE).edit().putString(KEY_ACCOUNT, json).apply();
//    }
}
