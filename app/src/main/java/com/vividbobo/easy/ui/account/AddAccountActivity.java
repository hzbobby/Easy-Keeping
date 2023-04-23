package com.vividbobo.easy.ui.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.adapter.AccountTypeAdapter;
import com.vividbobo.easy.databinding.DialogAddAccount2Binding;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;

@Deprecated
public class AddAccountActivity extends AppCompatActivity {
    private static final String TAG = "AddAccountActivity";

    DialogAddAccount2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogAddAccount2Binding.inflate(getLayoutInflater());
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


        AccountTypeAdapter accountTypeAdapter = new AccountTypeAdapter(this);
        accountTypeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
//                AccountType accountType = (AccountType) item;
//                AddAccountDialog.newInstance(null).show(getSupportFragmentManager(), AddAccountDialog.TAG);
//

//                if (accountType.getChildren().size() > 0) {
//                    //into second type select dialog
//                    AccountBankDialog.newInstance((ArrayList<AccountType>) accountType.getChildren()).show(getSupportFragmentManager(), AccountBankDialog.TAG);
//                } else {
//                    AddAccountDialog.newInstance(null).show(getSupportFragmentManager(), AddAccountDialog.TAG);
//                }
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