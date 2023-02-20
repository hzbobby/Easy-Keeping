package com.vividbobo.easy.ui.bill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vividbobo.easy.BaseActivity;
import com.vividbobo.easy.R;
import com.vividbobo.easy.databinding.ActivityBillBinding;

import java.util.ArrayList;
import java.util.List;

public class BillActivity extends BaseActivity {
    private ActivityBillBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //toolbar
        binding.billToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.billToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.bill_template:
                        return true;
                    default:
                        return false;
                }
            }
        });

        //set view pager adapter
        BillFragmentAdapter fragmentAdapter = new BillFragmentAdapter(this);
        binding.billViewPager.setAdapter(fragmentAdapter);


        //viewpager bind with tab layout
        new TabLayoutMediator(binding.billTabLayout, binding.billViewPager, true, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(fragmentAdapter.tabTitles.get(position));

            }
        }).attach();
    }


    public class BillFragmentAdapter extends FragmentStateAdapter {
        private List<String> tabTitles = new ArrayList<>();

        public BillFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);

            tabTitles.add("支出");
            tabTitles.add("收入");
            tabTitles.add("转账");
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            //depend on position create fragment
            //0-outcome; 1-income; 2-transfer
            //tow func: add,edit
            switch (position) {
                case 0:
                    return KindOfBillFragment.newInstance(KindOfBillFragment.KIND_OUTCOME, null);
                case 1:
                    return KindOfBillFragment.newInstance(KindOfBillFragment.KIND_INCOME, null);
                default:
                    return KindOfBillFragment.newInstance(KindOfBillFragment.KIND_TRANSFER, null);
            }
        }

        @Override
        public int getItemCount() {
            return tabTitles.size();
        }
    }
}