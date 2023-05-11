package com.vividbobo.easy.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.adapter.BillAdapter;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.BillInfo;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.databinding.FragmentHomeBinding;
import com.vividbobo.easy.ui.bill.BillActivity;
import com.vividbobo.easy.ui.bill.BillDetailBottomSheet;
import com.vividbobo.easy.ui.calendar.CalendarActivity;
import com.vividbobo.easy.ui.leger.LegerActivity;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.FormatUtils;
import com.vividbobo.easy.utils.GlideUtils;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.viewmodel.ConfigViewModel;
import com.vividbobo.easy.viewmodel.HomeViewModel;

import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;

    private View.OnClickListener toolbarNavigationClickListener;
    private HomeViewModel homeViewModel;

    private ConfigViewModel configViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        configViewModel = new ViewModelProvider(getActivity()).get(ConfigViewModel.class);

    }

    public static HomeFragment newInstance(View.OnClickListener toolbarNavigationClickListener) {
        HomeFragment fragment = new HomeFragment();
        fragment.setToolbarNavigationClickListener(toolbarNavigationClickListener);

        return fragment;
    }

    public void setToolbarNavigationClickListener(View.OnClickListener toolbarNavigationClickListener) {
        this.toolbarNavigationClickListener = toolbarNavigationClickListener;
    }

    //activity launcher for result
    ActivityResultLauncher<Intent> handleIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

        }
    });


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //toolbar nav icon click
        binding.homeToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                binding.homeDrawerLayout.open();
                if (toolbarNavigationClickListener != null) {
                    toolbarNavigationClickListener.onClick(null);
                }
            }
        });
        binding.homeToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_voice -> { //;
                        TextIdentifyBottomSheet.newInstance().show(getParentFragmentManager(), TextIdentifyBottomSheet.TAG);
                        return true;
                    }
                    case R.id.wechat_screenshot_import -> {
                        Intent intent = new Intent(getActivity(), ScreenshotImportActivity.class);
                        intent.putExtra("analyzer", ScreenshotImportActivity.ANALYZER_WECHAT);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.alipay_screenshot_import -> {
                        Intent intent = new Intent(getActivity(), ScreenshotImportActivity.class);
                        intent.putExtra("analyzer", ScreenshotImportActivity.ANALYZER_ALIPAY);
                        startActivity(intent);
                        return true;
                    }
                    default -> {
                        return false;
                    }
                }
            }
        });

        BillAdapter billAdapter = new BillAdapter(getActivity());
        billAdapter.setOnHeaderClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });
        billAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Bill bill = (Bill) item;
                BillDetailBottomSheet bottomSheet = BillDetailBottomSheet.newInstance(bill);
                bottomSheet.setOnRefundClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //退款
                        bill.setRefund(true);
                        //dismiss?
                        homeViewModel.updateBill(bill);
                        bottomSheet.dismiss();
                    }
                });
                bottomSheet.setOnDeleteClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        homeViewModel.deleteBill(bill);
                        bottomSheet.dismiss();
                    }
                });
                bottomSheet.setOnEditClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), BillActivity.class);
                        intent.putExtra(BillActivity.KEY_DATA_BILL, bill);
                        startActivity(intent);
                    }
                });
                bottomSheet.show(getParentFragmentManager(), BillDetailBottomSheet.TAG);
            }
        });
        binding.homeRecyclerView.setAdapter(billAdapter);

        //add bill
        binding.homeAddBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BillActivity.class);
                startActivity(intent);
            }
        });

        //leger layout
        binding.homeToolBarLegerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleIntent.launch(new Intent(getActivity(), LegerActivity.class));
            }
        });


        //month info
        homeViewModel.getMonthBillInfo().observe(getActivity(), new Observer<BillInfo>() {
            @Override
            public void onChanged(BillInfo billInfo) {
                Log.d(TAG, "onChanged: bill info is null?" + Objects.isNull(billInfo));
                if (billInfo != null) {
                    Log.d(TAG, "onChanged: bill income amount:" + billInfo.getIncomeAmount());
                    binding.homeToolBarIncomeText.setText(FormatUtils.getAmount(billInfo.getIncomeAmount()));
                    binding.homeToolBarOutcomeText.setText(FormatUtils.getAmount(billInfo.getExpenditureAmount()));
                    binding.homeToolBarLeftBalanceText.setText(FormatUtils.getAmount(billInfo.getBalanceAmount()));
                }
            }
        });

        homeViewModel.getTodayBillInfo().observe(getActivity(), new Observer<BillInfo>() {
            @Override
            public void onChanged(BillInfo billInfo) {
                billAdapter.setHeaderItem(billInfo);
            }
        });
        homeViewModel.getTodayBills().observe(getActivity(), new Observer<List<Bill>>() {
            @Override
            public void onChanged(List<Bill> billPresents) {
                Log.d(TAG, "onChanged: billPresents size: " + billPresents.size());
                billAdapter.updateItems(billPresents);
            }
        });

        // observe selected leger
        configViewModel.getSelectedLeger().observe(getActivity(), new Observer<Leger>() {
            @Override
            public void onChanged(Leger leger) {
                if (leger == null) return;

                binding.homeToolBarTitle.setText(leger.getTitle());
                GlideUtils.bindLegerCover(getActivity(), leger.getItemIconResName(), leger.getCoverType())
                        .centerCrop().into(binding.homeHeaderCoverIv);
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}