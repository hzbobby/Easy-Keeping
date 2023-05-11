package com.vividbobo.easy.ui.dataImportAndExport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayoutMediator;
import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.adapter.BillAdapter;
import com.vividbobo.easy.adapter.adapter.ItemAdapter;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Payee;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.databinding.ActivityImportBillsBinding;
import com.vividbobo.easy.utils.ConstantValue;
import com.vividbobo.easy.utils.CsvUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImportActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_OPEN_DOCUMENT_TREE = 0x101;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 0x201;
    private static final int REQUEST_CODE_PICK_MULTI_FILE = 0x301;
    private ActivityImportBillsBinding binding;
    private BillAdapter billAdapter;
    private ItemAdapter<Leger> legerAdapter;
    private ItemAdapter<Category> categoryAdapter;
    private ItemAdapter<Account> accountAdapter;
    private ItemAdapter<Role> roleAdapter;
    private ItemAdapter<Payee> payeeAdapter;
    private SharedPreferences sp;
    private List<Bill> billList;
    private List<Leger> legerList;
    private List<Account> accountList;
    private List<Category> categoryList;
    private List<Role> roleList;
    private List<Payee> payeeList;

    private ImportActivityViewModel importActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImportBillsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sp = this.getSharedPreferences(ConstantValue.DATA_IMPORT_SETTINGS_SP, MODE_PRIVATE);
        importActivityViewModel = new ViewModelProvider(this).get(ImportActivityViewModel.class);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.data_import) {
                    //选择文件夹导入
                    checkAndRequestStoragePermission();
                    return true;
                } else if (item.getItemId() == R.id.import_setting) {
                    DataImportSettingDialog.newInstance().show(getSupportFragmentManager(), DataImportSettingDialog.TAG);
                    return true;
                }
                return false;
            }
        });


        billAdapter = new BillAdapter(this);
        billAdapter.setEnableFooter(false);
        billAdapter.setEnableHeader(false);
        legerAdapter = new ItemAdapter(this);
        legerAdapter.setEnableIcon(true);
        categoryAdapter = new ItemAdapter<>(this);
        categoryAdapter.setEnableIcon(true);
        accountAdapter = new ItemAdapter<>(this);
        accountAdapter.setEnableIcon(true);
        roleAdapter = new ItemAdapter<>(this);
        roleAdapter.setEnableIcon(true);
        payeeAdapter = new ItemAdapter<>(this);
        payeeAdapter.setEnableIcon(true);


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ListTabFragment.newInstance(billAdapter));
        fragments.add(ListTabFragment.newInstance(legerAdapter));
        fragments.add(ListTabFragment.newInstance(accountAdapter));
        fragments.add(ListTabFragment.newInstance(categoryAdapter));
        fragments.add(ListTabFragment.newInstance(roleAdapter));
        fragments.add(ListTabFragment.newInstance(payeeAdapter));

        binding.viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragments));

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("账单");
                    break;
                case 1:
                    tab.setText("账本");
                    break;
                case 2:
                    tab.setText("账户");
                    break;
                case 3:
                    tab.setText("类别");
                    break;
                case 4:
                    tab.setText("角色");
                    break;
                case 5:
                    tab.setText("收款方");
                    break;
            }
        }).attach();

        //import btn click
        binding.importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataImport();
            }
        });
    }

    private void dataImport() {
        //read sp
        Boolean billReplace = sp.getBoolean("billReplace", false);
        Boolean legerReplace = sp.getBoolean("legerReplace", false);
        Boolean accountReplace = sp.getBoolean("accountReplace", false);
        Boolean categoryReplace = sp.getBoolean("categoryReplace", false);
        Boolean roleReplace = sp.getBoolean("roleReplace", false);
        Boolean payeeReplace = sp.getBoolean("payeeReplace", false);

        //如果不是进行替换，data的id全置为null
        if (!billReplace) {
            billList.forEach(bill -> bill.setId(null));
        }
        if (!legerReplace) {
            legerList.forEach(leger -> leger.setId(null));
        }
        if (!accountReplace) {
            accountList.forEach(account -> account.setId(null));
        }
        if (!categoryReplace) {
            categoryList.forEach(category -> category.setId(null));
        }
        if (!roleReplace) {
            roleList.forEach(role -> role.setId(null));
        }
        if (!payeeReplace) {
            payeeList.forEach(payee -> payee.setIconResId(null));
        }


        //insert
        importActivityViewModel.insertBills(billList);
        importActivityViewModel.insertLegers(legerList);
        importActivityViewModel.insertAccounts(accountList);
        importActivityViewModel.insertCategories(categoryList);
        importActivityViewModel.insertRoles(roleList);
        importActivityViewModel.insertPayees(payeeList);
    }

    private void checkAndRequestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
                    && Environment.isExternalStorageManager()) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_STORAGE_PERMISSION);
            } else {
                // 请求MANAGE_EXTERNAL_STORAGE权限
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        } else {
            openFilePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) ||
                    Environment.isExternalStorageManager()) {
                openFilePicker();
            } else {
                Toast.makeText(this, "请授予读取权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openDocumentTree() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_CODE_OPEN_DOCUMENT_TREE);
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*"); // 仅显示 CSV 文件
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 允许多选文件
        startActivityForResult(intent, REQUEST_CODE_PICK_MULTI_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_MULTI_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                List<Uri> fileUris = new ArrayList<>();
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        fileUris.add(uri);
                    }
                } else {
                    Uri uri = data.getData();
                    fileUris.add(uri);
                }
                // 对选中的文件进行处理
                Map<String, List> map= CsvUtil.readCSVFiles(fileUris,this);
                billList = map.get(ConstantValue.EXPORT_BILL_CSV_FILENAME);
                legerList = map.get(ConstantValue.EXPORT_LEGER_CSV_FILENAME);
                accountList = map.get(ConstantValue.EXPORT_ACCOUNT_CSV_FILENAME);
                categoryList = map.get(ConstantValue.EXPORT_CATEGORY_CSV_FILENAME);
                roleList = map.get(ConstantValue.EXPORT_ROLE_CSV_FILENAME);
                payeeList = map.get(ConstantValue.EXPORT_PAYEE_CSV_FILENAME);

                billAdapter.updateItems(billList);
                legerAdapter.updateItems(legerList);
                accountAdapter.updateItems(accountList);
                categoryAdapter.updateItems(categoryList);
                roleAdapter.updateItems(roleList);
                payeeAdapter.updateItems(payeeList);

                binding.importBtn.setVisibility(View.VISIBLE);
            }
        }
