package com.vividbobo.easy.ui.bill;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.adapter.adapter.DropdownMenuAdapter;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.databinding.FragmentTransferBinding;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.utils.ToastUtil;
import com.vividbobo.easy.viewmodel.AccountViewModel;
import com.vividbobo.easy.viewmodel.BillViewModel;

import java.util.List;
import java.util.Objects;

public class TransferFragment extends Fragment {
    private static final String TAG = "TransferFragment";
    private FragmentTransferBinding binding;

    private AccountViewModel accountViewModel;
    private BillViewModel billViewModel;

    public static TransferFragment newInstance() {
        Bundle args = new Bundle();

        TransferFragment fragment = new TransferFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountViewModel = new ViewModelProvider(getActivity()).get(AccountViewModel.class);
        billViewModel = new ViewModelProvider(getActivity()).get(BillViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTransferBinding.inflate(getLayoutInflater());


        DropdownMenuAdapter<Account> srcAdapter = new DropdownMenuAdapter<Account>(getActivity());
        srcAdapter.setEnableIcon(true);
        srcAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Account account = (Account) item;
                setSrcAccount(account);
            }
        });
        binding.transferSrcAccountAtv.setAdapter(srcAdapter);

        DropdownMenuAdapter<Account> tarAdapter = new DropdownMenuAdapter<Account>(getActivity());
        tarAdapter.setEnableIcon(true);
        tarAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Account account = (Account) item;
                setTarAccount(account);
            }
        });
        binding.transferTarAccountAtv.setAdapter(tarAdapter);
        //swap icon click
        binding.transferExchangeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //交换账户
                if (Objects.nonNull(billViewModel.getBillAmount()) && Objects.nonNull(billViewModel.getTarAccount())) {
                    Account srcAccount = billViewModel.getSrcAccount();
                    Account tarAccount = billViewModel.getTarAccount();
                    setSrcAccount(tarAccount);
                    setTarAccount(srcAccount);
                } else {
                    ToastUtil.makeToast("请选择账户");
                }
            }
        });

        //observe
        accountViewModel.getAllAccountsLD().observe(getActivity(), new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accountList) {
                srcAdapter.setItems(accountList);
                tarAdapter.setItems(accountList);
            }
        });


        return binding.getRoot();
    }

    private void setTarAccount(Account account) {
        billViewModel.setTarAccount(account);
        binding.transferTarAccountTil.getEditText().setText(account.getTitle());
        binding.transferTarAccountTil.setStartIconDrawable(ResourceUtils.getDrawable(account.getIconResName()));
    }

    private void setSrcAccount(Account account) {
        billViewModel.setSrcAccount(account);
        binding.transferSrcAccountTil.getEditText().setText(account.getTitle());
        binding.transferSrcAccountTil.setStartIconDrawable(ResourceUtils.getDrawable(account.getIconResName()));
    }
}
