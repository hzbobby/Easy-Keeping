package com.vividbobo.easy.viewmodel;

import android.app.Application;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.repository.ChartRepo;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChartViewModel extends AndroidViewModel {
    private final ChartRepo chartRepo;
    private final LiveData<List<PieEntry>> accountPieEntries;
    private final LiveData<List<PieEntry>> categoryPieEntries;
    private final LiveData<List<PieEntry>> payeePieEntries;
    private final LiveData<List<PieEntry>> rolePieEntries;
    private final LiveData<List<PieEntry>> tagPieEntries;
    private final LiveData<Pair<List<BarEntry>, List<BarEntry>>> inExpBarEntries;
    private final LiveData<List<Bill>> queryBills;

    public ChartViewModel(@NonNull Application application) {
        super(application);
        chartRepo = new ChartRepo(application);
        queryBills = chartRepo.getQueryBills();

        accountPieEntries = chartRepo.getAccountPieEntries();
        categoryPieEntries = chartRepo.getCategoryPieEntries();
        payeePieEntries = chartRepo.getPayeePieEntries();
        rolePieEntries = chartRepo.getRolePieEntries();
        tagPieEntries = chartRepo.getTagPieEntries();

        inExpBarEntries = chartRepo.getInExpBarEntriyPair();


    }

    public LiveData<List<Bill>> getQueryBills() {
        return queryBills;
    }

    public LiveData<List<PieEntry>> getAccountPieEntries() {
        return accountPieEntries;
    }

    public LiveData<List<PieEntry>> getCategoryPieEntries() {
        return categoryPieEntries;
    }

    public LiveData<List<PieEntry>> getPayeePieEntries() {
        return payeePieEntries;
    }

    public LiveData<List<PieEntry>> getRolePieEntries() {
        return rolePieEntries;
    }

    public LiveData<List<PieEntry>> getTagPieEntries() {
        return tagPieEntries;
    }

    public LiveData<Pair<List<BarEntry>, List<BarEntry>>> getInExpBarEntries() {
        return inExpBarEntries;
    }

    public void setDateRange(Date dateStart, Date dateEnd) {
        chartRepo.setDateRange(dateStart, dateEnd);
    }


}