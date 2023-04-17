package com.vividbobo.easy.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.CurrencyDao;
import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.List;
import java.util.concurrent.Callable;

public class CurrenciesRepo {
    //dao
    private CurrencyDao currencyDao;

    //livedata
    private final LiveData<List<Currency>> allCurrencies;
    private final LiveData<List<Currency>> selectedCurrencies;


    public CurrenciesRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        currencyDao = db.currencyDao();
        allCurrencies = currencyDao.getAllCurrencies();
        selectedCurrencies = currencyDao.getSelectedCurrencies();
    }

    public LiveData<List<Currency>> getAllCurrencies() {
        return allCurrencies;
    }

    public LiveData<List<Currency>> getSelectedCurrencies() {
        return selectedCurrencies;
    }

    public void update(Currency currency) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                currencyDao.update(currency);
                return null;
            }
        });
    }
}
