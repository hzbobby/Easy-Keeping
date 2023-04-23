package com.vividbobo.easy;

import android.app.Application;
import android.util.Log;

import com.google.android.material.color.DynamicColors;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.utils.AsyncProcessor;
import com.vividbobo.easy.utils.ColorUtils;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.utils.SharePreferenceUtil;
import com.vividbobo.easy.utils.ToastUtil;

import java.util.Objects;
import java.util.concurrent.Future;

/**
 * 全局Application
 * 可用一些全局初始化
 */
public class EasyApplication extends Application {
    private static final String TAG = "EasyApplication";


    @Override
    public void onCreate() {
        super.onCreate();
        // some utils need context initial
        ResourceUtils.setContext(this);
        ToastUtil.setContext(this);
        ColorUtils.setContext(this);
        // Apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this);

        onFirstLaunchInitial();

    }

    //第一次启动时，做的初始化
    private void onFirstLaunchInitial() {

    }


}
