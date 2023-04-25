package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.model.Payee;
import com.vividbobo.easy.repository.StoresRepo;

import java.util.List;

public class PayeeViewModel extends AndroidViewModel {
    private final LiveData<List<Payee>> allStores;
    private StoresRepo storesRepo;


    public PayeeViewModel(@NonNull Application application) {
        super(application);
        storesRepo = new StoresRepo(application);
        allStores = storesRepo.getAllStores();
    }

    public LiveData<List<Payee>> getAllStores() {
        return allStores;
    }

    public void insert(Payee payee) {
        storesRepo.insert(payee);
    }

    public void update(Payee entity) {
        storesRepo.update(entity);
    }

    public void delete(Payee item) {
        storesRepo.delete(item);
    }
}
