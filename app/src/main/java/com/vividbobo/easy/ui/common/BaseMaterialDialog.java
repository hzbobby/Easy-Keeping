package com.vividbobo.easy.ui.common;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vividbobo.easy.R;

import java.util.LinkedHashSet;

/**
 * DialogPicker 基类
 *
 * @param <VB> ViewBinding产生的类
 */
public abstract class BaseMaterialDialog<VB extends androidx.viewbinding.ViewBinding> extends DialogFragment {

    public interface OnPickerConfirmClickListener<T> {
        void onClick(T result);
    }

    /**
     * 采用ViewBinding进行视图绑定
     */
    private VB binding;


    protected abstract VB getViewBinding(@NonNull LayoutInflater inflater);

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = getViewBinding(getLayoutInflater());
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                .setView(binding.getRoot());

        onBuildDialog(builder);
        onBindView(binding);

        return builder.create();
    }

    /**
     * 使用builder建造Dialog
     */
    protected abstract void onBuildDialog(MaterialAlertDialogBuilder builder);

    /**
     * 与binding 数据、视图相关的操作
     */
    protected abstract void onBindView(VB binding);

}
