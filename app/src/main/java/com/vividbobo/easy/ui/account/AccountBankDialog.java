package com.vividbobo.easy.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.AccountTypeAdapter;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.AccountType;
import com.vividbobo.easy.databinding.DialogAccountBankBinding;
import com.vividbobo.easy.ui.others.FullScreenDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class AccountBankDialog extends FullScreenDialog {
    public static final String TAG = "AccountBankDialog";

    private DialogAccountBankBinding binding;
    private AccountTypeAdapter adapter;

    public static AccountBankDialog newInstance(ArrayList<AccountType> accountTypes) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("data", accountTypes);

        AccountBankDialog fragment = new AccountBankDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAccountBankBinding.inflate(getLayoutInflater());

        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        binding.toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        List<AccountType> accountTypes = getArguments().getParcelableArrayList("data");

        adapter = new AccountTypeAdapter(getContext());
        adapter.updateItems(accountTypes);


        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                AddAccountDialog.newInstance((Account) item).show(getParentFragmentManager(), AccountBankDialog.TAG);
            }
        });
        binding.bankTypeRecyclerView.setAdapter(adapter);

        initSearchView();

        return binding.getRoot();
    }

    private void initSearchView() {
        SearchView searchView = (SearchView) binding.toolBar.getMenu().findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //搜索框确认时搜索
                //data change
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //搜索框文字改变时进行搜索

                return false;
            }
        });
    }


}
