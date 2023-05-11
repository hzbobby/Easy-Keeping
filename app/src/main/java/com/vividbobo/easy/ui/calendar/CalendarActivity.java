package com.vividbobo.easy.ui.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.FlowLiveDataConversions;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import com.vividbobo.easy.adapter.adapter.BillAdapter;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.BillInfo;
import com.vividbobo.easy.databinding.ActivityCalendarBinding;
import com.vividbobo.easy.ui.bill.BillActivity;
import com.vividbobo.easy.ui.bill.BillDetailBottomSheet;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.CalendarUtils;
import com.vividbobo.easy.viewmodel.CalendarActivityViewModel;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    private static final String TAG = "CalendarActivity";

    private ActivityCalendarBinding binding;
    private CalendarActivityViewModel calendarViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        calendarViewModel = new ViewModelProvider(this).get(CalendarActivityViewModel.class);

        binding.appBarLayout.layoutToolBarTitleTv.setText("账单日历");
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        BillAdapter billAdapter = new BillAdapter(this);
        billAdapter.setEnableFooter(false);
        binding.billRv.setAdapter(billAdapter);
        billAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Bill bill = (Bill) item;
                BillDetailBottomSheet bottomSheet = BillDetailBottomSheet.newInstance(bill);
                bottomSheet.setOnRefundClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //退款
                        bill.setRefund(true);
                        //dismiss?
                        calendarViewModel.updateBill(bill);
                        bottomSheet.dismiss();
                    }
                });
                bottomSheet.setOnDeleteClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendarViewModel.deleteBill(bill);
                        bottomSheet.dismiss();
                    }
                });
                bottomSheet.setOnEditClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CalendarActivity.this, BillActivity.class);
                        intent.putExtra("data", bill);
                        startActivity(intent);
                    }
                });
                bottomSheet.show(getSupportFragmentManager(), BillDetailBottomSheet.TAG);
            }
        });

        calendarViewModel.getBills().observe(this, new Observer<List<Bill>>() {
            @Override
            public void onChanged(List<Bill> bills) {
                billAdapter.updateItems(bills);
            }
        });

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //create Date
                String dateStr = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                Date date = CalendarUtils.getDateFrom(dateStr, "yyyy-MM-dd");
                Log.d(TAG, "onSelectedDayChange: " + date.toString());
                calendarViewModel.postFilterDate(date);
            }
        });

        calendarViewModel.getMonthBillInfo().observe(this, new Observer<BillInfo>() {
            @Override
            public void onChanged(BillInfo billInfo) {
                billAdapter.setHeaderItem(billInfo);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding = null;
        }
        if (calendarViewModel != null) {
            calendarViewModel = null;
        }
    }
}