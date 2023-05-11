package com.vividbobo.easy.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.SettingItem;
import com.vividbobo.easy.databinding.ActivitySettingsBinding;
import com.vividbobo.easy.ui.category.CategorySettingsFullDialog;
import com.vividbobo.easy.ui.currency.CurrencyFullDialog;
import com.vividbobo.easy.ui.demo.OnFlingDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.role.RoleFullDialog;
import com.vividbobo.easy.ui.store.StoreFullDialog;
import com.vividbobo.easy.ui.tags.TagFullDialog;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements OnSettingItemClickListener {
    private static final String TAG = "SettingsActivity";
    private ActivitySettingsBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //toolbar
        binding.settingsToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //static setting item
        List<SettingItem> settingItems = new ArrayList<>();
        settingItems.add(new SettingItem("账本管理", "com.vividbobo.easy.ui.leger.LegerActivity"));
        settingItems.add(new SettingItem("标签管理", SettingItem.SETTING_TAG, this));

        Bundle expenseArgs = new Bundle();
        settingItems.add(new SettingItem("支出类型管理", SettingItem.SETTING_CATEGORY_EXPENDITURE, this));
        settingItems.add(new SettingItem("收入类型管理", SettingItem.SETTING_CATEGORY_INCOME, this));
//        settingItems.add(new SettingItem("onFling测试", SettingItem.SETTING_TESTING, this));
        settingItems.add(new SettingItem("角色管理", SettingItem.SETTING_ROLE, this));
        settingItems.add(new SettingItem("店家管理", SettingItem.SETTING_STORE, this));
        settingItems.add(new SettingItem("币种管理", SettingItem.SETTING_CURRENCY, this));


        //data recycler view
        binding.settingDataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SettingsAdapter dataAdapter = new SettingsAdapter();
        dataAdapter.setOpenHeader(true);
        dataAdapter.setHeaderTitle("全部");
        dataAdapter.setData(settingItems);
        dataAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                SettingItem settingItem = (SettingItem) item;

                if (settingItem.getTargetClass() == null || settingItem.getTargetClass().isEmpty()) {
                    settingItem.onSettingItemClick();
                } else {
                    //点击进入相应的activity
                    try {
                        Intent intent = new Intent(SettingsActivity.this, Class.forName(settingItem.getTargetClass()));
//                    Log.d(TAG, "onItemClick: setting item: "+settingItem.toString());s

                        intent.putExtra("args", settingItem.getArgs());
                        startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
        binding.settingDataRecyclerView.setAdapter(dataAdapter);


    }


    @Override
    public void OnClick(int settingId) {
        switch (settingId) {
            case SettingItem.SETTING_TAG:
                new TagFullDialog().show(getSupportFragmentManager(), OnFlingDialog.TAG);
                break;
            case SettingItem.SETTING_TESTING:
                new OnFlingDialog().show(getSupportFragmentManager(), OnFlingDialog.TAG);
                break;
            case SettingItem.SETTING_CATEGORY_EXPENDITURE:
                CategorySettingsFullDialog.newInstance(Category.TYPE_EXPENDITURE).show(getSupportFragmentManager(), CategorySettingsFullDialog.TAG);
                break;
            case SettingItem.SETTING_CATEGORY_INCOME:
                CategorySettingsFullDialog.newInstance(Category.TYPE_INCOME).show(getSupportFragmentManager(), CategorySettingsFullDialog.TAG);
                break;
            case SettingItem.SETTING_ROLE:
                RoleFullDialog.newInstance().show(getSupportFragmentManager(), RoleFullDialog.TAG);
                break;
            case SettingItem.SETTING_STORE:
                StoreFullDialog.newInstance().show(getSupportFragmentManager(),StoreFullDialog.TAG);
                break;
            case SettingItem.SETTING_CURRENCY:
                CurrencyFullDialog.newInstance().show(getSupportFragmentManager(),StoreFullDialog.TAG);
                break;
            default:
                //打开

        }
    }
}
