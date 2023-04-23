package com.vividbobo.easy.ui.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.adapter.ExpandableAdapter;
import com.vividbobo.easy.adapter.adapter.ItemAdapter;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.databinding.DialogCommonPickerBinding;
import com.vividbobo.easy.ui.common.BaseMaterialDialog;
import com.vividbobo.easy.ui.common.BaseDialogPickerAdapter;
import com.vividbobo.easy.ui.common.CommonFooterViewHolder;
import com.vividbobo.easy.ui.common.CommonItemViewHolder;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.viewmodel.AccountViewModel;

import java.util.List;

public class AccountPicker extends BaseMaterialDialog<DialogCommonPickerBinding> {
    public static final String TAG = "AccountPicker2";
    private AccountViewModel accountViewModel;
    private OnItemClickListener onItemClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountViewModel = new ViewModelProvider(getActivity()).get(AccountViewModel.class);
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
        builder.setTitle(R.string.pick_account).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
    }

    @Override
    protected void onBindView(DialogCommonPickerBinding binding) {
        binding.descriptionContentTv.setText(R.string.pick_account_desc);

        ItemAdapter<Account> accountItemAdapter = new ItemAdapter<Account>(getContext());
        accountItemAdapter.setEnableIcon(true);
        accountItemAdapter.setEnableFooter(true);
        accountItemAdapter.setFooterItem("添加账户");
        accountItemAdapter.setOnItemClickListener(onItemClickListener);
        accountItemAdapter.setOnFooterClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Intent intent = new Intent(getActivity(), AccountAddActivity.class);
                startActivity(intent);
            }
        });
        binding.contentRecyclerView.setAdapter(accountItemAdapter);

        accountViewModel.getAllAccountsLD().observe(getActivity(), new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accountList) {
                accountItemAdapter.updateItems(accountList);
            }
        });
    }

}
