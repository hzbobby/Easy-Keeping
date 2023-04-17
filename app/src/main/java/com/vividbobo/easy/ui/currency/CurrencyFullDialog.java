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
import com.vividbobo.easy.ui.common.ContextOperationMenuDialog;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;
import com.vividbobo.easy.viewmodel.CurrencyViewModel;

import java.util.List;

public class CurrencyFullDialog extends BaseEntityFullDialog<Currency> {
    public static final String TAG = "CurrencyFullDialog";
    private CurrencyViewModel currencyViewModel;
    private ContextOperationMenuDialog<Currency> operationDialog;

    public static CurrencyFullDialog newInstance() {
        Bundle args = new Bundle();
        CurrencyFullDialog fragment = new CurrencyFullDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currencyViewModel = new ViewModelProvider(getActivity()).get(CurrencyViewModel.class);
        operationDialog = new ContextOperationMenuDialog<>();
        //币种不需要编辑
        operationDialog.setMenuItemVisible(ContextOperationMenuDialog.MENU_ITEM_EDIT, View.GONE);
    }

    @Override
    public void menuAddClick() {
        CurrencySelectedFullDialog.newInstance().show(getParentFragmentManager(), CurrencySelectedFullDialog.TAG);
    }

    @Override
    public void config(DialogBaseEntityBinding binding, BaseEntityAdapter<Currency> adapter) {
        binding.appBarLayout.layoutToolBarTitleTv.setText("币种管理");
        //get selected currency
        currencyViewModel.getSelectedCurrencies().observe(getViewLifecycleOwner(), new Observer<List<Currency>>() {
            @Override
            public void onChanged(List<Currency> currencies) {
                adapter.updateItems(currencies);
            }
        });
        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Object item, int position) {
                operationDialog.show(getParentFragmentManager(), (Currency) item);
            }
        });
        operationDialog.setOnDeleteClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Currency>() {
            @Override
            public void onMenuItemClick(Currency item) {
                currencyViewModel.delete(item);
            }
        });
        operationDialog.setOnDetailClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Currency>() {
            @Override
            public void onMenuItemClick(Currency item) {

            }
        });

    }
}
