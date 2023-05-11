package com.vividbobo.easy.repository;

import android.app.Application;
import android.util.Log;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kotlin.jvm.functions.Function1;

public class ChartRepo {

    private final BillDao billDao;
    private final MutableLiveData<Pair<Date, Date>> dateRange;
    private final LiveData<List<Bill>> queryBills;

    private final MutableLiveData<Pair<List<BarEntry>, List<BarEntry>>> inExpBarEntriyPair;
    private final MutableLiveData<List<PieEntry>> categoryPieEntries;
    private final MutableLiveData<List<PieEntry>> tagPieEntries;
    private final MutableLiveData<List<PieEntry>> rolePieEntries;
    private final MutableLiveData<List<PieEntry>> payeePieEntries;
    private final MutableLiveData<List<PieEntry>> accountPieEntries;

    public ChartRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        dateRange = new MutableLiveData<>();
        billDao = db.billDao();
//        queryBills = Transformations.switchMap(dateRange, new Function1<Pair<Date, Date>, LiveData<List<Bill>>>() {
//            @Override
//            public LiveData<List<Bill>> invoke(Pair<Date, Date> dateDatePair) {
//                return Transformations.map(db.configDao().getSelectedLeger(), new Function1<Leger, LiveData<List<Bill>>>() {
//                    @Override
//                    public LiveData<List<Bill>> invoke(Leger leger) {
//                        return billDao.getBillsByDateRangeInLeger(dateDatePair.first, dateDatePair.second, leger.getId());
//                    }
//                }).getValue();
//            }
//        });
        queryBills = Transformations.map(
                Transformations.switchMap(dateRange, dateDatePair -> {
                    // 根据 dateRange 查询选定的 Leger
                    return Transformations.switchMap(
                            db.configDao().getSelectedLeger(),
                            leger -> {
                                // 在选定的 Leger 中根据日期范围查询帐单
                                return billDao.getBillsByDateRangeInLeger(dateDatePair.first, dateDatePair.second, leger.getId());
                            }
                    );
                }),
                this::generateCharts
        );


