package com.vividbobo.easy.ui.dataImportAndExport;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vividbobo.easy.databinding.DialogExportSettingBinding;
import com.vividbobo.easy.ui.common.BaseMaterialDialog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExportSettingDialog extends BaseMaterialDialog<DialogExportSettingBinding> {
    public static final String TAG = "ExportSettingDialog";

    public interface ExportClickListener {
        void onExport(boolean isExportAllTables,String parentDir);
    }

    private ExportClickListener exportClickListener;

    public static ExportSettingDialog newInstance(ExportClickListener listener) {
        Bundle args = new Bundle();
        ExportSettingDialog fragment = new ExportSettingDialog();
        fragment.setArguments(args);
        fragment.setExportClickListener(listener);
        return fragment;
    }

    public void setExportClickListener(ExportClickListener exportClickListener) {
        this.exportClickListener = exportClickListener;
    }

    @Override
    protected DialogExportSettingBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogExportSettingBinding.inflate(inflater);
    }

    @Override
    protected void onBuildDialog(MaterialAlertDialogBuilder builder) {
    }

    @Override
    protected void onBindView(DialogExportSettingBinding binding) {
    }

    @Override
    protected void onCreateDialog(MaterialAlertDialogBuilder builder, DialogExportSettingBinding binding) {
        super.onCreateDialog(builder, binding);

        builder.setTitle("账单导出").setPositiveButton("导出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String parentDir;
                //permission
                if (binding.filePathTil.getEditText().getText().toString().isEmpty()) {
                    //日期式
                    parentDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
                } else {
                    //input
                    parentDir = binding.filePathTil.getEditText().getText().toString();
                }
                if (exportClickListener!=null){
                    exportClickListener.onExport(binding.exportAllTablesSw.isChecked(),parentDir);
                }
                dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        binding.exportAllTablesSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.exportAllTablesSw.setText("导出所有表");
                } else {
                    binding.exportAllTablesSw.setText("仅导出账单表");
                }
            }
        });

    }



}
