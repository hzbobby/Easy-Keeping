package com.vividbobo.easy.model.viewmodel;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/***
 * 共享一些全局变量
 */
public class GlobalViewModel extends ViewModel {
    private MutableLiveData<Long> _leger_id = new MutableLiveData<>(0L);
    public LiveData<Long> leger_id = _leger_id;

    public void setLegerId(Long legerId) {
        _leger_id.setValue(legerId);
    }
}
