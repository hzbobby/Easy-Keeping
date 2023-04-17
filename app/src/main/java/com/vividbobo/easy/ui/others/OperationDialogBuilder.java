package com.vividbobo.easy.ui.others;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vividbobo.easy.utils.ToastUtil;

@Deprecated
public class OperationDialogBuilder<T> {
    public interface OnOperationMenuClickListener<T> {
        void onMenuItemClick(T item);
    }

    private MaterialAlertDialogBuilder dialogBuilder;
    private T item;     //the item of long click

    private String title = "操作";
    private String[] items = new String[]{"明细", "编辑", "删除"};
    private OnOperationMenuClickListener<T> onDetailClickListener;
    private OnOperationMenuClickListener<T> onEditClickListener;
    private OnOperationMenuClickListener<T> onDeleteClickListener;

    public OperationDialogBuilder setOnDetailClickListener(OnOperationMenuClickListener<T> onDetailClickListener) {
        this.onDetailClickListener = onDetailClickListener;
        return this;
    }

    public OperationDialogBuilder setOnDeleteClickListener(OnOperationMenuClickListener<T> onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
        return this;
    }

    public OperationDialogBuilder setOnEditClickListener(OnOperationMenuClickListener<T> onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
        return this;
    }

    public OperationDialogBuilder(Context context) {
        dialogBuilder = new MaterialAlertDialogBuilder(context).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (onDetailClickListener != null)
                            onDetailClickListener.onMenuItemClick(item);
                        break;
                    case 1:
                        if (onEditClickListener != null) onEditClickListener.onMenuItemClick(item);
                        break;
                    default:
                        if (onDeleteClickListener != null)
                            onDeleteClickListener.onMenuItemClick(item);
                }
            }
        });
        //default settings
        dialogBuilder.setTitle(title);
    }

    public AlertDialog create() {
        return dialogBuilder.create();
    }

}
