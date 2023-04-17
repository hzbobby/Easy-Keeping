package com.vividbobo.easy.ui.account;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.vividbobo.easy.R;
import com.vividbobo.easy.databinding.ActivityAddAccountBinding;
import com.vividbobo.easy.database.model.AccountTypeItem;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;
import com.vividbobo.easy.ui.others.OperationDialogBuilder;

public class AddAccountActivity extends AppCompatActivity {
    private static final String TAG = "AddAccountActivity";

    ActivityAddAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.addAccountToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.addAccountToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add_account_type) {
                    AddAccountTypeDialog.newInstance(null).show(getSupportFragmentManager(), AddAccountTypeDialog.TAG);
                    return true;
                }
                return false;
            }
        });


        AccountTypeAdapter accountTypeAdapter = new AccountTypeAdapter();
        accountTypeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                AccountTypeItem accountTypeItem = (AccountTypeItem) item;
                Log.d(TAG, "onItemClick: isChildAccountEmpty: " + accountTypeItem.isChildAccountEmpty());
                if (accountTypeItem.isChildAccountEmpty()) {
                    AddAccountDialog.newInstance(null).show(getSupportFragmentManager(), AddAccountDialog.TAG);
                } else {
                    //into second type select dialog
                    AccountBankDialog.newInstance().show(getSupportFragmentManager(), AccountBankDialog.TAG);
                }
            }
        });



        accountTypeAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Object item, int position) {
//                operationDialog.show();
            }
        });


        binding.addAccountTypeRecyclerView.setAdapter(accountTypeAdapter);


    }
}