        inExpBarEntriyPair = new MutableLiveData<>();
        categoryPieEntries = new MutableLiveData<>();
        tagPieEntries = new MutableLiveData<>();
        rolePieEntries = new MutableLiveData<>();
        payeePieEntries = new MutableLiveData<>();
        accountPieEntries = new MutableLiveData<>();
    }

    public LiveData<List<Bill>> getQueryBills() {
        return queryBills;
    }

    public void setDateRange(Date dateStart, Date dateEnd) {
        dateRange.postValue(new Pair<>(dateStart, dateEnd));
    }

    public LiveData<Pair<List<BarEntry>, List<BarEntry>>> getInExpBarEntriyPair() {
        return inExpBarEntriyPair;
    }

    public LiveData<List<PieEntry>> getCategoryPieEntries() {
        return categoryPieEntries;
    }

    public LiveData<List<PieEntry>> getTagPieEntries() {
        return tagPieEntries;
    }

    public LiveData<List<PieEntry>> getRolePieEntries() {
        return rolePieEntries;
    }

    public LiveData<List<PieEntry>> getPayeePieEntries() {
        return payeePieEntries;
    }

    public LiveData<List<PieEntry>> getAccountPieEntries() {
        return accountPieEntries;
    }

    private List<Bill> generateCharts(List<Bill> result) {

        Log.d("TAG", "generateCharts: ");

        Map<Integer, BarEntry> expenditureEntriesMap = new HashMap<>();
        Map<Integer, BarEntry> incomeEntriesMap = new HashMap<>();

        Map<String, PieEntry> categoryMap = new HashMap<>();
        Map<String, PieEntry> tagMap = new HashMap<>();
        Map<String, PieEntry> roleMap = new HashMap<>();
        Map<String, PieEntry> payeeMap = new HashMap<>();
        Map<String, PieEntry> accountMap = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        for (Bill bill : result) {
            /************* initial bar chart *************/
            String keyStr = sdf.format(bill.getDate());
            Integer key = Integer.valueOf(keyStr);

            if (Objects.nonNull(key)) {
                //作为group，两个必须都有数据才可显示
                if (!expenditureEntriesMap.containsKey(key)) {
                    expenditureEntriesMap.put(key, new BarEntry(key, 0f));
                }
                if (!incomeEntriesMap.containsKey(key)) {
                    incomeEntriesMap.put(key, new BarEntry(key, 0f));
                }
            }

            if (bill.getBillType() == Bill.EXPENDITURE) {
                //退款、报销、不计入收支

                if (Objects.requireNonNullElse(bill.getRefund(), false) || Objects.requireNonNullElse(bill.getReimburse(), false) || Objects.requireNonNullElse(bill.getIncomeExpenditureIncluded(), false)) {
                    continue;
                }
                if (Objects.nonNull(key)) {
                    BarEntry entry = expenditureEntriesMap.get(key);
                    float axisY = entry.getY() + ((float) bill.getAmount()) / 100;
                    entry.setY(axisY);
                }

            } else if (bill.getBillType() == Bill.INCOME) {
                if (Objects.requireNonNullElse(bill.getIncomeExpenditureIncluded(), false)) {
                    continue;
                }
                if (Objects.nonNull(key)) {
                    BarEntry entry = incomeEntriesMap.get(key);
                    float axisY = entry.getY() + ((float) bill.getAmount()) / 100;
                    entry.setY(axisY);
                }
            }

//                    /************** initial pie chart ***********/
//                    // category pie chart, category title count
            if (Objects.nonNull(bill.getCategoryTitle())) {
                if (!categoryMap.containsKey(bill.getCategoryTitle())) {
                    categoryMap.put(bill.getCategoryTitle(), new PieEntry(0f, bill.getCategoryTitle()));
                }
                PieEntry pieEntry = categoryMap.get(bill.getCategoryTitle());
                float value = pieEntry.getValue();
                pieEntry.setY(value + bill.getFloatAmount());

            }

            //tags
            if (Objects.nonNull(bill.getTags())) {
                for (Tag tag : bill.getTags()) {
                    if (!tagMap.containsKey(tag.getTitle())) {
                        PieEntry pieEntry = new PieEntry(0f, tag.getTitle());
                        pieEntry.setData((String) tag.getHexCode());
                        tagMap.put(tag.getTitle(), pieEntry);
                    }
                    PieEntry pieEntry = tagMap.get(tag.getTitle());
                    float val = pieEntry.getValue();
                    pieEntry.setY(val + bill.getFloatAmount());

                }
            }

            //role
            if (Objects.nonNull(bill.getRoleTitle())) {
                if (!roleMap.containsKey(bill.getRoleTitle())) {
                    roleMap.put(bill.getRoleTitle(), new PieEntry(0f, bill.getRoleTitle()));
                }
                PieEntry pieEntry = roleMap.get(bill.getRoleTitle());
                float val = pieEntry.getValue();
                pieEntry.setY(val + bill.getFloatAmount());

            }

            //payee
            if (Objects.nonNull(bill.getPayeeTitle())) {
                if (!payeeMap.containsKey(bill.getPayeeTitle())) {
                    payeeMap.put(bill.getPayeeTitle(), new PieEntry(0f, bill.getPayeeTitle()));
                }
                PieEntry pieEntry = payeeMap.get(bill.getPayeeTitle());
                float val = pieEntry.getValue();
                pieEntry.setY(val + bill.getFloatAmount());

            }
            //account
            if (Objects.nonNull(bill.getAccountTitle())) {
                if (!accountMap.containsKey(bill.getAccountTitle())) {
                    accountMap.put(bill.getAccountTitle(), new PieEntry(0f, bill.getAccountTitle()));
                }
                PieEntry pieEntry = accountMap.get(bill.getAccountTitle());
                float val = pieEntry.getValue();
                pieEntry.setY(val + bill.getFloatAmount());

            }
        }

        //generate bar charts

        ArrayList<BarEntry> expenditureEntries = new ArrayList<>(expenditureEntriesMap.values());
        ArrayList<BarEntry> incomeEntries = new ArrayList<>(incomeEntriesMap.values());
        inExpBarEntriyPair.postValue(new Pair<>(expenditureEntries, incomeEntries));

        ArrayList<PieEntry> categoryEntries = new ArrayList<>(categoryMap.values());
        categoryPieEntries.postValue(categoryEntries);

        ArrayList<PieEntry> roleEntries = new ArrayList<>(roleMap.values());
        rolePieEntries.postValue(roleEntries);

        ArrayList<PieEntry> tagEntries = new ArrayList<>(tagMap.values());
        tagPieEntries.postValue(tagEntries);

        ArrayList<PieEntry> payeeEntries = new ArrayList<>(payeeMap.values());
        payeePieEntries.postValue(payeeEntries);

        ArrayList<PieEntry> accountEntries = new ArrayList<>(accountMap.values());
        accountPieEntries.postValue(accountEntries);

        return result;
    }


}
