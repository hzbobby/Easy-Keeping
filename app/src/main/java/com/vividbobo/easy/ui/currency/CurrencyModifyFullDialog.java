package com.vividbobo.easy.ui.currency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.databinding.DialogCurrencyModifyBinding;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;
import com.vividbobo.easy.ui.others.NotifyItemChange;
import com.vividbobo.easy.utils.ConstantValue;
import com.vividbobo.easy.viewmodel.CurrencyViewModel;

public class CurrencyModifyFullDialog extends BaseFullScreenMaterialDialog<DialogCurrencyModifyBinding> {
    public static final String TAG = "CurrencyModifyFullDialog";
    private CurrencyViewModel currencyViewModel;
    private NotifyItemChange notifyItemChange;

    public void setNotifyItemChange(NotifyItemChange notifyItemChange) {
        this.notifyItemChange = notifyItemChange;
    }

    private int position;

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currencyViewModel = new ViewModelProvider(getActivity()).get(CurrencyViewModel.class);
    }


    public static CurrencyModifyFullDialog newInstance(Currency currency) {

        Bundle args = new Bundle();
        args.putSerializable(ConstantValue.KEY_DATA, currency);

        CurrencyModifyFullDialog fragment = new CurrencyModifyFullDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected DialogCurrencyModifyBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogCurrencyModifyBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(DialogCurrencyModifyBinding binding) {
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Currency currency = (Currency) getArguments().getSerializable(ConstantValue.KEY_DATA);
        binding.appBarLayout.layoutToolBarTitleTv.setText(String.format("%s - %s", currency.getTitle(), currency.getCode()));
        if (currency.isAutoUpdate()) {
            binding.rateTil.getEditText().setText(String.format("%.4f", currency.getRate()));
        } else {
            binding.rateTil.getEditText().setText(String.format("%.4f", currency.getLocalRate()));
        }

        binding.autoRateEnableSwitch.setChecked(currency.isAutoUpdate());
        binding.rateTil.setEnabled(!binding.autoRateEnableSwitch.isChecked());

        binding.autoRateEnableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.rateTil.setEnabled(!isChecked);
                if (isChecked) {
                    binding.rateTil.getEditText().setText(String.format("%.4f", currency.getRate()));
                } else {
                    binding.rateTil.getEditText().setText(String.format("%.4f", currency.getLocalRate()));
                }
            }
        });

        binding.currencyModifySaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currency.setAutoUpdate(binding.autoRateEnableSwitch.isChecked());
                if (!currency.isAutoUpdate()) {
                    currency.setLocalRate(Float.valueOf(binding.rateTil.getEditText().getText().toString()));
                }
                currencyViewModel.update(currency);
                if (notifyItemChange != null) {
                    notifyItemChange.notifyItemChange(position);
                }
                dismiss();
            }
        });

    }
}
