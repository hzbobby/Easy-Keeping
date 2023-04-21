package com.vividbobo.easy.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.ExpandableAdapter;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.databinding.FragmentAccountBinding;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;
import com.vividbobo.easy.viewmodel.AccountViewModel;

import java.util.List;


public class AccountFragment extends Fragment {
    private static final String TAG = "AccountFragment";

    private FragmentAccountBinding binding;

    private View.OnClickListener toolbarNavigationClickListener;
    private int lastClickGroupPos = -1;
    private int lastClickChildPos = -1;

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
                    case R.id.account_more:
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


        expandableAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
            }
        });
        expandableAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Object item, int position) {

            }
        });

        accountViewModel.getAllAccountsLD().observe(getActivity(), new Observer<List<ExpandableAdapter.ExpandableItem<Account>>>() {
            @Override
            public void onChanged(List<ExpandableAdapter.ExpandableItem<Account>> expandableItems) {
                expandableAdapter.updateItems(expandableItems);
            }
        });


        return binding.getRoot();
    }

    private void setHideToolBarAssets(boolean hideToolBarAssets) {
        if (hideToolBarAssets) {
            binding.accountNetAssetsText.setText("****");
            binding.accountTotalAssetsText.setText("****");
            binding.accountTotalLiabilityText.setText("****");
        } else {
            //TODO change to detail assets
            binding.accountNetAssetsText.setText("1111");
            binding.accountTotalAssetsText.setText("1111");
            binding.accountTotalLiabilityText.setText("1111");
        }
    }

    private void setLastClickPos(int groupPos, int childPos) {
        lastClickGroupPos = groupPos;
        lastClickChildPos = childPos;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}