package com.vividbobo.easy.ui.dataImportAndExport;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.R;
import com.vividbobo.easy.databinding.DialogImportSettingBinding;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;
import com.vividbobo.easy.utils.ConstantValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataImportSettingDialog extends BaseFullScreenMaterialDialog<DialogImportSettingBinding> implements CompoundButton.OnCheckedChangeListener {
    public static final String TAG = "DataImportSettingDialog";

    private SharedPreferences sp;
    private Map<String, Boolean> replaceBoolMap;

    public static DataImportSettingDialog newInstance() {

        Bundle args = new Bundle();

        DataImportSettingDialog fragment = new DataImportSettingDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getContext().getSharedPreferences(ConstantValue.DATA_IMPORT_SETTINGS_SP, Context.MODE_PRIVATE);
        replaceBoolMap = new HashMap<>();
        replaceBoolMap.put("billReplace", sp.getBoolean("billReplace", false));
        replaceBoolMap.put("legerReplace", sp.getBoolean("legerReplace", false));
        replaceBoolMap.put("accountReplace", sp.getBoolean("accountReplace", false));
        replaceBoolMap.put("categoryReplace", sp.getBoolean("categoryReplace", false));
        replaceBoolMap.put("roleReplace", sp.getBoolean("roleReplace", false));
        replaceBoolMap.put("payeeReplace", sp.getBoolean("payeeReplace", false));
    }

    @Override
    protected DialogImportSettingBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogImportSettingBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(DialogImportSettingBinding binding) {
        binding.appBarLayout.layoutToolBarTitleTv.setText("数据导入设置");
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //sw initial
        binding.billConflictSw.setChecked(replaceBoolMap.get("billReplace"));
        binding.legerConflictSw.setChecked(replaceBoolMap.get("legerReplace"));
        binding.accountConflictSw.setChecked(replaceBoolMap.get("accountReplace"));
        binding.categoryConflictSw.setChecked(replaceBoolMap.get("categoryReplace"));
        binding.roleConflictSw.setChecked(replaceBoolMap.get("roleReplace"));
        binding.payeeConflictSw.setChecked(replaceBoolMap.get("payeeReplace"));

        binding.billConflictSw.setOnCheckedChangeListener(this);
        binding.legerConflictSw.setOnCheckedChangeListener(this);
        binding.accountConflictSw.setOnCheckedChangeListener(this);
        binding.categoryConflictSw.setOnCheckedChangeListener(this);
        binding.roleConflictSw.setOnCheckedChangeListener(this);
        binding.payeeConflictSw.setOnCheckedChangeListener(this);

    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        // save to sp
        if (Objects.nonNull(sp)) {
            sp.edit().putBoolean("billReplace", replaceBoolMap.get("billReplace"))
                    .putBoolean("legerReplace", replaceBoolMap.get("legerReplace"))
                    .putBoolean("accountReplace", replaceBoolMap.get("accountReplace"))
                    .putBoolean("categoryReplace", replaceBoolMap.get("categoryReplace"))
                    .putBoolean("roleReplace", replaceBoolMap.get("roleReplace"))
                    .putBoolean("payeeReplace", replaceBoolMap.get("payeeReplace"))
                    .apply();

            sp = null;
        }
        super.onDismiss(dialog);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.bill_conflict_sw -> replaceBoolMap.put("billReplace", isChecked);
            case R.id.leger_conflict_sw -> replaceBoolMap.put("legerReplace", isChecked);
            case R.id.account_conflict_sw -> replaceBoolMap.put("accountReplace", isChecked);
            case R.id.category_conflict_sw -> replaceBoolMap.put("categoryReplace", isChecked);
            case R.id.role_conflict_sw -> replaceBoolMap.put("roleReplace", isChecked);
            case R.id.payee_conflict_sw -> replaceBoolMap.put("payeeReplace", isChecked);
        }
    }
}
