package com.vividbobo.easy.ui.dataImportAndExport;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.vividbobo.easy.BaseActivity;
import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.Itemzable;
import com.vividbobo.easy.adapter.adapter.BillAdapter;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.CheckableItem;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Payee;
import com.vividbobo.easy.database.model.QueryCondition;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.databinding.ActivityExportBillsBinding;
import com.vividbobo.easy.ui.bill.BillActivity;
import com.vividbobo.easy.ui.bill.BillDetailBottomSheet;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.CalendarUtils;
import com.vividbobo.easy.utils.ConstantValue;
import com.vividbobo.easy.utils.CsvUtil;
import com.vividbobo.easy.viewmodel.FilterViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ExportActivity extends BaseActivity implements View.OnClickListener, ExportSettingDialog.ExportClickListener {

    private static final int REQUEST_CODE_DOCUMENT_TREE = 0x0001;
    private static final int EXPORT_REQUEST_PERMISSIONS = 0x0002;
    private ActivityExportBillsBinding binding;
    private List<CheckableItem<Account>> accounts;
    private List<CheckableItem<Leger>> legers;
    private List<CheckableItem<Payee>> payees;
    private List<CheckableItem<Role>> roles;
    private List<CheckableItem<Tag>> tags;
    private List<CheckableItem<Category>> expenditureCategories;
    private List<CheckableItem<Category>> incomeCategories;
    private List<CheckableItem<Category>> categories;

    private List<Itemzable> items;
    private ExportBottomSheet exportBottomSheet;

    private QueryCondition queryCondition;
    private MaterialDatePicker<Pair<Long, Long>> datePicker;
    private FilterViewModel filterViewModel;
    private BillAdapter billAdapter;
    private List<Bill> queryBillResult;
    private ExportSettingDialog exportSettingDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExportBillsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dataInitial();
        configToolBar();
        configChip();
        configFilterGroup();
        configDataInitial();
        configViewModel();
        configBottomSheet();
        configSearch();
        configRecyclerView();
        configExport();
    }

    private void configExport() {
        exportSettingDialog = ExportSettingDialog.newInstance(this);
    }

    private void configRecyclerView() {
        billAdapter = new BillAdapter(this);
        billAdapter.setEnableHeader(false);
        billAdapter.setEnableFooter(false);
        billAdapter.setEnableMaxCount(false);
        billAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Bill bill = (Bill) item;
                BillDetailBottomSheet bottomSheet = BillDetailBottomSheet.newInstance(bill);
                bottomSheet.setOnRefundClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //退款
                        bill.setRefund(true);
                        //dismiss?
                        filterViewModel.updateBill(bill);
                        bottomSheet.dismiss();
                    }
                });
                bottomSheet.setOnDeleteClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterViewModel.deleteBill(bill);
                        bottomSheet.dismiss();
                    }
                });
                bottomSheet.setOnEditClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ExportActivity.this, BillActivity.class);
                        intent.putExtra(BillActivity.KEY_DATA_BILL, bill);
                        startActivity(intent);
                    }
                });
                bottomSheet.show(getSupportFragmentManager(), BillDetailBottomSheet.TAG);
            }
        });
        binding.billRv.setAdapter(billAdapter);
    }

    private void dataInitial() {
        queryCondition = QueryCondition.createBillCondition();

        datePicker = MaterialDatePicker.Builder.dateRangePicker().setTitleText("选择日期").setSelection(new Pair<>(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                String start = CalendarUtils.formatDate(new Date(selection.first), "yyyy/MM/dd");
                String end = CalendarUtils.formatDate(new Date(selection.second), "yyyy/MM/dd");
                binding.dateChip.setText(start + " - " + end);
            }
        });
        datePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.dateChip.setText("日期");
            }
        });
    }

    private void configSearch() {
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    private void addDateFilter() {
        queryCondition.getObjectMap().clear();
        Pair<Long, Long> pair = datePicker.getSelection();
        if (Objects.nonNull(pair) && !Objects.equals(binding.dateChip.getText().toString(), "日期")) {
            queryCondition.getObjectMap().put(QueryCondition.BILL_DATE_START, new Date(pair.first));
            queryCondition.getObjectMap().put(QueryCondition.BILL_DATE_END, new Date(pair.second));
        }
    }

    private void addInputFilter() {
        queryCondition.getLongMap().clear();

        String minAmountStr = binding.minAmountTil.getEditText().getText().toString();
        String maxAmountStr = binding.maxAmountTil.getEditText().getText().toString();
        if (minAmountStr.isEmpty()) {
            queryCondition.getLongMap().put(QueryCondition.BILL_MIN_AMOUNT, 0L);
        } else {
            try {
                Double d = Double.parseDouble(minAmountStr);
                queryCondition.getLongMap().put(QueryCondition.BILL_MIN_AMOUNT, (long) (d * 100));
            } catch (Exception e) {
                e.printStackTrace();
                queryCondition.getLongMap().put(QueryCondition.BILL_MIN_AMOUNT, 0L);
            }
        }
        if (maxAmountStr.isEmpty()) {
            queryCondition.getLongMap().put(QueryCondition.BILL_MAX_AMOUNT, Long.MAX_VALUE);
        } else {
            try {
                Double d = Double.parseDouble(maxAmountStr);
                queryCondition.getLongMap().put(QueryCondition.BILL_MAX_AMOUNT, (long) (d * 100));
            } catch (Exception e) {
                e.printStackTrace();
                queryCondition.getLongMap().put(QueryCondition.BILL_MAX_AMOUNT, Long.MAX_VALUE);
            }
        }
        queryCondition.getStringMap().clear();
        if (!binding.remarkTil.getEditText().getText().toString().isBlank())
            queryCondition.getStringMap().put(QueryCondition.BILL_REMARK, binding.remarkTil.getEditText().getText().toString());
    }

    private void filterConditions() {
        addUnCheckedIds(queryCondition.getIntSetMap().get(QueryCondition.BILL_LEGER), legers);
        addUnCheckedIds(queryCondition.getIntSetMap().get(QueryCondition.BILL_ACCOUNT), accounts);
        addUnCheckedIds(queryCondition.getIntSetMap().get(QueryCondition.BILL_ROLE), roles);
        addUnCheckedIds(queryCondition.getIntSetMap().get(QueryCondition.BILL_PAYEE), payees);
        if (categories.isEmpty()) {
            categories.addAll(expenditureCategories);
            categories.addAll(incomeCategories);
        }
        addUnCheckedIds(queryCondition.getIntSetMap().get(QueryCondition.BILL_CATEGORY), categories);

        // add unChecked tags
        List<String> tagTitles = new ArrayList<>();
        for (CheckableItem<Tag> checkableItem : tags) {
            if (!checkableItem.isChecked()) {
                tagTitles.add(checkableItem.getData().getTitle());
            }
        }
        queryCondition.getStringArrayMap().put(QueryCondition.BILL_TAG, tagTitles.toArray(new String[0]));

        //bill type
        queryCondition.getIntSetMap().get(QueryCondition.BILL_BILLTYPE).clear();
        if (binding.expenditureFilterBtn.isChecked())
            queryCondition.getIntSetMap().get(QueryCondition.BILL_BILLTYPE).add(Bill.EXPENDITURE);
        if (binding.incomeFilterBtn.isChecked())
            queryCondition.getIntSetMap().get(QueryCondition.BILL_BILLTYPE).add(Bill.INCOME);
        if (binding.transferFilterBtn.isChecked())
            queryCondition.getIntSetMap().get(QueryCondition.BILL_BILLTYPE).add(Bill.TRANSFER);

        queryCondition.getBoolMap().clear();
        // if true
        if (binding.imageFilterBtn.isChecked())
            queryCondition.getBoolMap().put(QueryCondition.BILL_IMAGE, true);
        if (binding.refundFilterBtn.isChecked())
            queryCondition.getBoolMap().put(QueryCondition.BILL_REFUND, true);
        if (binding.reimburseFilterBtn.isChecked())
            queryCondition.getBoolMap().put(QueryCondition.BILL_REIMBURSE, true);
        if (binding.budgetFilterBtn.isChecked())
            queryCondition.getBoolMap().put(QueryCondition.BILL_BUDGET, true);
        if (binding.inExpFilterBtn.isChecked())
            queryCondition.getBoolMap().put(QueryCondition.BILL_INCOME_EXPENDITURE, true);
    }


    private <T extends Itemzable> void addUnCheckedIds(Set<Integer> set, List<CheckableItem<T>> list) {
        set.clear();
        for (CheckableItem<T> checkableItem : list) {
            if (!checkableItem.isChecked()) {
                set.add(checkableItem.getData().getId());
            }
        }
    }

    private void search() {
        filterConditions();
        addInputFilter();
        addDateFilter();

        filterViewModel.query(queryCondition);
        Log.d("TAG", "search: " + queryCondition.toString());
    }

    private void configBottomSheet() {
        exportBottomSheet = new ExportBottomSheet();
        exportBottomSheet.setAllChecked(true);
        exportBottomSheet.setOnDoneClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportBottomSheet.dismiss();
            }
        });

    }


    private void configDataInitial() {
        accounts = new ArrayList<>();
        legers = new ArrayList<>();
        payees = new ArrayList<>();
        items = new ArrayList<>();
        roles = new ArrayList<>();
        tags = new ArrayList<>();
        expenditureCategories = new ArrayList<>();
        incomeCategories = new ArrayList<>();
        categories = new ArrayList<>();
    }

    private void configViewModel() {
        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);

        filterViewModel.getAccounts().observe(this, accountList -> {
            accounts.clear();
            accounts.addAll(accountList);
        });
        filterViewModel.getLegers().observe(this, legerList -> {
            legers.clear();
            legers.addAll(legerList);
        });
        filterViewModel.getPayees().observe(this, payeeList -> {
            payees.clear();
            payees.addAll(payeeList);
        });
        filterViewModel.getTags().observe(this, tagList -> {
            tags.clear();
            tags.addAll(tagList);
        });
        filterViewModel.getRoles().observe(this, roleList -> {
            roles.clear();
            roles.addAll(roleList);
        });
        filterViewModel.getExpenditureCategories().observe(this, categoryList -> {
            expenditureCategories.clear();
            expenditureCategories.addAll(categoryList);
        });
        filterViewModel.getIncomeCategories().observe(this, categoryList -> {
            incomeCategories.clear();
            incomeCategories.addAll(categoryList);
        });
        filterViewModel.getQueryBills().observe(this, billList -> {
            queryBillResult = billList;
            billAdapter.updateItems(billList);
        });
    }

    private void configFilterGroup() {
        binding.filterGroup.addOnButtonCheckedListener((group, viewId, isChecked) -> {
            switch (viewId) {
                case R.id.budget_filter_btn -> {

                }
                case R.id.image_filter_btn -> {
                }
                case R.id.expenditure_filter_btn -> {
                }
                case R.id.income_filter_btn -> {
                }
                case R.id.refund_filter_btn -> {
                }
                case R.id.reimburse_filter_btn -> {

                }
                case R.id.in_exp_filter_btn -> {

                }
                case R.id.transfer_filter_btn -> {

                }
            }
        });


    }


    private void configChip() {
        binding.categoryChip.setOnClickListener(this);
        binding.accountChip.setOnClickListener(this);
        binding.legerChip.setOnClickListener(this);
        binding.payeeChip.setOnClickListener(this);
        binding.roleChip.setOnClickListener(this);
        binding.tagChip.setOnClickListener(this);

        binding.dateChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(), "DatePicker");
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXPORT_REQUEST_PERMISSIONS) {
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) ||
                    Environment.isExternalStorageManager()) {
                exportSettingDialog.show(getSupportFragmentManager(), ExportSettingDialog.TAG);
            } else {
                Toast.makeText(this, "需要存储权限来导出CSV文件", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestStoragePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
                && Environment.isExternalStorageManager()) {
            // 在Android 11及更高版本中，可以直接访问存储
            // 进行相关操作
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXPORT_REQUEST_PERMISSIONS);

        } else {
            // 请求MANAGE_EXTERNAL_STORAGE权限
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private void configToolBar() {
        binding.appBarLayout.layoutToolBarTitleTv.setText("账单查询");
        binding.appBarLayout.layoutToolBar.inflateMenu(R.menu.export);
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(v -> finish());
        binding.appBarLayout.layoutToolBar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.export -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        exportSettingDialog.show(getSupportFragmentManager(), ExportSettingDialog.TAG);
                    } else {
                        requestStoragePermission();
                    }
                    return true;
                }
            }
            return false;
        });
    }

    private void exportBills(String directory) {
        List<Bill> dataList = queryBillResult;
        if (Objects.isNull(dataList) || dataList.isEmpty()) {
            Toast.makeText(this, "导出账单为空，已取消", Toast.LENGTH_SHORT).show();
            return;
        }
        CsvUtil.exportToCSV(Bill.class, dataList, directory, ConstantValue.EXPORT_BILL_CSV_FILENAME);
        Toast.makeText(this, "CSV文件已导出", Toast.LENGTH_SHORT).show();
    }

    private void exportAll(String directory) {
        List<Bill> exportBills = queryBillResult;
        if (Objects.isNull(exportBills) || exportBills.isEmpty()) {
            Toast.makeText(this, "导出账单为空，已取消", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Account> exportAccounts = new ArrayList<>();
        for (int i = 0; i < accounts.size(); i++) {
            exportAccounts.add(accounts.get(i).getData());
        }
        List<Category> exportCategories = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            exportCategories.add(categories.get(i).getData());
        }
        List<Leger> exportLegers = new ArrayList<>();
        for (int i = 0; i < legers.size(); i++) {
            exportLegers.add(legers.get(i).getData());
        }
        List<Role> exportRoles = new ArrayList<>();
        for (int i = 0; i < roles.size(); i++) {
            exportRoles.add(roles.get(i).getData());
        }
        List<Payee> exportPayees = new ArrayList<>();
        for (int i = 0; i < payees.size(); i++) {
            exportPayees.add(payees.get(i).getData());
        }
        List<Tag> exportTags = new ArrayList<>();
        for (int i = 0; i < tags.size(); i++) {
            exportTags.add(tags.get(i).getData());
        }
        CsvUtil.exportToCSV(Bill.class, exportBills, directory, ConstantValue.EXPORT_BILL_CSV_FILENAME);
        CsvUtil.exportToCSV(Account.class, exportAccounts, directory, ConstantValue.EXPORT_ACCOUNT_CSV_FILENAME);
        CsvUtil.exportToCSV(Category.class, exportCategories, directory, ConstantValue.EXPORT_CATEGORY_CSV_FILENAME);
        CsvUtil.exportToCSV(Leger.class, exportLegers, directory, ConstantValue.EXPORT_LEGER_CSV_FILENAME);
        CsvUtil.exportToCSV(Role.class, exportRoles, directory, ConstantValue.EXPORT_ROLE_CSV_FILENAME);
        CsvUtil.exportToCSV(Payee.class, exportPayees, directory, ConstantValue.EXPORT_PAYEE_CSV_FILENAME);
        CsvUtil.exportToCSV(Tag.class, exportTags, directory, ConstantValue.EXPORT_TAG_CSV_FILENAME);

        Toast.makeText(this, "CSV文件已导出", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        //chip click
        switch (v.getId()) {
            case R.id.category_chip -> {
                categories.clear();
                if (binding.expenditureFilterBtn.isChecked()) {
                    categories.addAll(expenditureCategories);
                }
                if (binding.incomeFilterBtn.isChecked()) {
                    categories.addAll(incomeCategories);
                }
                exportBottomSheet.updateItems(categories);
            }
            case R.id.account_chip -> {
                exportBottomSheet.updateItems(accounts);
            }

            case R.id.leger_chip -> {
                exportBottomSheet.updateItems(legers);
            }
            case R.id.payee_chip -> {
                exportBottomSheet.updateItems(payees);
            }
            case R.id.role_chip -> {
                exportBottomSheet.updateItems(roles);
            }
            case R.id.tag_chip -> {
                exportBottomSheet.updateItems(tags);
            }
        }
        exportBottomSheet.show(getSupportFragmentManager(), ExportBottomSheet.TAG);
    }

    @Override
    public void onExport(boolean isExportAllTables, String parentDir) {
        // export dialog
        if (isExportAllTables) {
            exportAll(parentDir);
        } else {
            exportBills(parentDir);

        }
    }
}
