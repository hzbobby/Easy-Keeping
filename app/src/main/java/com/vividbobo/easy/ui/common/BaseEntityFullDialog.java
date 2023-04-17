package com.vividbobo.easy.ui.common;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.BaseEntityAdapter;
import com.vividbobo.easy.database.model.BaseEntity;
import com.vividbobo.easy.databinding.DialogBaseEntityBinding;
import com.vividbobo.easy.ui.others.OnItemClickListener;

public abstract class BaseEntityFullDialog<T extends BaseEntity> extends BaseFullScreenMaterialDialog<DialogBaseEntityBinding> {

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public abstract void menuAddClick();

    /**
     * 额外配置
     *
     * @param binding
     * @param adapter
     */
    public abstract void config(DialogBaseEntityBinding binding, BaseEntityAdapter<T> adapter);


    @Override
    public DialogBaseEntityBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogBaseEntityBinding.inflate(inflater);
    }

    @Override
    public void onViewBinding(DialogBaseEntityBinding binding) {
        binding.appBarLayout.layoutToolBar.inflateMenu(R.menu.menu_add);
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.appBarLayout.layoutToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add) {
                    menuAddClick();
                    return true;
                }
                return false;
            }
        });


        BaseEntityAdapter<T> baseEntityAdapter = new BaseEntityAdapter<T>(getContext());
        baseEntityAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, item, position);
                }
            }
        });

        binding.baseEntityRv.setAdapter(baseEntityAdapter);


        config(binding, baseEntityAdapter);

    }
}
