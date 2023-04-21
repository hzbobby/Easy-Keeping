package com.vividbobo.easy;

import android.app.Application;

import com.google.android.material.color.DynamicColors;
import com.vividbobo.easy.utils.ColorUtils;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.utils.SharePreferenceUtil;
import com.vividbobo.easy.utils.ToastUtil;

/**
 * 全局Application
 * 可用一些全局初始化
 */
public class EasyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        // some utils need context initial
        ResourceUtils.setContext(this);
        ToastUtil.setContext(this);
        ColorUtils.setContext(this);
        // Apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this);
    }


}
