package com.vividbobo.easy.repository;

import android.app.Application;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.concurrent.Callable;

public class BillsRepo {
    //the DAO
    private final BillDao billDao;

    //the LiveData



    public BillsRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        this.billDao = db.billDao();


    }







    public void insert(Bill bill) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                billDao.insert(bill);
                return null;
            }
        });
    }
}
