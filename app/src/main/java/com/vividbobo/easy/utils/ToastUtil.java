package com.vividbobo.easy.utils;

import android.content.Context;
import android.widget.Toast;

import java.net.ContentHandler;

public class ToastUtil {
    private static Context mContext;

    public static void setContext(Context mContext) {
        ToastUtil.mContext = mContext;
    }

    public static void makeToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
