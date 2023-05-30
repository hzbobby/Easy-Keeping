package com.vividbobo.easy;

import android.app.Application;
import android.util.Log;

import com.google.android.material.color.DynamicColors;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.mlsdk.common.MLApplication;
import com.vividbobo.easy.hmsml.HmsMlCredential;
import com.vividbobo.easy.utils.AsyncProcessor;
import com.vividbobo.easy.utils.ColorUtils;
import com.vividbobo.easy.utils.ConstantValue;
import com.vividbobo.easy.utils.LogWatcher;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.utils.SharedPrefsUtils;
import com.vividbobo.easy.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

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

        onAppCreateInitial();

        // ml settings
        MLApplication.initialize(getApplicationContext());
        AGConnectServicesConfig config = AGConnectServicesConfig.fromContext(getApplicationContext());
//        MLApplication.getInstance().setApiKey(config.getString("client/api_key"));
        MLApplication.getInstance().setApiKey(HmsMlCredential.API_KEY);
        MLApplication.getInstance().setUserRegion(MLApplication.REGION_DR_CHINA);
    }

    //第一次启动时，做的初始化
    private void onAppCreateInitial() {
//
    }


}
