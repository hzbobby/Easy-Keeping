package com.vividbobo.easy.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

public class DrawableUtils {
    private static final String TAG = "DrawableUtils";
    private static Context mContext;

    public static void setContext(Context mContext) {
        DrawableUtils.mContext = mContext;
    }

    public static VectorDrawableCompat getDrawable(@DrawableRes int resId, String color) {
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(mContext.getResources(), resId, null);
        if (color != null)
            vectorDrawableCompat.setTint(Color.parseColor(color));
        return vectorDrawableCompat;
    }



}
