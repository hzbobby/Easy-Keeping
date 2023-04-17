package com.vividbobo.easy.ui.currency;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.BaseEntityAdapter;
import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.databinding.DialogCommonPickerBinding;
import com.vividbobo.easy.database.model.CurrencyItem;
import com.vividbobo.easy.ui.common.BaseDialogPickerAdapter;
import com.vividbobo.easy.ui.common.BaseMaterialDialog;
import com.vividbobo.easy.ui.common.CommonFooterViewHolder;
import com.vividbobo.easy.ui.common.CommonItemViewHolder;
import com.vividbobo.easy.ui.others.OnItemClickListener;

public class CurrencyPicker extends BaseMaterialDialog<DialogCommonPickerBinding> {
    public static final String TAG = "CurrencyPicker";
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected DialogCommonPickerBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogCommonPickerBinding.inflate(inflater);
    }

    @Override
    protected void onBuildDialog(MaterialAlertDialogBuilder builder) {
        builder.setTitle(R.string.pick_currency)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });

    }

    @Override
    protected void onBindView(DialogCommonPickerBinding binding) {
        binding.descriptionContentTv.setText(R.string.pick_currency_desc);
        BaseEntityAdapter<Currency> currencyAdapter = new BaseEntityAdapter<>(getActivity());
        currencyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, item, position);
                }
            }
        });
        binding.contentRecyclerView.setAdapter(currencyAdapter);
    }


}
