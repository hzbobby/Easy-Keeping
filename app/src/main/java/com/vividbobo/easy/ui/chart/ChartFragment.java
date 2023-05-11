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
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
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
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.databinding.FragmentChartBinding;
import com.vividbobo.easy.ui.leger.LegerActivity;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.CalendarUtils;
import com.vividbobo.easy.utils.ColorUtils;
import com.vividbobo.easy.viewmodel.ChartViewModel;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ChartFragment extends Fragment {

    private static final int CHARTS_BY_DAY = 100;
    private static final int CHARTS_BY_MONTH = 101;
    private FragmentChartBinding binding;
    private ChartViewModel chartViewModel;
    private View.OnClickListener toolbarNavigationClickListener;

    //bar chart

    private String toolbarTitle = "图表";
    /************** charts data ************/
    private List<PieEntry> accountPieEntries;
    private List<PieEntry> categoryPieEntries;
    private List<PieEntry> payeePieEntries;
    private List<PieEntry> tagPieEntries;
    private List<PieEntry> rolePieEntries;


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
        configViewModel();

        return root;
    }

    private void configViewModel() {
        chartViewModel.getQueryBills().observe(getActivity(), new Observer<List<Bill>>() {
            @Override
            public void onChanged(List<Bill> bills) {
                Log.d("TAG", "onChanged: bills list");
            }
        });
        chartViewModel.getInExpBarEntries().observe(getActivity(), new Observer<Pair<List<BarEntry>, List<BarEntry>>>() {
            @Override
            public void onChanged(Pair<List<BarEntry>, List<BarEntry>> listListPair) {
                if (Objects.nonNull(listListPair)) {
                    generateBarChart(listListPair);
                }
            }
        });
        chartViewModel.getAccountPieEntries().observe(getActivity(), new Observer<List<PieEntry>>() {
            @Override
            public void onChanged(List<PieEntry> entries) {
                if (Objects.nonNull(entries)) {
                    accountPieEntries = entries;
                }
            }
        });
        chartViewModel.getCategoryPieEntries().observe(getActivity(), new Observer<List<PieEntry>>() {
            @Override
            public void onChanged(List<PieEntry> entries) {
                if (Objects.nonNull(entries)) {
                    categoryPieEntries = entries;
                    generatePieChart(categoryPieEntries, "类别");
                }
            }
        });
        chartViewModel.getPayeePieEntries().observe(getActivity(), new Observer<List<PieEntry>>() {
            @Override
            public void onChanged(List<PieEntry> entries) {
                if (Objects.nonNull(entries)) {
                    payeePieEntries = entries;
                }
            }
        });
        chartViewModel.getTagPieEntries().observe(getActivity(), new Observer<List<PieEntry>>() {
            @Override
            public void onChanged(List<PieEntry> entries) {
                if (Objects.nonNull(entries)) {
                    tagPieEntries = entries;
                }
            }
        });
        chartViewModel.getRolePieEntries().observe(getActivity(), new Observer<List<PieEntry>>() {
            @Override
            public void onChanged(List<PieEntry> entries) {
                if (Objects.nonNull(entries)) {
                    rolePieEntries = entries;
                }
            }
        });
    }

    private void generateBarChart(Pair<List<BarEntry>, List<BarEntry>> listListPair) {
        List<BarEntry> entries1 = listListPair.first;
        List<BarEntry> entries2 = listListPair.second;
        Log.d("TAG", "generateBarChart: entries1 size: "+entries1.size());
        Log.d("TAG", "generateBarChart: entries2 size: "+entries2.size());

        BarDataSet expenditureDataSet = new BarDataSet(entries1, "支出");
        expenditureDataSet.setColor(Color.RED);
        BarDataSet incomeDataSet = new BarDataSet(entries2, "收入");
        incomeDataSet.setColor(Color.GREEN);

        Log.d("TAG", "generateBarChart: barDataSet1 size: "+expenditureDataSet.getStackSize());

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(expenditureDataSet);
        dataSets.add(incomeDataSet);

        BarData barData = new BarData(dataSets);
        barData.setBarWidth(0.45f);


        XAxis xAxis = binding.barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        int entrySize = entries1.size();

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                String label = String.valueOf((int) value);
                String day = label.substring(4);
                String month = label.substring(2, 4);
                return entrySize > 60 ? month : month + "-" + day;
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

    private void configPieChart() {
        View.OnClickListener pieChartSegmentClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.segment_category -> generatePieChart(categoryPieEntries, "类别");
                    case R.id.segment_role -> generatePieChart(rolePieEntries, "角色");
                    case R.id.segment_tag -> generatePieChart(tagPieEntries, "标签");
                    case R.id.segment_payee -> generatePieChart(payeePieEntries, "收款方");
                    case R.id.segment_account -> generatePieChart(accountPieEntries, "账户");
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
                    binding.collapseLayout.setTitle(toolbarTitle);
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

                            chartViewModel.setDateRange(firstDay, lastDay);
                        }
                    });
                    return monthFrag;
                }
                case 1 -> {
                    YearConditionFragment yearFrag = new YearConditionFragment();
                    yearFrag.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, Object item, int position) {
                            Integer year = (Integer) item;
                            Date firstDay = Date.valueOf(LocalDate.of(year, 1, 1).toString());
                            Date lastDay = Date.valueOf(LocalDate.of(year, 12, 31).toString());
                            chartViewModel.setDateRange(firstDay, lastDay);
                        }
                    });
                    return yearFrag;
                }
                case 2 -> {
                    CustomConditionFragment customFrag = new CustomConditionFragment();
                    customFrag.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, Object item, int position) {
                            Date firstDay = null;
                            Date lastDay = null;
                            Calendar calendar = Calendar.getInstance();
                            if (position == -1) {
                                //chip
                                Integer days = (Integer) item;
                                lastDay = Date.valueOf(LocalDate.now().toString());
                                firstDay = Date.valueOf(LocalDate.now().minusDays(days).toString());
                            } else {
                                androidx.core.util.Pair<Date, Date> pair = (androidx.core.util.Pair<Date, Date>) item;
                                firstDay = pair.first;
                                lastDay = pair.second;
                            }

                            chartViewModel.setDateRange(firstDay, lastDay);
                        }
                    });
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

}