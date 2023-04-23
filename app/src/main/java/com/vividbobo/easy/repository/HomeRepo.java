package com.vividbobo.easy.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.BillPresent;
import com.vividbobo.easy.database.model.DayBillInfo;
import com.vividbobo.easy.utils.CalendarUtils;
import com.vividbobo.easy.utils.FormatUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeRepo {

    private final BillDao billDao;

    private final LiveData<List<Bill>> todayBills;
    private LiveData<DayBillInfo> todayBillInfo;
    private Date today;

    public HomeRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        billDao = db.billDao();
        today = new Date(System.currentTimeMillis());
        todayBills = billDao.getBillsByDate(today);
        todayBillInfo = billDao.getBillInfoByDate(today);
    }

    public LiveData<DayBillInfo> getTodayBillInfo() {
        return Transformations.map(todayBillInfo, this::wrapDayBillInfo);
    }

    private DayBillInfo wrapDayBillInfo(DayBillInfo dayBillInfo) {
        dayBillInfo.setDate(CalendarUtils.getDateMMdd(today));
        dayBillInfo.setWeek(CalendarUtils.getDayOfWeekCN(today));
        return dayBillInfo;
    }

    public LiveData<List<Bill>> getTodayBills() {
        return todayBills;
    }

    public LiveData<List<Bill>> getBillsByDate(Date date) {
        return billDao.getBillsByDate(date);
    }
}
