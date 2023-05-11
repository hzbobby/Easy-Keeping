package com.vividbobo.easy.ui.bill;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.databinding.DialogFullRemarkBinding;
import com.vividbobo.easy.ui.common.BottomSheetDialog;
import com.vividbobo.easy.viewmodel.BillViewModel;

public class RemarkSheetDialog extends BottomSheetDialog<DialogFullRemarkBinding> {
    public static final String TAG = "RemarkSheetDialog";

    private BillViewModel billViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        billViewModel = new ViewModelProvider(getActivity()).get(BillViewModel.class);
    }

    public static RemarkSheetDialog newInstance() {

        Bundle args = new Bundle();

        RemarkSheetDialog fragment = new RemarkSheetDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public DialogFullRemarkBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogFullRemarkBinding.inflate(inflater);
    }

    @Override
    public void onViewBinding(DialogFullRemarkBinding binding) {

        binding.dialogFullExpendLessIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.dialogFullClearTextIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.dialogFullEditText.getEditableText().clear();
            }
        });

        binding.dialogFullEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.dialogFullClearTextIv.setVisibility(View.VISIBLE);
                } else {
                    binding.dialogFullClearTextIv.setVisibility(View.GONE);
                }
            }
        });
        binding.dialogFullEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    billViewModel.setRemark(binding.dialogFullEditText.getText().toString());
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        billViewModel.getInputRemark().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.dialogFullEditText.setText(s);
            }
        });

    }

    @Override
    protected int getInitialPeekHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }
}
