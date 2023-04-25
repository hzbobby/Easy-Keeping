package com.vividbobo.easy.ui.leger;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.vividbobo.easy.BaseActivity;
import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.adapter.LegerAdapter;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.databinding.ActivityLegerBinding;
import com.vividbobo.easy.ui.common.ContextOperationMenuDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;
import com.vividbobo.easy.utils.ConstantValue;
import com.vividbobo.easy.utils.ToastUtil;
import com.vividbobo.easy.viewmodel.ConfigViewModel;
import com.vividbobo.easy.viewmodel.LegerViewModel;

import java.util.List;

public class LegerActivity extends BaseActivity {
    private static final String TAG = "LegerActivity";
    private ActivityLegerBinding binding;
    private LegerViewModel legerViewModel;
    private ConfigViewModel configViewModel;

    private Leger selectedLeger;
    private LegerAdapter legerAdapter;

    private ContextOperationMenuDialog<Leger> operationDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLegerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        legerViewModel = new ViewModelProvider(this).get(LegerViewModel.class);
        configViewModel = new ViewModelProvider(this).get(ConfigViewModel.class);


        operationDialog = new ContextOperationMenuDialog<>();
        operationDialog.setOnDetailClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Leger>() {
            @Override
            public void onMenuItemClick(Leger item) {
                //TODO detail
            }
        });
        operationDialog.setOnDeleteClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Leger>() {
            @Override
            public void onMenuItemClick(Leger item) {
                if (item.getId() == ConstantValue.DEFAULT_ID) {
                    ToastUtil.makeToast("该账本不能删除");
                } else {
                    try {
                        if (item.getId() == selectedLeger.getId()) {
                            //删除的账本是当前选中的账本，则设置为默认账本
                            //修改选中的账本
                            configViewModel.updateSelected(Config.TYPE_LEGER, 1);
                        }
                        legerViewModel.delete(item);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "onMenuItemClick: 删除账本时发生错误");
                        ToastUtil.makeToast("删除账本时发生错误");
                    }
                }
            }
        });
        operationDialog.setOnEditClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Leger>() {
            @Override
            public void onMenuItemClick(Leger item) {
                AddLegerDialog.newInstance(item).show(getSupportFragmentManager(), AddLegerDialog.TAG);
            }
        });

        binding.legerToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //add dialog

        binding.legerToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_leger:
                        AddLegerDialog.newInstance().show(getSupportFragmentManager(), AddLegerDialog.TAG);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        //recycler view
        binding.legerRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        legerAdapter = new LegerAdapter(this);
        legerAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Object item, int position) {
                operationDialog.show(getSupportFragmentManager(), (Leger) item);
            }
        });

        legerAdapter.setOnEditClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                AddLegerDialog.newInstance((Leger) item).show(getSupportFragmentManager(), AddLegerDialog.TAG);
            }
        });
        legerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                selectedLeger = (Leger) item;
                legerAdapter.setSelectedLegerId(((Leger) item).getId());
            }
        });
        // observer
        legerViewModel.getAllLegersLD().observe(this, new Observer<List<Leger>>() {
            @Override
            public void onChanged(List<Leger> legers) {
                legerAdapter.updateItems(legers);
            }
        });

        binding.legerRecyclerView.setAdapter(legerAdapter);

        configViewModel.getSelectedLeger().observe(this, new Observer<Leger>() {
            @Override
            public void onChanged(Leger leger) {
                if (leger != null) {
                    selectedLeger = leger;
                    legerAdapter.setSelectedLegerId(leger.getId());
                    Log.d(TAG, "onChanged: selected leger title: " + leger.getTitle());
                }
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        //退出时修改选中账本
        if (selectedLeger != null) {
            Log.d(TAG, "onStop: update selected leger title:" + selectedLeger.getTitle());
            configViewModel.updateSelected(Config.TYPE_LEGER, selectedLeger.getId());
        }
    }
}