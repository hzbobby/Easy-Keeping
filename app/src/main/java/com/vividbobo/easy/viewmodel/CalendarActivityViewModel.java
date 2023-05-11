package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.BillInfo;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.utils.AsyncProcessor;
import com.vividbobo.easy.utils.CalendarUtils;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.Callable;

import kotlin.jvm.functions.Function1;

public class CalendarActivityViewModel extends AndroidViewModel {
    private final MutableLiveData<Date> filterDate;
    private final LiveData<List<Bill>> bills;
    private final LiveData<BillInfo> monthBillInfo;
    private final BillDao billDao;

    public CalendarActivityViewModel(@NonNull Application application) {
        super(application);
        EasyDatabase db = EasyDatabase.getDatabase(application);
        billDao = db.billDao();
        filterDate = new MutableLiveData<>(new Date(System.currentTimeMillis()));
        LiveData<Leger> selectedLeger = db.configDao().getSelectedLeger();

       bills= Transformations.switchMap(filterDate, date -> {
            // 根据 dateRange 查询选定的 Leger
            return Transformations.switchMap(
                    selectedLeger,
                    leger -> {
                        // 在选定的 Leger 中根据日期范围查询帐单
                        return  billDao.getBillsByDateInLeger(date, leger.getId());
                    }
            );
        });

//        bills = Transformations.switchMap(filterDate, new Function1<Date, LiveData<List<Bill>>>() {
//            @Override
//            public LiveData<List<Bill>> invoke(Date date) {
//                Integer legerId = db.configDao().getSelectedLeger().getValue().getId();
//                return billDao.getBillsByDateInLeger(date, legerId);
//            }
//        });
        monthBillInfo= Transformations.switchMap(filterDate, date -> {
            // 根据 dateRange 查询选定的 Leger
            return Transformations.switchMap(
                    selectedLeger,
                    leger -> {
                        // 在选定的 Leger 中根据日期范围查询帐单
                        return  billDao.getBillInfoByDateInLeger(date, leger.getId());
                    }
            );
        });
//        monthBillInfo = Transformations.switchMap(filterDate, new Function1<Date, LiveData<BillInfo>>() {
//            @Override
//            public LiveData<BillInfo> invoke(Date date) {
//                Integer legerId = db.configDao().getSelectedLeger().getValue().getId();
//                return billDao.getBillInfoByDateInLeger(date, legerId);
//            }
//        });
    }

    public LiveData<BillInfo> getMonthBillInfo() {
        return Transformations.map(monthBillInfo, this::wrapDayBillInfo);
    }

    public BillInfo wrapDayBillInfo(BillInfo billInfo) {
        billInfo.setDate(CalendarUtils.getDateMMdd(filterDate.getValue()));
        billInfo.setBalanceAmount(billInfo.getIncomeAmount() - billInfo.getExpenditureAmount());
        return billInfo;
    }

    public LiveData<List<Bill>> getBills() {
        return bills;
    }

    public void postFilterDate(Date date) {
        filterDate.postValue(date);
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
