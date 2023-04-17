package com.vividbobo.easy.ui.common;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.BaseEntity;
import com.vividbobo.easy.databinding.DialogAddBaseEntityBinding;

public abstract class BaseEntityAddFullDialog<T extends BaseEntity> extends BaseFullScreenMaterialDialog<DialogAddBaseEntityBinding> {
    protected static final String KEY_DATA = "data";

    public abstract String getTitle();

    public abstract void save(String title, String desc);

    public abstract void update(T entity);


    @Override
    protected DialogAddBaseEntityBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogAddBaseEntityBinding.inflate(inflater);
    }


    @Override
    protected void onViewBinding(DialogAddBaseEntityBinding binding) {

        T entity = (T) getArguments().getSerializable(KEY_DATA);
        if (entity != null) {
            if (entity.getTitle() != null)
                binding.dialogTitleTil.getEditText().setText(entity.getTitle());
            if (entity.getDesc() != null)
                binding.dialogDescTil.getEditText().setText(entity.getDesc());
        }

        binding.appBarLayout.layoutToolBarTitleTv.setText(getTitle());
        binding.appBarLayout.layoutToolBar.inflateMenu(R.menu.confirm);
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.appBarLayout.layoutToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.confirm) {
                    String title = binding.dialogTitleTil.getEditText().getText().toString();
                    String desc = binding.dialogDescTil.getEditText().getText().toString();
                    if (entity == null) {
                        save(title, desc);
                    } else {
                        entity.setTitle(title);
                        entity.setDesc(desc);
                        update(entity);
                    }
                    dismiss();
                    return true;
                }
                return false;
            }
        });

    }
}
