package com.vividbobo.easy.ui.currency;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.adapter.CurrencyPickerAdapter;
import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.databinding.DialogCommonPickerBinding;
import com.vividbobo.easy.ui.common.BaseMaterialDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.viewmodel.CurrencyViewModel;

import java.util.List;

public class CurrencyPicker extends BaseMaterialDialog<DialogCommonPickerBinding> {
    public static final String TAG = "CurrencyPicker";
    private OnItemClickListener onItemClickListener;
    private CurrencyViewModel currencyViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currencyViewModel = new ViewModelProvider(getActivity()).get(CurrencyViewModel.class);
    }

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

        CurrencyPickerAdapter adapter = new CurrencyPickerAdapter(getContext());
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, item, position);
                }
                dismiss();
            }
        });

        binding.contentRecyclerView.setAdapter(adapter);

        currencyViewModel.getEnableCurrencies().observe(getActivity(), new Observer<List<Currency>>() {
            @Override
            public void onChanged(List<Currency> currencies) {
                adapter.updateItems(currencies);
            }
        });
    }


}
