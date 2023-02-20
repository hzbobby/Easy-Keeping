package com.vividbobo.easy;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.model.viewmodel.GlobalViewModel;
import com.vividbobo.easy.utils.DrawableUtils;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.utils.ToastUtil;

/**
 * 全局Application
 * 可用一些全局初始化
 */
public class EasyApplication extends Application {
    {
        ResourceUtils.setContext(this);        //initial
        ToastUtil.setContext(this);
        DrawableUtils.setContext(this);
    }

}
