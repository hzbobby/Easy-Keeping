package com.vividbobo.easy.ui.bill;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.converter.Converters;
import com.vividbobo.easy.database.converter.LocalTimeConverter;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.databinding.SheetBillDetailBinding;
import com.vividbobo.easy.ui.common.BottomSheetDialog;
import com.vividbobo.easy.utils.ConvertUtils;
import com.vividbobo.easy.utils.FormatUtils;
import com.vividbobo.easy.utils.ResourceUtils;

import java.util.List;
import java.util.Objects;

public class BillDetailBottomSheet extends BottomSheetDialog<SheetBillDetailBinding> {
    public static final String TAG = "BillDetailBottomSheet";

    private View.OnClickListener onRefundClickListener;
    private View.OnClickListener onDeleteClickListener;
    private View.OnClickListener onEditClickListener;

    public void setOnEditClickListener(View.OnClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    public void setOnDeleteClickListener(View.OnClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnRefundClickListener(View.OnClickListener onRefundClickListener) {
        this.onRefundClickListener = onRefundClickListener;
    }

    public static BillDetailBottomSheet newInstance(Bill bill) {

        Bundle args = new Bundle();
        args.putSerializable("data", bill);

        BillDetailBottomSheet fragment = new BillDetailBottomSheet();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public SheetBillDetailBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return SheetBillDetailBinding.inflate(inflater);
    }

    @Override
    public void onViewBinding(SheetBillDetailBinding binding) {
        Bill bill = (Bill) getArguments().getSerializable("data");
        if (bill.getBillType() == Bill.EXPENDITURE) {
            binding.amountTv.setText("-" + FormatUtils.getAmount(bill.getAmount()));
            binding.amountTv.setTextColor(getContext().getColor(R.color.expenditure));
        } else if (bill.getBillType() == Bill.INCOME) {
            binding.amountTv.setText("+" + FormatUtils.getAmount(bill.getAmount()));
            binding.amountTv.setTextColor(getContext().getColor(R.color.income));
        } else {
            binding.amountTv.setText(FormatUtils.getAmount(bill.getAmount()));
            binding.amountTv.setTextColor(getContext().getColor(R.color.transfer));
        }
        binding.refundIconIv.setOnClickListener(onRefundClickListener);
        binding.deleteIconIv.setOnClickListener(onDeleteClickListener);
        binding.editIconIv.setOnClickListener(onEditClickListener);


        //hide tar account
        binding.tarAccountLayout.setVisibility(View.GONE);
        if (bill.getBillType() == Bill.TRANSFER) {
            //hide category, store, other
            binding.categoryLayout.setVisibility(View.GONE);
            binding.storeLayout.setVisibility(View.GONE);
            binding.otherLayout.setVisibility(View.GONE);
            binding.refundIconIv.setVisibility(View.GONE);
            //show src,tar account
            binding.tarAccountLayout.setVisibility(View.VISIBLE);
            binding.srcAccountLabelTv.setText("转出账户");

        } else if (bill.getBillType() == Bill.INCOME) {
            //hide store,
            binding.storeLayout.setVisibility(View.GONE);
            binding.refundIconIv.setVisibility(View.GONE);
        }

        if (Objects.nonNull(bill.getTarAccountIconResName()) && !bill.getTarAccountIconResName().isEmpty()) {
            ResourceUtils.bindImageDrawable(getContext(), ResourceUtils.getDrawable(bill.getTarAccountIconResName())).centerInside().into(binding.tarAccountIconIv);
        }
        if (Objects.nonNull(bill.getAccountIconResName()) && !bill.getAccountIconResName().isEmpty()) {
            ResourceUtils.bindImageDrawable(getContext(), ResourceUtils.getDrawable(bill.getAccountIconResName())).centerInside().into(binding.srcAccountIconIv);
        }
        if (Objects.nonNull(bill.getTarAccountTitle()) && !bill.getTarAccountTitle().isEmpty()) {
            binding.tarAccountTv.setText(bill.getTarAccountTitle());
        }
        if (Objects.nonNull(bill.getAccountTitle()) && !bill.getAccountTitle().isEmpty()) {
            binding.srcAccountTv.setText(bill.getAccountTitle());
        }
        if (Objects.nonNull(bill.getCategoryTitle()) && !bill.getCategoryTitle().isEmpty()) {
            binding.categoryTv.setText(bill.getCategoryTitle());
        }
        if (Objects.nonNull(bill.getCategoryIconResName()) && !bill.getAccountIconResName().isEmpty()) {
            if (bill.getBillType() == Bill.EXPENDITURE) {
                ResourceUtils.bindImageDrawable(getContext(), ResourceUtils.getTintedDrawable(bill.getCategoryIconResName(),
                        ResourceUtils.getColor(R.color.expenditure)
                )).centerInside().into(binding.categoryIconIv);

            } else if (bill.getBillType() == Bill.INCOME) {
                ResourceUtils.bindImageDrawable(getContext(), ResourceUtils.getTintedDrawable(bill.getCategoryIconResName(),
                        ResourceUtils.getColor(R.color.income)
                )).centerInside().into(binding.categoryIconIv);
            }
        }
        if (Objects.nonNull(bill.getRoleTitle()) && !bill.getRoleTitle().isEmpty() && !Objects.equals(bill.getRoleTitle(), "自己")) {
            binding.roleTv.setText(bill.getRoleTitle());
        } else {
            binding.roleLayout.setVisibility(View.GONE);
        }
        if (Objects.nonNull(bill.getPayeeTitle()) && !bill.getPayeeTitle().isEmpty()) {
            binding.storeTv.setText(bill.getPayeeTitle());
        } else {
            binding.storeLayout.setVisibility(View.GONE);
        }
        if (Objects.nonNull(bill.getRemark()) && !bill.getRemark().isEmpty()) {
            binding.remarkTv.setText(bill.getRemark());
        }
        if (Objects.nonNull(bill.getDate())) {
            binding.dateTv.setText(Converters.fromDateToDateText(bill.getDate()));
        }
        if (Objects.nonNull(bill.getTime())) {
            binding.timeTv.setText(LocalTimeConverter.localTimeToString(bill.getTime()));
        }


        binding.createTimeTv.setText(bill.getCreateTime().toString());
        binding.roleTv.setText(bill.getRoleTitle());
        binding.remarkTv.setText(bill.getRemark());
        //setting tags
        if (bill.getTags() != null && !bill.getTags().isEmpty()) {
            for (Tag tag : bill.getTags()) {
                Chip chip = new Chip(getContext());
                ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getContext(), null, 0, com.google.android.material.R.style.Widget_Material3_Chip_Assist_Elevated);
                chip.setChipDrawable(chipDrawable);
                chip.setText(tag.getTitle());
                chip.setTextColor(Color.parseColor(tag.getHexCode()));
                binding.tagChipGroup.addView(chip);
            }
        }

        //其他
        StringBuilder sb = new StringBuilder();
        if (Objects.nonNull(bill.getRefund()) && bill.getRefund()) sb.append("退款 ");
        if (Objects.nonNull(bill.getReimburse()) && bill.getReimburse()) sb.append("报销 ");
        if (Objects.nonNull(bill.getIncomeExpenditureIncluded()) && bill.getIncomeExpenditureIncluded())
            sb.append("不计入收支");
        if (Objects.nonNull(bill.getBudgetIncluded()) && bill.getBudgetIncluded())
            sb.append("不计入预算");
        if (sb.toString().isBlank()) {
            binding.otherTv.setVisibility(View.GONE);
        } else {
            binding.otherTv.setVisibility(View.VISIBLE);
            binding.otherTv.setText(sb.toString());
        }

        if(Objects.isNull(bill.getImagePaths())||bill.getImagePaths().size()==0){
            binding.motionLayout.setVisibility(View.GONE);
        }else{
            binding.motionLayout.setVisibility(View.VISIBLE);

            List<String> pathList = bill.getImagePaths();
            Log.d(TAG, "onViewBinding: imagePathListSize: " + pathList.size());

            binding.carousel.setAdapter(new Carousel.Adapter() {
                @Override
                public int count() {
                    return pathList.size();
                }

                @Override
                public void populate(View view, int index) {
                    ImageFilterView imageFilterView = (ImageFilterView) view;
                    Log.d(TAG, "populate: index: " + index);
                    Log.d(TAG, "populate: path: " + pathList.get(index));
                    Glide.with(getContext()).load(pathList.get(index)).into(imageFilterView);
                }

                @Override
                public void onNewItem(int index) {

                }
            });
        }

    }
}
