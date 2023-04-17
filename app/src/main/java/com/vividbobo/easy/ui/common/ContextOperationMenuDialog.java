package com.vividbobo.easy.ui.common;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vividbobo.easy.R;
import com.vividbobo.easy.databinding.DialogOperationBinding;
import com.vividbobo.easy.ui.others.OperationDialogBuilder;

public class ContextOperationMenuDialog<T> extends BaseMaterialDialog<DialogOperationBinding> {
    public static final String TAG = "ContextOperationMenuDia";
    private T item;

    public interface OnOperationMenuItemClickListener<S> {
        void onMenuItemClick(S item);
    }

    private OnOperationMenuItemClickListener<T> onDetailClickListener;
    private OnOperationMenuItemClickListener<T> onEditClickListener;
    private OnOperationMenuItemClickListener<T> onDeleteClickListener;

    public static final int MENU_ITEM_DETAIL = 0;
    public static final int MENU_ITEM_EDIT = 1;
    public static final int MENU_ITEM_DELETE = 2;

    //菜单项visibility
    private int[] menuItemVisibility = {View.VISIBLE, View.VISIBLE, View.VISIBLE,};

    public void setMenuItemVisible(int which, int visibility) {
        menuItemVisibility[which] = visibility;
    }

    public void setOnEditClickListener(OnOperationMenuItemClickListener<T> onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    public void setOnDeleteClickListener(OnOperationMenuItemClickListener<T> onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnDetailClickListener(OnOperationMenuItemClickListener<T> onDetailClickListener) {
        this.onDetailClickListener = onDetailClickListener;
    }

    @Override
    protected DialogOperationBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogOperationBinding.inflate(inflater);
    }

    @Override
    protected void onBuildDialog(MaterialAlertDialogBuilder builder) {
        builder.setTitle(R.string.operation);
    }

    @Override
    protected void onBindView(DialogOperationBinding binding) {
        //visible settings
        binding.operationDetailTv.setVisibility(menuItemVisibility[MENU_ITEM_DETAIL]);
        binding.operationEditTv.setVisibility(menuItemVisibility[MENU_ITEM_EDIT]);
        binding.operationDeleteTv.setVisibility(menuItemVisibility[MENU_ITEM_DELETE]);

        binding.operationDeleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onMenuItemClick(item);
                }
                dismiss();
            }
        });
        binding.operationDetailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDetailClickListener != null) {
                    onDetailClickListener.onMenuItemClick(item);
                }
                dismiss();
            }
        });
        binding.operationEditTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditClickListener != null) {
                    onEditClickListener.onMenuItemClick(item);
                }
                dismiss();
            }
        });
    }

    public void show(@NonNull FragmentManager manager, T item) {
        this.item = item;
        show(manager, TAG);
    }

}
