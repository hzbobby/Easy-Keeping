package com.vividbobo.easy.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.ConfigDao;
import com.vividbobo.easy.database.dao.LegerDao;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.List;
import java.util.concurrent.Callable;

public class LegersRepo {

    private final LegerDao legerDao;
    private final LiveData<List<Leger>> allLegersLD;

    public LegersRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        legerDao = db.legerDao();
        allLegersLD = legerDao.getAllLegersLD();
    }


    public LiveData<List<Leger>> getAllLegersLD() {
        return allLegersLD;
    }

    public void insert(Leger leger) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                legerDao.insert(leger);
                Log.d("TAG", "call: leger insert");
                return null;
            }
        });
    }

    public void update(Leger editLeger) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                legerDao.update(editLeger);
                return null;
            }
        });
    }

    public void delete(Leger item) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                legerDao.delete(item);
                return null;
            }
        });
    }

    public ListenableFuture<Leger> getLegerById(Integer defaultId) {
        return legerDao.getLegerByID(defaultId);
    }

}
