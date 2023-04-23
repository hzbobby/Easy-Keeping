package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.BillPresent;
import com.vividbobo.easy.database.model.DayBillInfo;
import com.vividbobo.easy.repository.HomeRepo;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final LiveData<DayBillInfo> todayBillInfo;
    private final LiveData<List<Bill>> todayBills;
    private HomeRepo billsRepo;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        billsRepo = new HomeRepo(application);
        todayBills = billsRepo.getTodayBills();
        todayBillInfo = billsRepo.getTodayBillInfo();
    }

    public LiveData<List<Bill>> getTodayBills() {
        return todayBills;
    }

    public LiveData<DayBillInfo> getTodayBillInfo() {
        return todayBillInfo;
    }
}
