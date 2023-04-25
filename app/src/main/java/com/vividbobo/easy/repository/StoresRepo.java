package com.vividbobo.easy.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.PayeeDao;
import com.vividbobo.easy.database.model.Payee;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.List;
import java.util.concurrent.Callable;

public class StoresRepo {
    //dao
    private PayeeDao payeeDao;


    //livedata
    private final LiveData<List<Payee>> allStores;


    public StoresRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        payeeDao = db.payeeDao();
        allStores = payeeDao.getAllStores();
    }

    public LiveData<List<Payee>> getAllStores() {
        return allStores;
    }

    public void insert(Payee payee) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                payeeDao.insert(payee);
                return null;
            }
        });
    }

    public void update(Payee entity) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                payeeDao.update(entity);
                return null;
            }
        });
    }

    public void delete(Payee item) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                payeeDao.delete(item);
                return null;
            }
        });
    }
}
