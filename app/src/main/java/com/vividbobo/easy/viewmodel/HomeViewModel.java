package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.BillInfo;
import com.vividbobo.easy.repository.HomeRepo;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final LiveData<BillInfo> todayBillInfo;
    private final LiveData<List<Bill>> todayBills;
    private final LiveData<BillInfo> monthBillInfo;
    private HomeRepo homeRepo;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        homeRepo = new HomeRepo(application);
        todayBills = homeRepo.getTodayBills();
        todayBillInfo = homeRepo.getTodayBillInfo();
        monthBillInfo = homeRepo.getMonthBillInfo();
    }

    public LiveData<BillInfo> getMonthBillInfo() {
        return monthBillInfo;
    }

    public LiveData<List<Bill>> getTodayBills() {
        return todayBills;
    }

    public LiveData<BillInfo> getTodayBillInfo() {
        return todayBillInfo;
    }

    public void updateBill(Bill bill) {
        homeRepo.updateBill(bill);
    }

    public void deleteBill(Bill bill) {
        homeRepo.deleteBill(bill);
    }

    public void updateAccount(Integer accountId, Long amount) {
        homeRepo.updateAccount(accountId,amount);
    }

}
