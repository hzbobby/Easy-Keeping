package com.vividbobo.easy.ui.chart;

import android.icu.util.LocaleData;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.vividbobo.easy.databinding.LayoutConditionYearBinding;
import com.vividbobo.easy.ui.others.OnItemClickListener;

import java.time.LocalDate;

public class YearConditionFragment extends ConditionFragment<LayoutConditionYearBinding> implements View.OnClickListener {
    private int lastBtnYear;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected LayoutConditionYearBinding createViewBinding(LayoutInflater layoutInflater) {
        return LayoutConditionYearBinding.inflate(layoutInflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lastBtnYear = LocalDate.now().getYear();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configBtnClick();

        refreshBtnText(lastBtnYear);
        binding.navigateNextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastBtnYear += 10;
//                if (lastBtnYear > LocalDate.now().getYear()) {
//                    lastBtnYear = LocalDate.now().getYear();
//                }
                refreshBtnText(lastBtnYear);
            }
        });
        binding.navigatePrevImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastBtnYear -= 10;
                refreshBtnText(lastBtnYear);
            }
        });


    }

    private void configBtnClick() {
        for (int i = 0; i < binding.line2Group.getChildCount(); i++) {
            View v = binding.line2Group.getChildAt(i);
            if (v instanceof MaterialButton) {
                v.setOnClickListener(this);
            }
        }
        for (int i = 0; i < binding.line1Group.getChildCount(); i++) {
            View v = binding.line1Group.getChildAt(i);
            if (v instanceof MaterialButton) {
                v.setOnClickListener(this);
            }
        }
    }

    private void refreshBtnText(int year) {
        for (int i = binding.line2Group.getChildCount() - 1; i >= 0; i--, year--) {
            View v = binding.line2Group.getChildAt(i);
            if (v instanceof MaterialButton) {
                ((MaterialButton) v).setText(String.valueOf(year));
            }
        }
        for (int i = binding.line1Group.getChildCount() - 1; i >= 0; i--, year--) {
            View v = binding.line1Group.getChildAt(i);
            if (v instanceof MaterialButton) {
                ((MaterialButton) v).setText(String.valueOf(year));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof MaterialButton) {
            binding.line2Group.clearChecked();
            binding.line1Group.clearChecked();
            ((MaterialButton) v).setChecked(true);
            String yearString = ((MaterialButton) v).getText().toString();

            Integer year = Integer.parseInt(yearString);
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, year, -1);
            }
        }
    }
}
