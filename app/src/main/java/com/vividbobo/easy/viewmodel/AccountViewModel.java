package com.vividbobo.easy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountViewModel extends ViewModel {

    private final MutableLiveData<Boolean> _hideAsserts = new MutableLiveData<>(false);
    public LiveData<Boolean> hideAsserts = _hideAsserts;

    public AccountViewModel() {
    }

    public void setHideAsserts(boolean hideAsserts) {
        _hideAsserts.setValue(hideAsserts);
    }
}