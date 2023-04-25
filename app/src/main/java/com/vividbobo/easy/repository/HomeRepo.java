package com.vividbobo.easy.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.BillInfo;
import com.vividbobo.easy.utils.AsyncProcessor;
import com.vividbobo.easy.utils.CalendarUtils;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class HomeRepo {

    private final BillDao billDao;

    private final LiveData<List<Bill>> todayBills;
    private final LiveData<BillInfo> monthBillInfo;
    private LiveData<BillInfo> todayBillInfo;
    private Date today;

    public HomeRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        billDao = db.billDao();
        today = new Date(System.currentTimeMillis());
        todayBills = billDao.getBillsByDate(today);
        todayBillInfo = billDao.getBillInfoByDate(today);
        monthBillInfo = billDao.getBillInfoByMonth(CalendarUtils.getDateYYYYMM(today));
    }

    public LiveData<BillInfo> getMonthBillInfo() {
        return Transformations.map(monthBillInfo, this::wrapMonthBillInfo);
    }

    public LiveData<BillInfo> getTodayBillInfo() {
        return Transformations.map(todayBillInfo, this::wrapDayBillInfo);
    }

    public BillInfo wrapMonthBillInfo(BillInfo billInfo) {
        billInfo.setDate(CalendarUtils.getDateYYYYMM(today));
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

    public LiveData<List<Bill>> getBillsByDate(Date date) {
        return billDao.getBillsByDate(date);
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
}
