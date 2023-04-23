package com.vividbobo.easy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.vividbobo.easy.databinding.ActivityMainBinding;
import com.vividbobo.easy.ui.account.AccountFragment;
import com.vividbobo.easy.ui.chart.ChartFragment;
import com.vividbobo.easy.ui.home.HomeFragment;
import com.vividbobo.easy.ui.settings.SettingsActivity;
import com.vividbobo.easy.utils.SharePreferenceUtil;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        //using viewpage2
        ViewPager2 viewPager2 = binding.mainViewPager;
        //viewpager2's adapter
        View.OnClickListener onNavigationClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.mainDrawerLayout.open();
            }
        };
        FragmentStateAdapter fragmentStateAdapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return HomeFragment.newInstance(onNavigationClickListener);
                    case 1:
                        return AccountFragment.newInstance(onNavigationClickListener);
                    default:
                        return new ChartFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        };

        viewPager2.setAdapter(fragmentStateAdapter);

        // set change page listener
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                navView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        //bottomNav select listener
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        viewPager2.setCurrentItem(0, true);
                        break;
                    case R.id.navigation_account:
                        viewPager2.setCurrentItem(1, true);
                        break;
                    default:
                        viewPager2.setCurrentItem(2, true);
                }
                return true;
            }
        });

        //side nav


        binding.sideNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.settings_fragment:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        break;
                    case R.id.auto_record_item:
                        break;
                    case R.id.data_backup_recover:
                        break;
                    case R.id.data_import_export:
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });

    }


}