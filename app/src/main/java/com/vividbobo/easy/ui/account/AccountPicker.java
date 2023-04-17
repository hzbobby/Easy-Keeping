package com.vividbobo.easy.ui.account;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vividbobo.easy.R;
import com.vividbobo.easy.databinding.DialogCommonPickerBinding;
import com.vividbobo.easy.database.model.AccountItem;
import com.vividbobo.easy.ui.common.BaseMaterialDialog;
import com.vividbobo.easy.ui.common.BaseDialogPickerAdapter;
import com.vividbobo.easy.ui.common.CommonFooterViewHolder;
import com.vividbobo.easy.ui.common.CommonItemViewHolder;
import com.vividbobo.easy.ui.others.OnItemClickListener;

public class AccountPicker extends BaseMaterialDialog<DialogCommonPickerBinding> {
    public static final String TAG = "AccountPicker2";

    @Override
    protected DialogCommonPickerBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogCommonPickerBinding.inflate(inflater);
    }

    @Override
    protected void onBuildDialog(MaterialAlertDialogBuilder builder) {
        builder.setTitle(R.string.pick_account)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
    }

    @Override
    protected void onBindView(DialogCommonPickerBinding binding) {
        binding.descriptionContentTv.setText(R.string.pick_account_desc);
        AccountPickerAdapter adapter = new AccountPickerAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {

            }
        });
        binding.contentRecyclerView.setAdapter(adapter);
    }

    private class AccountPickerAdapter
            extends BaseDialogPickerAdapter<AccountItem, AccountPickerAdapter.AccountViewHolder, RecyclerView.ViewHolder, CommonFooterViewHolder> {


        public AccountPickerAdapter() {
            super();
            setEnableFooter(true);
            for (int i = 0; i < 15; i++) {
                items.add(new AccountItem(String.format("账户%d", i), "余额"));
            }
        }

        @Override
        protected AccountPickerAdapter.AccountViewHolder getNormalViewHolder(View view) {
            return new AccountViewHolder(view);
        }

        @Override
        protected CommonFooterViewHolder getFooterViewHolder(View view) {
            return new CommonFooterViewHolder(view);
        }


        @Override
        protected void onBindNormalViewHolder(@NonNull AccountPickerAdapter.AccountViewHolder holder, int position) {
            AccountItem accountItem = getItem(getItemPosition(position));
            holder.bind(accountItem);
        }

        @Override
        protected void onBindFooterViewHolder(@NonNull CommonFooterViewHolder holder, int position) {
            Log.d(TAG, "onBindFooterViewHolder: ");
            holder.setTitle(R.string.add_account);
        }

        public class AccountViewHolder extends CommonItemViewHolder<AccountItem> {

            public AccountViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            @Override
            public void bind(AccountItem item) {
                title.setText(item.getTitle());
                desc.setText(item.getDesc());
//                icon.setImageDrawable();
            }
        }
    }

}
