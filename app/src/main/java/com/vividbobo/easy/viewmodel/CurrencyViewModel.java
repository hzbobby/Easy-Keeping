package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.repository.CurrenciesRepo;

import java.util.List;

public class CurrencyViewModel extends AndroidViewModel {
    private final LiveData<List<Currency>> allCurrencies;
    private final LiveData<List<Currency>> selectedCurrencies;
    private CurrenciesRepo currenciesRepo;

    public CurrencyViewModel(@NonNull Application application) {
        super(application);
        currenciesRepo = new CurrenciesRepo(application);
        allCurrencies = currenciesRepo.getAllCurrencies();
        selectedCurrencies = currenciesRepo.getSelectedCurrencies();
    }

    public LiveData<List<Currency>> getAllCurrencies() {
        return allCurrencies;
    }

    public LiveData<List<Currency>> getSelectedCurrencies() {
        return selectedCurrencies;
    }

    public void add(Currency currency) {
        //just set item to be selected
        currency.setSelected(true);
        currenciesRepo.update(currency);
    }

    public void delete(Currency item) {
        //just set item to be unselected
        item.setSelected(false);
        currenciesRepo.update(item);
    }
}
