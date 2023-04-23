package com.vividbobo.easy.ui.bill;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.vividbobo.easy.databinding.SheetBillTemplateBinding;
import com.vividbobo.easy.ui.common.BottomSheetDialog;

public class BillTemplateSheetDialog extends BottomSheetDialog<SheetBillTemplateBinding> {
    public static final String TAG = "BillTemplateSheetDialog";

    public static BillTemplateSheetDialog newInstance() {

        Bundle args = new Bundle();

        BillTemplateSheetDialog fragment = new BillTemplateSheetDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public SheetBillTemplateBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return null;
    }

    @Override
    public void onViewBinding(SheetBillTemplateBinding binding) {

    }
}
