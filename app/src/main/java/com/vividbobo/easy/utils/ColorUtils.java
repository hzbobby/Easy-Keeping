package com.vividbobo.easy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import com.vividbobo.easy.R;

public class ColorUtils {

    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context;
    }

    public static String getRandomColor() {
        StringBuilder sb = new StringBuilder("#");
        for (int i = 0; i < 6; i++) {
            int flag = (int) (Math.random() * 2);
            if (flag == 1) {
                //A-E
                sb.append((char) ('A' + (int) (Math.random() * 6)));
            } else {
                //0-9
                sb.append((char) ('0' + (int) (Math.random() * 10)));
            }
        }
        return sb.toString();
    }

    public static String colorToHex(@ColorRes int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }


}
