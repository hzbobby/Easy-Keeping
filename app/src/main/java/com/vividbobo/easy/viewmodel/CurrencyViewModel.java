package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.repository.CurrenciesRepo;
import com.vividbobo.easy.utils.ConstantValue;

import java.util.List;
import java.util.Map;

public class CurrencyViewModel extends AndroidViewModel {
    private final LiveData<List<Currency>> enableCurrencies;
    private final LiveData<Currency> baseCurrency;
    private final MutableLiveData<String> baseCurrencyCode;
    private final LiveData<ListenableFuture<List<Currency>>> listenableFutureOfCurrencies;
    //    private final LiveData<Map<String, Float>> rateMap;
    private CurrenciesRepo currenciesRepo;

    public CurrencyViewModel(@NonNull Application application) {
        super(application);
        currenciesRepo = new CurrenciesRepo(application);
        enableCurrencies = currenciesRepo.getEnableCurrencies();
        listenableFutureOfCurrencies = currenciesRepo.getListenableFutureOfCurrencies();
        baseCurrency = currenciesRepo.getBaseCurrency();
        baseCurrencyCode = currenciesRepo.getBaseCurrencyCode();
//        rateMap = currenciesRepo.getRateMap();
    }

//    public LiveData<Map<String, Float>> getRateMap() {
//        return rateMap;
//    }

    public LiveData<ListenableFuture<List<Currency>>> getListenableFutureOfCurrencies() {
        return listenableFutureOfCurrencies;
    }

    public void setBaseCurrencyCode(String code) {
        baseCurrencyCode.setValue(code);
    }

    public LiveData<List<Currency>> getEnableCurrencies() {
        return enableCurrencies;
    }



    public void updateRate(String baseCode) {
        currenciesRepo.updateRateOf(baseCode);
    }

    public void add(Currency currency) {
        //just set item to be selected
        currenciesRepo.update(currency);
    }

    public void delete(Currency item) {
        //just set item to be unselected
        currenciesRepo.update(item);
    }

    public void update(Currency currency) {
        currenciesRepo.update(currency);
    }

    public void update(List<Currency> currency) {
        currenciesRepo.update(currency);
    }

    public LiveData<Currency> getBaseCurrency() {
        return currenciesRepo.getBaseCurrency();
    }


    public void updateEnable(String code, boolean isCheck) {
        currenciesRepo.updateEnable(code,isCheck);
    }
}
