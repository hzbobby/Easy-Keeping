package com.vividbobo.easy;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.model.viewmodel.GlobalViewModel;

public class BaseActivity extends AppCompatActivity {
    static GlobalViewModel globalViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //用于全局变量的共享
        globalViewModel = new ViewModelProvider(this).get(GlobalViewModel.class);
    }

    public static GlobalViewModel getGlobalViewModel() {
        return globalViewModel;
    }
}
