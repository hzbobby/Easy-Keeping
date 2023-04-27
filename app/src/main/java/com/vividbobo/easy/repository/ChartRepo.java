package com.vividbobo.easy.repository;

import android.app.Application;

import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.model.Bill;

import java.sql.Date;
import java.util.List;

public class ChartRepo {

    private final BillDao billDao;

    public ChartRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        billDao = db.billDao();
    }

    public ListenableFuture<List<Bill>> getBillsByDateRange(Date start, Date end) {
        return billDao.getBillsByDateRange(start, end);
    }
}
