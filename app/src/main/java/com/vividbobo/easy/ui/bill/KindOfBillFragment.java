package com.vividbobo.easy.ui.bill;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.vividbobo.easy.databinding.FragmentKindOfBillBinding;
import com.vividbobo.easy.model.TagItem;
import com.vividbobo.easy.model.viewmodel.BillViewModel;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class KindOfBillFragment extends Fragment {
    public static final int KIND_OUTCOME = 0;
    public static final int KIND_INCOME = 1;
    public static final int KIND_TRANSFER = 2;


    //记录Fragment的类型
    private Integer kindOfBill = KIND_OUTCOME;

    public void setKindOfBill(Integer kindOfBill) {
        this.kindOfBill = kindOfBill;
    }

    static KindOfBillFragment newInstance(int kindOfBill, Object data) {
        KindOfBillFragment fragment = new KindOfBillFragment();
        fragment.setKindOfBill(kindOfBill);
        //如果需要传入数据
        Bundle bundle = new Bundle();

        fragment.setArguments(bundle);
        return fragment;
    }

    FragmentKindOfBillBinding binding;
    private RemarkTagBottomSheet remarkTagBottomSheet;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        BillViewModel billViewModel = new ViewModelProvider(getActivity()).get(BillViewModel.class);

        binding = FragmentKindOfBillBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //如果是编辑


        //initial
        binding.billFragRemarkTagLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //点击备注，打开bottomsheet进行输入
                remarkTagBottomSheet = new RemarkTagBottomSheet();
                remarkTagBottomSheet.show(getActivity().getSupportFragmentManager(), RemarkTagBottomSheet.TAG);
            }
        });

        billViewModel.remark.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.billFragRemarkText.setText(s);
            }
        });

        billViewModel.tagItems.observe(getViewLifecycleOwner(), new Observer<List<TagItem>>() {
            @Override
            public void onChanged(List<TagItem> tagItems) {
                setTags(tagItems);
            }
        });

        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("选择日期").build();
        //选择日期
        binding.billFragChipDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getParentFragmentManager(), "BillDatePicker");
            }
        });

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                //view model share selection
                billViewModel.setDate((Long) selection);
            }
        });
        datePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.dismiss();
            }
        });
        //date text observe
        billViewModel.date.observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                //TODO need translation
                binding.billFragChipDate.setText(String.valueOf(aLong));
            }
        });

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder().setTitleText("时间选择").build();
        //选择时间
        binding.billFragChipTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.show(getParentFragmentManager(), "BillTimePicker");
            }
        });
        timePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.dismiss();
            }
        });
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer time[] = new Integer[2];
                time[0] = timePicker.getHour();
                time[1] = timePicker.getMinute();
                billViewModel.setTime(time);
            }
        });
        billViewModel.time.observe(getViewLifecycleOwner(), new Observer<Integer[]>() {
            @Override
            public void onChanged(Integer[] integers) {
                String time = String.format("%02d:%02d", integers[0], integers[1]);
                binding.billFragChipTime.setText(time);
            }
        });


        return root;
    }

    private void setTags(List<TagItem> tagItems) {
        binding.billFragTagText.setHint("[#标签]");
        binding.billFragTagText.setText("");
        for (int i = 0; i < tagItems.size(); i++) {
            TagItem tagItem = tagItems.get(i);
            String tag = String.format("#%s ", tagItem.getText());
            SpannableString spannableString = new SpannableString(tag);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(tagItem.getColor())), 0, tag.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.billFragTagText.append(spannableString);
        }
    }


}