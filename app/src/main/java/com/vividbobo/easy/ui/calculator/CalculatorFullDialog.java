package com.vividbobo.easy.ui.calculator;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.vividbobo.easy.databinding.DialogCalculateBinding;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;

public class CalculatorFullDialog extends BaseFullScreenMaterialDialog<DialogCalculateBinding> {
    @Override
    protected DialogCalculateBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return null;
    }

    @Override
    protected void onViewBinding(DialogCalculateBinding binding) {

    }
}
