package com.vividbobo.easy.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.dao.ConfigDao;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.BillPresent;
import com.vividbobo.easy.database.model.DayBillPresent;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.utils.CalendarUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class BillsRepo {
    //the DAO
    private final BillDao billDao;
    private final ConfigDao configDao;

    //the LiveData
    private final LiveData<Leger> selectedLeger;
    private final LiveData<List<Bill>> todayBillPresents;
    private final LiveData<Long> todayTotalExpenditure;
    private final LiveData<Long> todayTotalIncome;
    private final DayBillPresent dayBillPresent;
    private final LiveData<Role> selectedRole;
    private final LiveData<Account> selectedAccount;


    public BillsRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        this.billDao = db.billDao();
        this.configDao = db.configDao();
        Date today = new Date(System.currentTimeMillis());
        todayBillPresents = billDao.getBillsByDate(today);
        todayTotalExpenditure = billDao.getTotalAmountByDate(today, Bill.EXPENDITURE);
        todayTotalIncome = billDao.getTotalAmountByDate(today, Bill.INCOME);
        dayBillPresent = new DayBillPresent();
        dayBillPresent.setDate("今天");
        dayBillPresent.setWeek(CalendarUtils.getDayOfWeekCN(today));

        selectedLeger = configDao.getSelectedLeger();
        selectedRole = configDao.getSelectedRole();
        selectedAccount = configDao.getSelectedAccount();
    }

    public LiveData<Account> getSelectedAccount() {
        return selectedAccount;
    }

    public LiveData<Role> getSelectedRole() {
        return selectedRole;
    }

    public LiveData<Leger> getSelectedLeger() {
        return selectedLeger;
    }

    public LiveData<List<BillPresent>> getToDayBillPresents() {
        return Transformations.map(todayBillPresents, this::convertToBillPresentList);
    }

    public LiveData<DayBillPresent> getTodayTotalExpenditure() {
        return Transformations.map(todayTotalExpenditure, this::convertToDayPresentExpenditure);
    }

    public LiveData<DayBillPresent> getTodayTotalIncome() {
        return Transformations.map(todayTotalIncome, this::convertToDayPresentIncome);
    }

    private DayBillPresent convertToDayPresentExpenditure(Long aLong) {
        Log.d("TAG", "convertToDayPresentExpenditure: aLong is null? " + (aLong == null));
        if (aLong == null) dayBillPresent.setExpenditureAmount(0L);
        else dayBillPresent.setExpenditureAmount(aLong);
        return dayBillPresent;
    }

    private DayBillPresent convertToDayPresentIncome(Long aLong) {
        Log.d("TAG", "convertToDayPresentIncome: aLong is null? " + (aLong == null));

        if (aLong == null) dayBillPresent.setIncomeAmount(0L);
        else dayBillPresent.setIncomeAmount(aLong);
        return dayBillPresent;
    }

    private List<BillPresent> convertToBillPresentList(List<Bill> bills) {
        List<BillPresent> billPresents = new ArrayList<>();
        for (int i = 0; i < bills.size(); i++) {
            Bill bill = bills.get(i);
            BillPresent bp = new BillPresent(bill);
            billPresents.add(bp);
        }
        return billPresents;
    }
}
