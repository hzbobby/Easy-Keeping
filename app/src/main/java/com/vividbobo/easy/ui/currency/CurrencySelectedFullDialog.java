package com.vividbobo.easy.ui.currency;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.adapter.BaseEntityAdapter;
import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.databinding.DialogBaseEntityBinding;
import com.vividbobo.easy.ui.common.BaseEntityFullDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.viewmodel.CurrencyViewModel;

import java.util.List;

public class CurrencySelectedFullDialog extends BaseEntityFullDialog<Currency> {
    public static final String TAG = "CurrencyAddFullDialog";
    private CurrencyViewModel currencyViewModel;

    public static CurrencySelectedFullDialog newInstance() {
        Bundle args = new Bundle();
        CurrencySelectedFullDialog fragment = new CurrencySelectedFullDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currencyViewModel = new ViewModelProvider(getActivity()).get(CurrencyViewModel.class);
    }

    @Override
    public void menuAddClick() {
//        CurrencyAddFullDialog
    }

    @Override
    public void config(DialogBaseEntityBinding binding, BaseEntityAdapter<Currency> adapter) {

        //get all currencies
        currencyViewModel.getAllCurrencies().observe(getViewLifecycleOwner(), new Observer<List<Currency>>() {
            @Override
            public void onChanged(List<Currency> currencies) {
                adapter.updateItems(currencies);
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                //set it selected
                currencyViewModel.add((Currency) item);
                dismiss();
            }
        });
    }
}
