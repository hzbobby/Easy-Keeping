package com.vividbobo.easy.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.databinding.LayoutVoiceBillBinding;
import com.vividbobo.easy.googleml.GoogleBillExtractor;
import com.vividbobo.easy.ui.bill.MoreCategoryBottomSheet;
import com.vividbobo.easy.ui.common.BottomSheetDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.viewmodel.TextIdentifyViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

public class TextIdentifyBottomSheet extends BottomSheetDialog<LayoutVoiceBillBinding> {
    public static final String TAG = "TextIdentifyBottomSheet";
    private GoogleBillExtractor googleEntityExtractor;
    private TextIdentifyViewModel textIdentifyViewModel;
    private Category selectedCategory;
    private Leger lastLeger;
    private Account lastAccount;
    private Category expCategory;
    private Category inCategory;

    public static TextIdentifyBottomSheet newInstance() {

        Bundle args = new Bundle();

        TextIdentifyBottomSheet fragment = new TextIdentifyBottomSheet();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public LayoutVoiceBillBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return LayoutVoiceBillBinding.inflate(inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textIdentifyViewModel = new ViewModelProvider(this).get(TextIdentifyViewModel.class);
    }

    @Override
    public void onViewBinding(LayoutVoiceBillBinding binding) {
        googleEntityExtractor = new GoogleBillExtractor();
        googleEntityExtractor.setOnAnalyzeDoneListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleEntityExtractor.isSuccess()) {

                    String dateStr = googleEntityExtractor.getDate();
                    String amountStr = googleEntityExtractor.getAmount();
                    binding.calendarTv.setText(dateStr);
                    binding.amountTv.setText(amountStr);

                    binding.analyzeResultLayout.setVisibility(View.VISIBLE);
                    binding.saveTv.setVisibility(View.VISIBLE);
                } else {
                    binding.analyzeResultLayout.setVisibility(View.GONE);
                    binding.saveTv.setVisibility(View.GONE);
                }
            }
        });

        binding.calendarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder<Long> datePickerBuilder = MaterialDatePicker.Builder.datePicker().setTitleText("选择日期");
                if (googleEntityExtractor.isSuccess()) {
                    String dateStr = binding.calendarTv.getText().toString();
                    Log.d(TAG, "onClick: datePicker: " + dateStr);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    try {
                        Date date = sdf.parse(dateStr);
                        datePickerBuilder.setSelection(date.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                MaterialDatePicker<Long> datePicker = datePickerBuilder.build();
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        binding.calendarTv.setText(sdf.format(selection));
                    }
                });
                datePicker.show(getParentFragmentManager(), "DatePicker");
            }
        });

        binding.categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreCategoryBottomSheet categoryBottomSheet = MoreCategoryBottomSheet.newInstance(googleEntityExtractor.getBillType());
                categoryBottomSheet.show(getParentFragmentManager(), MoreCategoryBottomSheet.TAG);
                categoryBottomSheet.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, Object item, int position) {
                        selectedCategory = (Category) item;
                        ResourceUtils.bindImageDrawable(getContext(), ResourceUtils.getDrawable(selectedCategory.getIconResName())).centerInside().into(binding.categoryIv);
                        binding.categoryTv.setText(selectedCategory.getTitle());
                        categoryBottomSheet.dismiss();
                    }
                });
            }
        });
        binding.textInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: ");
                googleEntityExtractor.extract(s.toString());
                Log.d(TAG, "onTextChanged: isSuccess" + googleEntityExtractor.isSuccess());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double amount = null;
                try {
                    amount = Double.parseDouble(googleEntityExtractor.getAmount());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                java.sql.Date date = null;
                try {
                    date = new java.sql.Date(sdf.parse(googleEntityExtractor.getDate()).getTime());
                } catch (ParseException e) {
//                    throw new RuntimeException(e);
                    e.printStackTrace();
                }
                if (Objects.isNull(date)) {
                    Toast.makeText(getContext(), "识别日期出错", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Objects.isNull(amount)) {
                    Toast.makeText(getContext(), "识别金额出错", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Objects.isNull(selectedCategory)) {
                    if (googleEntityExtractor.getBillType() == Bill.EXPENDITURE) {
                        selectedCategory = expCategory;
                    } else {
                        selectedCategory = inCategory;
                    }
                }

                Bill bill = Bill.createBill(googleEntityExtractor.getBillType(), (long) (amount * 100), lastLeger);

                bill.setCategory(selectedCategory);
                bill.setAccount(lastAccount);
                bill.setDate(date);
                bill.setTime(LocalTime.now());
                bill.setRemark(googleEntityExtractor.getRemark());

                textIdentifyViewModel.insert(bill);
                dismiss();
            }
        });

        configViewModel();
    }

    private void configViewModel() {
        textIdentifyViewModel.getLastAccount().observe(this, new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                lastAccount = account;
            }
        });
        textIdentifyViewModel.getLastLeger().observe(this, new Observer<Leger>() {
            @Override
            public void onChanged(Leger leger) {
                lastLeger = leger;
            }
        });
        textIdentifyViewModel.getExpCategory().observe(this, new Observer<Category>() {
            @Override
            public void onChanged(Category category) {
                expCategory = category;
            }
        });
        textIdentifyViewModel.getInCategory().observe(this, new Observer<Category>() {
            @Override
            public void onChanged(Category category) {
                inCategory = category;
            }
        });

    }
}
