package com.vividbobo.easy.ui.chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.vividbobo.easy.databinding.LayoutConditionCustomBinding;
import com.vividbobo.easy.ui.others.OnItemClickListener;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class CustomConditionFragment extends ConditionFragment<LayoutConditionCustomBinding> implements View.OnClickListener {

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected LayoutConditionCustomBinding createViewBinding(LayoutInflater layoutInflater) {
        return LayoutConditionCustomBinding.inflate(layoutInflater);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configChipClick();
        configDateRangePicker();
    }

    private void configDateRangePicker() {
        MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker().setTitleText("选择日期范围").build();
        binding.customDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateRangePicker.show(getParentFragmentManager(), "DateRangePicker");
            }
        });
        dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Date firstOfDay = new Date(selection.first);
                Date lastOfDay = new Date(selection.second);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                binding.customDateTv.setText(String.format("%s - %s", sdf.format(firstOfDay), sdf.format(lastOfDay)));
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(null, new Pair<Date, Date>(firstOfDay, lastOfDay), -2);
                }
            }
        });
    }

    private void configChipClick() {
        for (int i = 0; i < binding.chipGroup.getChildCount(); i++) {
            View v = binding.chipGroup.getChildAt(i);
            if (v instanceof Chip) {
                v.setOnClickListener(this);
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v instanceof Chip) {
            String chipText = ((Chip) v).getText().toString();
            Integer days = 0;
            switch (chipText) {
                case "近7天" -> days = 7;
                case "近30天" -> days = 30;
                case "近3月" -> days = 90;
                case "近半年" -> days = 180;
            }
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, days, -1);
            }
        }
    }
}
