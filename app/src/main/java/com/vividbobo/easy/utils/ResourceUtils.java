package com.vividbobo.easy.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;

/**
 * 用于全局获取resource
 */
public class ResourceUtils {
    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context;
    }

    public static Drawable getDrawable(@DrawableRes int id) {
        return mContext.getDrawable(id);
    }
}
