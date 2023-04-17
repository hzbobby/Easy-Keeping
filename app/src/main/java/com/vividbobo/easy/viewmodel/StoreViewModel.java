package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.model.Store;
import com.vividbobo.easy.repository.StoresRepo;

import java.util.List;

public class StoreViewModel extends AndroidViewModel {
    private final LiveData<List<Store>> allStores;
    private StoresRepo storesRepo;


    public StoreViewModel(@NonNull Application application) {
        super(application);
        storesRepo = new StoresRepo(application);
        allStores = storesRepo.getAllStores();
    }

    public LiveData<List<Store>> getAllStores() {
        return allStores;
    }

    public void insert(Store store) {
        storesRepo.insert(store);
    }

    public void update(Store entity) {
        storesRepo.update(entity);
    }

    public void delete(Store item) {
        storesRepo.delete(item);
    }
}
