package com.vividbobo.easy.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vividbobo.easy.MainActivity;
import com.vividbobo.easy.databinding.ActivitySettingsBinding;
import com.vividbobo.easy.model.SettingItem;
import com.vividbobo.easy.ui.others.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
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
        settingItems.add(new SettingItem("标签管理", "com.vividbobo.easy.ui.tags.TagsActivity"));

        //data recycler view
        binding.settingDataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SettingsAdapter dataAdapter = new SettingsAdapter();
        dataAdapter.setOpenHeader(true);
        dataAdapter.setHeaderTitle("全部");
        dataAdapter.setData(settingItems);
        dataAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(Object item, int position) {
                //点击进入相应的activity
                try {
                    Intent intent = new Intent(SettingsActivity.this, Class.forName(((SettingItem) item).getTargetClass()));
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        binding.settingDataRecyclerView.setAdapter(dataAdapter);


    }
}
