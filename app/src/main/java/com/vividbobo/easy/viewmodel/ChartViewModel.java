package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.repository.ChartRepo;

import java.sql.Date;
import java.util.List;

public class ChartViewModel extends AndroidViewModel {
    private final ChartRepo chartRepo;

    public ChartViewModel(@NonNull Application application) {
        super(application);
        chartRepo = new ChartRepo(application);
    }

    public ListenableFuture<List<Bill>> getBillsByDateRange(Date start, Date end) {
        return chartRepo.getBillsByDateRange(start, end);
    }
}