package com.vividbobo.easy.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.StoreDao;
import com.vividbobo.easy.database.model.Store;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.nio.channels.AsynchronousChannelGroup;
import java.util.List;
import java.util.concurrent.Callable;

public class StoresRepo {
    //dao
    private StoreDao storeDao;


    //livedata
    private final LiveData<List<Store>> allStores;


    public StoresRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        storeDao = db.storeDao();
        allStores = storeDao.getAllStores();
    }

    public LiveData<List<Store>> getAllStores() {
        return allStores;
    }

    public void insert(Store store) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                storeDao.insert(store);
                return null;
            }
        });
    }

    public void update(Store entity) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                storeDao.update(entity);
                return null;
            }
        });
    }

    public void delete(Store item) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                storeDao.delete(item);
                return null;
            }
        });
    }
}
