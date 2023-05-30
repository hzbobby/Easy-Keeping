package com.vividbobo.easy.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.adapter.ExpandableAdapter;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.AccountsInfo;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.databinding.FragmentAccountBinding;
import com.vividbobo.easy.ui.common.ContextOperationMenuDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;
import com.vividbobo.easy.utils.GlideUtils;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.viewmodel.AccountViewModel;
import com.vividbobo.easy.viewmodel.ConfigViewModel;

import java.util.List;


public class AccountFragment extends Fragment {
    private static final String TAG = "AccountFragment";

    private FragmentAccountBinding binding;
    private ConfigViewModel configViewModel;


    private View.OnClickListener toolbarNavigationClickListener;
    private int lastClickGroupPos = -1;
    private int lastClickChildPos = -1;

    private ContextOperationMenuDialog<Account> operationDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configViewModel = new ViewModelProvider(getActivity()).get(ConfigViewModel.class);
    }

    public static AccountFragment newInstance(View.OnClickListener toolbarNavigationClickListener) {
        AccountFragment fragment = new AccountFragment();
        fragment.setToolbarNavigationClickListener(toolbarNavigationClickListener);

        return fragment;
    }

    public void setToolbarNavigationClickListener(View.OnClickListener toolbarNavigationClickListener) {
        this.toolbarNavigationClickListener = toolbarNavigationClickListener;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);
        operationDialog = new ContextOperationMenuDialog<>();
        operationDialog.setMenuItemVisible(ContextOperationMenuDialog.MENU_ITEM_DETAIL,View.GONE);
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        binding.accountToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toolbarNavigationClickListener != null) {
                    toolbarNavigationClickListener.onClick(null);
                }
            }
        });
        binding.accountToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.account_add:
                        Intent intent = new Intent(getActivity(), AccountAddActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        //折叠item Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ExpandableAdapter<Account> expandableAdapter = new ExpandableAdapter<Account>(getContext(), linearLayoutManager, R.layout.item_expandable_child_list);
        expandableAdapter.setEnableChildIcon(true);
        expandableAdapter.setEnableParentIcon(true);
        binding.accountExpandableRv.setAdapter(expandableAdapter);


        expandableAdapter.setOnChildItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
            }
        });
        expandableAdapter.setOnChildItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Object item, int position) {
                operationDialog.show(getParentFragmentManager(), (Account) item);
            }
        });

        accountViewModel.getExpandableAccountsLD().observe(getActivity(), new Observer<List<ExpandableAdapter.ExpandableItem<Account>>>() {
            @Override
            public void onChanged(List<ExpandableAdapter.ExpandableItem<Account>> expandableItems) {
                Log.d(TAG, "onChanged: expandable items size: " + expandableItems.size());
                expandableAdapter.updateItems(expandableItems);
            }
        });
        accountViewModel.getAccountInfoLD().observe(getActivity(), new Observer<AccountsInfo>() {
            @Override
            public void onChanged(AccountsInfo accountsInfo) {
                Log.d(TAG, "onChanged: header accounts info");

                setAccountsInfo(accountsInfo);
            }
        });
        /*********** operation dialog ***********/

        operationDialog.setOnEditClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Account>() {
            @Override
            public void onMenuItemClick(Account item) {
                Intent intent = new Intent(getActivity(), AccountAddActivity.class);
                intent.putExtra(AccountAddActivity.KEY_EDIT_ACCOUNT, item);
                startActivity(intent);
            }
        });
        operationDialog.setOnDeleteClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Account>() {
            @Override
            public void onMenuItemClick(Account item) {
                accountViewModel.delete(item);
            }
        });


        /********** header cover **********/
        configViewModel.getSelectedLeger().observe(getActivity(), new Observer<Leger>() {
            @Override
            public void onChanged(Leger leger) {
                if (leger == null) return;
                GlideUtils.bindLegerCover(getActivity(), leger.getItemIconResName(), leger.getCoverType())
                        .centerCrop().into(binding.accountHeaderCoverIv);
            }
        });

        return binding.getRoot();
    }

    private void setAccountsInfo(AccountsInfo accountsInfo) {
        binding.accountNetAssetsText.setText(String.format("%.2f", (double) accountsInfo.getNetAssert() / 100));
        binding.accountTotalAssetsText.setText(String.format("%.2f", (double) accountsInfo.getTotalAssert() / 100));
        binding.accountTotalLiabilityText.setText(String.format("%.2f", (double) accountsInfo.getTotalDebt() / 100));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}