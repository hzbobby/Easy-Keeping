package com.vividbobo.easy.ui.currency;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.adapter.CurrenciesAdapter;
import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.databinding.DialogCurrenciesBinding;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;
import com.vividbobo.easy.ui.others.NotifyItemChange;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.viewmodel.CurrencyViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 异步更新策略
 * list: repo层里用 MutableLiveData<ListenableFuture<List<Currency>>>，这里不能去掉ListenableFuture，
 *      否则会出现在主线程访问database.
 *      list 只在需要时更新，而不需要LiveData 进行时刻观察，避免update某条数据时，也会notify整个列表
 *
 * 某个Item Enable CheckedChange: 监听switch的checked变化
 *
 * 点击进入修改，是否自动更新：这里重新写了接口，进行回调adapter.notifyItemChange，并且创建dialog时，传入currency和
 *      click position.
 *
 * header 选择后，进行更新rate，同时写入数据库
 *      首次进入时，没有进行获取rate，
 */
public class CurrencyFullDialog extends BaseFullScreenMaterialDialog<DialogCurrenciesBinding> implements NotifyItemChange {
    public static final String TAG = "CurrencyFullDialog";
    private CurrencyViewModel currencyViewModel;
    private CurrenciesAdapter currenciesAdapter;


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
    }

    @Override
    protected DialogCurrenciesBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogCurrenciesBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(DialogCurrenciesBinding binding) {
        binding.appBarLayout.layoutToolBarTitleTv.setText(R.string.currency_manage);
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        currenciesAdapter = new CurrenciesAdapter(getContext());

        /**************** the picker **************************/
        CurrencyPicker currencyPicker = new CurrencyPicker();
        currencyPicker.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                //update rate
                Currency currency = (Currency) item;
                //set header
                currencyViewModel.setBaseCurrencyCode(currency.getCode());
                currencyViewModel.updateRate(currency.getCode());
            }
        });
        currenciesAdapter.setOnHeaderClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                //choose other currency as base currency
                currencyPicker.show(getParentFragmentManager(), CurrencyPicker.TAG);
            }
        });

        currenciesAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                //edit
                CurrencyModifyFullDialog dialog = CurrencyModifyFullDialog.newInstance((Currency) item);
                dialog.setPosition(position);
                dialog.setNotifyItemChange(CurrencyFullDialog.this);
                dialog.show(getParentFragmentManager(), CurrencyModifyFullDialog.TAG);

            }
        });
        currenciesAdapter.setOnItemCheckedChangeListener(new CurrenciesAdapter.OnItemCheckedChangeListener() {
            @Override
            public void onItemCheckedChange(CompoundButton buttonView, Object item, boolean isCheck) {
                Currency currency = (Currency) item;
                currencyViewModel.updateEnable(currency.getCode(), isCheck);
            }
        });

        binding.currencyRv.setAdapter(currenciesAdapter);

        currencyViewModel.getBaseCurrency().observe(getActivity(), new Observer<Currency>() {
            @Override
            public void onChanged(Currency currency) {
                currenciesAdapter.setHeaderItem(currency);
            }
        });

        //更新列表
        currencyViewModel.getListenableFutureOfCurrencies().observe(getActivity(), new Observer<ListenableFuture<List<Currency>>>() {
            @Override
            public void onChanged(ListenableFuture<List<Currency>> listListenableFuture) {
                initialDataList(listListenableFuture);
            }
        });

        // observe the rate map live data
//        currencyViewModel.getRateMap().observe(getActivity(), new Observer<Map<String, Float>>() {
//            @Override
//            public void onChanged(Map<String, Float> stringFloatMap) {
//                //update adapter rateMap
//                Log.d(TAG, "onChanged: stringFloatMap is null? " + (stringFloatMap == null));
//                currenciesAdapter.updateRateMap(stringFloatMap);
//            }
//        });

    }

    private void initialDataList(ListenableFuture<List<Currency>> listListenableFuture) {
        try {
            currenciesAdapter.updateItems(listListenableFuture.get());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        // update database
        Log.d(TAG, "onDismiss: ");
//        currencyViewModel.update(currenciesAdapter.getNeedUpdateCurrencyList());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }


    @Override
    public void notifyItemChange(int position) {
        Log.d(TAG, "notifyItemChange: position: " + position);
        currenciesAdapter.notifyItemChanged(position);
    }
}
