package com.vividbobo.easy.ui.bill;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.vividbobo.easy.databinding.DialogFullRemarkBinding;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;

public class RemarkFullDialog extends BaseFullScreenMaterialDialog<DialogFullRemarkBinding> {
    public static final String TAG = "RemarkFullDialog";

    public static RemarkFullDialog newInstance() {
        Bundle args = new Bundle();

        RemarkFullDialog fragment = new RemarkFullDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected DialogFullRemarkBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogFullRemarkBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(DialogFullRemarkBinding binding) {
        binding.dialogFullExpendLessIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
