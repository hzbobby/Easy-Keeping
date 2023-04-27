package com.vividbobo.easy.ui.chart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.databinding.FragmentChartBinding;
import com.vividbobo.easy.ui.leger.LegerActivity;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.AsyncProcessor;
import com.vividbobo.easy.utils.CalendarUtils;
import com.vividbobo.easy.utils.ColorUtils;
import com.vividbobo.easy.viewmodel.ChartViewModel;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChartFragment extends Fragment {

    private static final int CHARTS_BY_DAY = 100;
    private static final int CHARTS_BY_MONTH = 101;
    private FragmentChartBinding binding;
    private ChartViewModel chartViewModel;
    private View.OnClickListener toolbarNavigationClickListener;

    /************** charts data ************/
    //bar chart
    private List<BarEntry> expenditureEntries = null;
    private List<BarEntry> incomeEntries = null;
    //pie chart
    private List<PieEntry> categoryEntries = null;
    private List<PieEntry> roleEntries = null;
    private List<PieEntry> tagEntries = null;
    private List<PieEntry> payeeEntries = null;
    private List<PieEntry> accountEntries = null;


    public static Fragment newInstance(View.OnClickListener onNavigationClickListener) {
        Bundle args = new Bundle();
        ChartFragment fragment = new ChartFragment();
        fragment.setToolbarNavigationClickListener(onNavigationClickListener);
        fragment.setArguments(args);
        return fragment;
    }

    public void setToolbarNavigationClickListener(View.OnClickListener toolbarNavigationClickListener) {
        this.toolbarNavigationClickListener = toolbarNavigationClickListener;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chartViewModel = new ViewModelProvider(getActivity()).get(ChartViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ChartViewModel chartViewModel = new ViewModelProvider(this).get(ChartViewModel.class);

        binding = FragmentChartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        configToolBar();
        configViewPager();
        configPieChart();

        return root;
    }

    private void configPieChart() {
        View.OnClickListener pieChartSegmentClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.segment_category -> generatePieChart(categoryEntries, "类别");
                    case R.id.segment_role -> generatePieChart(roleEntries, "角色");
                    case R.id.segment_tag -> generatePieChart(tagEntries, "标签");
                    case R.id.segment_payee -> generatePieChart(payeeEntries, "收款方");
                    case R.id.segment_account -> generatePieChart(accountEntries, "账户");
                }
            }
        };

        binding.segmentCategory.setOnClickListener(pieChartSegmentClickListener);
        binding.segmentRole.setOnClickListener(pieChartSegmentClickListener);
        binding.segmentTag.setOnClickListener(pieChartSegmentClickListener);
        binding.segmentPayee.setOnClickListener(pieChartSegmentClickListener);
        binding.segmentAccount.setOnClickListener(pieChartSegmentClickListener);
    }

    private void generatePieChart(List<PieEntry> entries, String label) {
        if (entries == null) {
            entries = new ArrayList<>();
        }
        PieDataSet set = new PieDataSet(entries, label);

        List<Integer> colorList = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            PieEntry pieEntry = entries.get(i);
            Integer color = null;
            if (Objects.nonNull((String) pieEntry.getData())) {
                try {
                    color = Color.parseColor((String) pieEntry.getData());
                } catch (Exception e) {
                    color = Color.parseColor(ColorUtils.getRandomColor());
                }
            }
            if (Objects.isNull(color)) {
                color = Color.parseColor(ColorUtils.getRandomColor());
            }
            colorList.add(color);
        }
        set.setColors(colorList);
        set.setValueTextSize(14f);
        set.setValueTextColor(Color.BLACK);


        PieData data = new PieData(set);
        binding.pieChart.setData(data);
        binding.pieChart.invalidate(); // refresh
    }


    private void configToolBar() {
        binding.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toolbarNavigationClickListener != null) {
                    toolbarNavigationClickListener.onClick(null);
                }
            }
        });
        binding.layoutToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.select_leger) {
                    Intent intent = new Intent(getActivity(), LegerActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("TAG", "onOffsetChanged: " + verticalOffset);
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // CollapseToolBarLayout折叠
                    binding.toolBarSegmentGroup.setVisibility(View.GONE);
                    binding.collapseLayout.setTitle("TOOL BAR TITLE");
                    binding.collapseLayout.setCollapsedTitleGravity(Gravity.CENTER);

                } else {
                    // CollapseToolBarLayout展开
                    binding.toolBarSegmentGroup.setVisibility(View.VISIBLE);
                    binding.collapseLayout.setTitle("");
                }
            }
        });

    }

    private void configViewPager() {

        ConditionFragmentAdapter conditionFragAdapter = new ConditionFragmentAdapter(getActivity());
        binding.viewpager2.setAdapter(conditionFragAdapter);
        binding.toolBarSegmentGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                Log.d("TAG", "onButtonChecked: " + String.format("id: %d, checked: %s", checkedId, Boolean.toString(isChecked)));
                if (isChecked) {
                    switch (checkedId) {
                        case R.id.month_seg -> binding.viewpager2.setCurrentItem(0, false);
                        case R.id.year_seg -> binding.viewpager2.setCurrentItem(1, false);
                        case R.id.custom_seg -> binding.viewpager2.setCurrentItem(2, false);
                    }
                }
            }
        });

    }


    private void generateAmountBarChart() {

        BarDataSet expenditureDataSet = new BarDataSet(expenditureEntries, "支出");
        expenditureDataSet.setColor(Color.RED);
        BarDataSet incomeDataSet = new BarDataSet(incomeEntries, "收入");
        incomeDataSet.setColor(Color.GREEN);

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(expenditureDataSet);
        dataSets.add(incomeDataSet);

        BarData barData = new BarData(dataSets);
        barData.setBarWidth(0.45f);


        XAxis xAxis = binding.barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_YEAR, (int) value);
                return sdf.format(calendar.getTime());
            }
        });

        YAxis yAxis = binding.barChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinimum(0f);
        yAxis.setXOffset(20f);

        Legend legend = binding.barChart.getLegend();
        legend.setEnabled(true);
        legend.setDrawInside(false);
        legend.setYOffset(10f);
        legend.setYEntrySpace(0f);
        legend.setXOffset(20f);

        binding.barChart.setData(barData);
        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.setTouchEnabled(true);
        binding.barChart.setDragEnabled(true);
        binding.barChart.setScaleEnabled(true);
        binding.barChart.animateY(1000);
        binding.barChart.invalidate();
    }

    private void generatePieChart() {


        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(18.5f, "Green"));
        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));

        PieDataSet set = new PieDataSet(entries, "Election Results");

        set.setColors(Color.rgb(255, 165, 0), Color.rgb(60, 179, 113), Color.rgb(255, 0, 0));
        set.setValueTextSize(14f);
        set.setValueTextColor(Color.BLACK);

        PieData data = new PieData(set);
        binding.pieChart.setData(data);
        binding.pieChart.invalidate(); // refresh
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class ConditionFragmentAdapter extends FragmentStateAdapter {

        public ConditionFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Log.d("TAG", "createFragment: " + position);
            switch (position) {
                case 0 -> {
                    MonthConditionFragment monthFrag = new MonthConditionFragment();
                    monthFrag.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, Object item, int position) {
                            String yyyyMM = (String) item;
                            String firstOfDate = yyyyMM + "-01";
                            java.sql.Date firstDay = CalendarUtils.getDateFrom(firstOfDate, "yyyy-MM-dd");

                            String[] parts = yyyyMM.split("-");
                            int year = Integer.parseInt(parts[0]);
                            int month = Integer.parseInt(parts[1]);
                            java.sql.Date lastDay = CalendarUtils.getLastDay(year, month);

                            generateCharts(chartViewModel.getBillsByDateRange(firstDay, lastDay), CHARTS_BY_DAY, firstDay, lastDay);
                        }
                    });
                    return monthFrag;
                }
                case 1 -> {
                    YearConditionFragment yearFrag = new YearConditionFragment();
                    return yearFrag;
                }
                case 2 -> {
                    CustomConditionFragment customFrag = new CustomConditionFragment();
                    return customFrag;
                }
                default -> {
                    return null;
                }
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    /**
     * @param billsByDateRange
     * @param type             by day, month
     * @param firstDay
     * @param lastDay
     */
    private void generateCharts(ListenableFuture<List<Bill>> billsByDateRange, int type, Date firstDay, Date lastDay) {
        Futures.addCallback(billsByDateRange, new FutureCallback<List<Bill>>() {
            @Override
            public void onSuccess(List<Bill> result) {

                Map<Integer, BarEntry> expenditureEntriesMap = new HashMap<>();
                Map<Integer, BarEntry> incomeEntriesMap = new HashMap<>();

                Map<String, PieEntry> categoryMap = new HashMap<>();
                Map<String, PieEntry> tagMap = new HashMap<>();
                Map<String, PieEntry> roleMap = new HashMap<>();
                Map<String, PieEntry> payeeMap = new HashMap<>();
                Map<String, PieEntry> accountMap = new HashMap<>();

                Calendar calendar = Calendar.getInstance();
                for (Bill bill : result) {
                    /************* initial bar chart *************/
                    calendar.setTime(bill.getDate());
                    Integer key = null;
                    if (type == CHARTS_BY_DAY) {
                        //按天计算
                        key = calendar.get(Calendar.DAY_OF_YEAR);
                    } else if (type == CHARTS_BY_MONTH) {
                        key = calendar.get(Calendar.MONTH);
                    }
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
                expenditureEntries = new ArrayList<>(expenditureEntriesMap.values());
                incomeEntries = new ArrayList<>(incomeEntriesMap.values());

                categoryEntries = new ArrayList<>(categoryMap.values());
                roleEntries = new ArrayList<>(roleMap.values());
                tagEntries = new ArrayList<>(tagMap.values());
                payeeEntries = new ArrayList<>(payeeMap.values());
                accountEntries = new ArrayList<>(accountMap.values());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        generateAmountBarChart();
                    }
                });

            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, AsyncProcessor.getInstance().getExecutorService());
    }
}