//        if (requestCode == REQUEST_CODE_OPEN_DOCUMENT_TREE && resultCode == RESULT_OK && data != null) {
//            Uri uri = data.getData();
//            if (uri != null) {
//                Map<String, List> map = CsvUtil.readCSVFolder(uri, this);
//                billList = map.get(ConstantValue.EXPORT_BILL_CSV_FILENAME);
//                legerList = map.get(ConstantValue.EXPORT_LEGER_CSV_FILENAME);
//                accountList = map.get(ConstantValue.EXPORT_ACCOUNT_CSV_FILENAME);
//                categoryList = map.get(ConstantValue.EXPORT_CATEGORY_CSV_FILENAME);
//                roleList = map.get(ConstantValue.EXPORT_ROLE_CSV_FILENAME);
//                payeeList = map.get(ConstantValue.EXPORT_PAYEE_CSV_FILENAME);
//
//                billAdapter.updateItems(billList);
//                legerAdapter.updateItems(legerList);
//                accountAdapter.updateItems(accountList);
//                categoryAdapter.updateItems(categoryList);
//                roleAdapter.updateItems(roleList);
//                payeeAdapter.updateItems(payeeList);
//
//                binding.importBtn.setVisibility(View.VISIBLE);
//
//            } else {
//
//            }
//        }
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {

        private final List<Fragment> fragments;

        public ViewPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
            super(fragmentManager, lifecycle);
            this.fragments = fragments;
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sp != null) {
            sp = null;
        }
    }
}