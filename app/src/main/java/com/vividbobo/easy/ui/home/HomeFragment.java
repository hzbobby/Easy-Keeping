package com.vividbobo.easy.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vividbobo.easy.databinding.FragmentHomeBinding;
import com.vividbobo.easy.ui.bill.BillActivity;
import com.vividbobo.easy.ui.leger.LegerActivity;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;

    private View.OnClickListener toolbarNavigationClickListener;

    public static HomeFragment newInstance(View.OnClickListener toolbarNavigationClickListener) {
        HomeFragment fragment = new HomeFragment();
        fragment.setToolbarNavigationClickListener(toolbarNavigationClickListener);
        Bundle args = new Bundle();
        fragment.setArguments(args);
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.homeRecyclerView.setLayoutManager(linearLayoutManager);
        HomeBillAdapter homeBillAdapter = new HomeBillAdapter();
        homeBillAdapter.setOpenFooter(true);
        homeBillAdapter.setOpenHeader(true);
        homeBillAdapter.setDataToShowLimit(true);
        homeBillAdapter.setMax2show(50);
        binding.homeRecyclerView.setAdapter(homeBillAdapter);


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

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}