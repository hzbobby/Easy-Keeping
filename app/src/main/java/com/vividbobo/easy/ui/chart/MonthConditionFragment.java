package com.vividbobo.easy.ui.chart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.vividbobo.easy.R;
import com.vividbobo.easy.databinding.LayoutConditionMonthBinding;
import com.vividbobo.easy.ui.others.OnItemClickListener;

import java.util.Calendar;

public class MonthConditionFragment extends ConditionFragment<LayoutConditionMonthBinding> implements View.OnClickListener {
    private int year;
    private int month;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MonthConditionFragment() {
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
    }


    @Override
    protected LayoutConditionMonthBinding createViewBinding(LayoutInflater layoutInflater) {
        return LayoutConditionMonthBinding.inflate(layoutInflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.navigatePrevImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year--;
                binding.yearTextView.setText(year + "年");
            }
        });
        binding.navigateNextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year++;
                binding.yearTextView.setText(year + "年");
            }
        });

        binding.month1Mbtn.setOnClickListener(this);
        binding.month2Mbtn.setOnClickListener(this);
        binding.month3Mbtn.setOnClickListener(this);
        binding.month4Mbtn.setOnClickListener(this);
        binding.month5Mbtn.setOnClickListener(this);
        binding.month6Mbtn.setOnClickListener(this);
        binding.month7Mbtn.setOnClickListener(this);
        binding.month8Mbtn.setOnClickListener(this);
        binding.month9Mbtn.setOnClickListener(this);
        binding.month10Mbtn.setOnClickListener(this);
        binding.month11Mbtn.setOnClickListener(this);
        binding.month12Mbtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v instanceof MaterialButton) {
            binding.line2Group.clearChecked();
            binding.line1Group.clearChecked();
            ((MaterialButton) v).setChecked(true);
            String yearString = binding.yearTextView.getText().toString();
            yearString = yearString.substring(0, yearString.length() - 1);
            String monthString = ((MaterialButton) v).getText().toString();
            monthString = monthString.substring(0, monthString.length() - 1).strip();
            month = Integer.parseInt(monthString);
            Log.d("TAG", "onClick: " + String.format("%d-%d", year, month));
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, String.format("%04d-%02d", year, month), -1);
            }
        }
    }
}
