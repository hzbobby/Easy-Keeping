package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.model.BillPresent;
import com.vividbobo.easy.database.model.DayBillPresent;
import com.vividbobo.easy.repository.BillsRepo;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final LiveData<List<BillPresent>> todayBillPresent;
    private final LiveData<DayBillPresent> todayTotalExpenditure;
    private final LiveData<DayBillPresent> todayTotalIncome;
    private BillsRepo billsRepo;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        billsRepo = new BillsRepo(application);
        todayBillPresent = billsRepo.getToDayBillPresents();
        todayTotalExpenditure = billsRepo.getTodayTotalExpenditure();
        todayTotalIncome = billsRepo.getTodayTotalIncome();
    }

    public LiveData<DayBillPresent> getTodayTotalExpenditure() {
        return todayTotalExpenditure;
    }

    public LiveData<DayBillPresent> getTodayTotalIncome() {
        return todayTotalIncome;
    }

    public LiveData<List<BillPresent>> getTodayBillPresent() {
        return todayBillPresent;
    }
}
