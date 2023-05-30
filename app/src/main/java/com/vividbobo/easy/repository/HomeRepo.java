package com.vividbobo.easy.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.AccountDao;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.dao.LegerDao;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.BillInfo;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.utils.AsyncProcessor;
import com.vividbobo.easy.utils.CalendarUtils;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.Callable;

import kotlin.jvm.functions.Function1;

public class HomeRepo {

    private final BillDao billDao;
    private final LiveData<List<Bill>> todayBills;
    private final LiveData<BillInfo> monthBillInfo;
    private final AccountDao accountDao;
    private LiveData<BillInfo> todayBillInfo;
    private Date today;

    public HomeRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        billDao = db.billDao();
        accountDao = db.accountDao();
        today = new Date(System.currentTimeMillis());
        LiveData<Leger> selectedLeger = db.configDao().getSelectedLeger();
        todayBills = Transformations.switchMap(selectedLeger, new Function1<Leger, LiveData<List<Bill>>>() {
            @Override
            public LiveData<List<Bill>> invoke(Leger leger) {
                Integer legerId = 1;
                if (leger != null) {
                    legerId = leger.getId();
                }
                return billDao.getBillsByDateInLeger(today, legerId);
            }
        });
        todayBillInfo = Transformations.switchMap(selectedLeger, new Function1<Leger, LiveData<BillInfo>>() {
            @Override
            public LiveData<BillInfo> invoke(Leger leger) {
                Integer legerId = 1;
                if (leger != null) {
                    legerId = leger.getId();
                }
                return billDao.getBillInfoByDateInLeger(today, legerId);
            }
        });
        monthBillInfo = Transformations.switchMap(selectedLeger, new Function1<Leger, LiveData<BillInfo>>() {
            @Override
            public LiveData<BillInfo> invoke(Leger leger) {
                Integer legerId = 1;
                if (leger != null) {
                    legerId = leger.getId();
                }
                return billDao.getBillInfoByMonthInLeger(CalendarUtils.getYYYYMM(today), legerId);
            }
        });
    }

    public LiveData<BillInfo> getMonthBillInfo() {
        return Transformations.map(monthBillInfo, this::wrapMonthBillInfo);
    }

    public LiveData<BillInfo> getTodayBillInfo() {
        return Transformations.map(todayBillInfo, this::wrapDayBillInfo);
    }

    public BillInfo wrapMonthBillInfo(BillInfo billInfo) {
        billInfo.setDate(CalendarUtils.getYYYYMM(today));
        billInfo.setBalanceAmount(billInfo.getIncomeAmount() - billInfo.getExpenditureAmount());
        return billInfo;
    }

    private BillInfo wrapDayBillInfo(BillInfo billInfo) {
        billInfo.setDate(CalendarUtils.getDateMMdd(today));
        billInfo.setWeek(CalendarUtils.getDayOfWeekCN(today));
        return billInfo;
    }

    public LiveData<List<Bill>> getTodayBills() {
        return todayBills;
    }


    public void updateBill(Bill bill) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                billDao.update(bill);
                return null;
            }
        });
    }

    public void deleteBill(Bill bill) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                billDao.delete(bill);
                return null;
            }
        });
    }

    public void updateAccount(Integer accountID, Long amount) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Account account = accountDao.getRawAccountById(accountID);
                account.setBalance(account.getBalance() + amount);
                accountDao.update(account);
                return null;
            }
        });
    }
}
