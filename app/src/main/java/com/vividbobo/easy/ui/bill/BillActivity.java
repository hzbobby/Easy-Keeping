package com.vividbobo.easy.ui.bill;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.BaseActivity;
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.database.model.Store;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.databinding.ActivityBillBinding;
import com.vividbobo.easy.ui.account.AccountPicker;
import com.vividbobo.easy.ui.common.BaseMaterialDialog;
import com.vividbobo.easy.ui.leger.LegerActivity;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.role.RolePicker;
import com.vividbobo.easy.ui.store.StorePicker;
import com.vividbobo.easy.ui.tags.TagPicker;
import com.vividbobo.easy.utils.AsyncProcessor;
import com.vividbobo.easy.utils.FileUtil;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.viewmodel.AccountViewModel;
import com.vividbobo.easy.viewmodel.BillViewModel;
import com.vividbobo.easy.viewmodel.ConfigViewModel;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class BillActivity extends BaseActivity {
    private static final String TAG = "BillActivity";
    private ActivityBillBinding binding;
    private BillViewModel billViewModel;
    private ConfigViewModel configViewModel;
    private AccountViewModel accountViewModel;


    private StringBuilder expression = new StringBuilder();
    private StringBuilder resultBoardText = new StringBuilder();

    private View.OnClickListener calculatorKeyPressListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MaterialButton button = (MaterialButton) view;
            String tag = (String) view.getTag();
            String text = (String) button.getText();

            if (tag.equals(ResourceUtils.getString(R.string.key_tag_equal))) {
                if (binding.keyEqual.getText().toString().equals(ResourceUtils.getString(R.string.key_done))) {
                    //保存
                    save();
                    finish();
                } else {
                    //计算表达式
                    Double res = calculate(expression.toString());

                    if (res == null) {
                        //error
                        clearResultBoard();
                        binding.resultBoardText.setHint(ResourceUtils.getString(R.string.cal_error_info));
                    } else {
                        //更新表达式
                        res = 1.0 * Math.round(res * 100) / 100;//四舍五入
                        resultBoardText = new StringBuilder(String.format("%,.2f", res));
                        expression = new StringBuilder(String.valueOf(res));
                    }
                    //记录数值
                    billViewModel.setAmount((long) (res * 100));
                    binding.keyEqual.setText(ResourceUtils.getString(R.string.key_done));
                }

            } else if (tag.equals(ResourceUtils.getString(R.string.key_tag_backspace))) {
                //退格
                if (expression.length() > 0) {
                    expression.deleteCharAt(expression.length() - 1);
                    resultBoardText.deleteCharAt(resultBoardText.length() - 1);
                }
            } else if (tag.equals(ResourceUtils.getString(R.string.key_tag_all_clear))) {
                clearResultBoard();


            } else if (tag.equals(ResourceUtils.getString(R.string.key_tag_record_again))) {
                clearResultBoard();


                save();

            } else if (tag.equals(ResourceUtils.getString(R.string.key_tag_dot))) {
                expression.append(tag);
                resultBoardText.append(tag);
            } else {
                //记录表达式
                binding.keyEqual.setText(R.string.key_equal);
                expression.append(tag);
                resultBoardText.append(text);
            }

            binding.resultBoardText.setText(resultBoardText.toString());
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        billViewModel = new ViewModelProvider(this).get(BillViewModel.class);
        configViewModel = new ViewModelProvider(this).get(ConfigViewModel.class);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);


        /***************** toolbar 相关设置 *****************/
        //toolbar
        binding.billToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        BillTemplateSheetDialog templateSheetDialog = BillTemplateSheetDialog.newInstance();
        binding.billToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.bill_template:
                        templateSheetDialog.show(getSupportFragmentManager(), BillTemplateSheetDialog.TAG);
                        return true;
                    default:
                        return false;
                }
            }
        });

        /******************* tab layout & view pager2 **********************/
        BillFragmentAdapter fragmentAdapter = new BillFragmentAdapter(this);
        binding.billViewPager.setAdapter(fragmentAdapter);
        binding.billViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int expenditureVisible = (position == 1 || position == 2) ? View.GONE : View.VISIBLE;
                int transferVisible = position == 2 ? View.GONE : View.VISIBLE;
                switch (position) {
                    case 0:
                        billViewModel.setBillType(Bill.EXPENDITURE);
                        break;
                    case 1:
                        billViewModel.setBillType(Bill.INCOME);
                        break;
                    default:
                        billViewModel.setBillType(Bill.TRANSFER);
                }

                binding.billStorePickerChip.setVisibility(expenditureVisible);
                binding.billPickAccountChip.setVisibility(transferVisible);
                binding.billRefundCheckChip.setVisibility(expenditureVisible);
                binding.billReimburseCheckChip.setVisibility(expenditureVisible);
                binding.billStatisticsChip.setVisibility(transferVisible);
//                binding.billReceivePaymentChip.setVisibility(expenditureVisible);
            }
        });
        //viewpager bind with tab layout
        new TabLayoutMediator(binding.billTabLayout, binding.billViewPager, true, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(fragmentAdapter.tabTitles.get(position));
            }
        }).attach();


        //
        viewModelObserve();
        remarkConfig();
        calculaterConfig();
        pickersConfig();
    }

    private void remarkConfig() {
        /************** remark confing *************/
        binding.billRemarkExpandMoreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                RemarkFullDialog.newInstance().show(getSupportFragmentManager(), RemarkFullDialog.TAG);
                RemarkSheetDialog.newInstance().show(getSupportFragmentManager(), RemarkSheetDialog.TAG);
            }
        });
        //设置remark小窗口和全屏窗口的信息的同步
        binding.billRemark.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    billViewModel.setRemark(binding.billRemark.getText().toString());
                    binding.billRemark.clearFocus();
                }
                return false;
            }
        });
    }

    /**
     * 顶部chip bar 的一些相关配置：点击事件、...
     */
    private void pickersConfig() {
        /******************* statistics chip picker config *********************/
        // 统计选项选中记录数组
        boolean[] statisticsCheckItems = new boolean[2];
        AlertDialog statisticsPicker = new MaterialAlertDialogBuilder(binding.getRoot().getContext()).setTitle(R.string.pick_statistics).setMultiChoiceItems(R.array.statistics_items, statisticsCheckItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                switch (which) {
                    case 0:
                        statisticsCheckItems[0] = isChecked;
                        break;
                    case 1:
                        statisticsCheckItems[1] = isChecked;
                        break;
                    default:

                }
            }
        }).setNegativeButton(R.string.cancel, null).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //计入viewModel
                billViewModel.setIncomeExpenditureIncluded(statisticsCheckItems[0]);
                billViewModel.setBudgetIncluded(statisticsCheckItems[1]);
            }
        }).create();
        binding.billStatisticsChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsPicker.show();
            }
        });

        /***************** image picker *******************/
        //image viewer sheet
        ImageViewerSheet imageViewerSheet = ImageViewerSheet.newInstance();
        //imagePicker
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(9), uris -> {
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            if (!uris.isEmpty()) {
                /**
                 * 存储思路
                 * 先存储为内存文件，保存时，再写入系统
                 * 先写成功后，再保存账单条目
                 *
                 * 1.作为内部文件
                 * 2.作为数据库字段
                 * 3.作为服务器文件  yes, 先存储再应用内部，再上传，上传成功，可删除该文件
                 */
                /**
                 * 先保存Uri，待点保存时，再进行写入应用内部
                 */
                List<String> imagePaths = new ArrayList<>();
                for (Uri uri : uris) {
                    String path = FileUtil.getFilePathFromContentUri(this, uri);
                    Log.d(TAG, "pickersConfig: real path: " + path);
                    imagePaths.add(path);
                }
                billViewModel.setImagePaths(imagePaths);
            }
        });
        //编译器报错bug，采用 activityx.1.7以上就不会
        ActivityResultContracts.PickVisualMedia.VisualMediaType mediaType = (ActivityResultContracts.PickVisualMedia.VisualMediaType) ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE;
        PickVisualMediaRequest request = new PickVisualMediaRequest.Builder().setMediaType(mediaType).build();

        binding.billPhotoPickerChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if 选择了附件，进入图片查看器 bottom sheet？
                //else 进行选择图片
                if (billViewModel.getImagePaths() != null && !billViewModel.getImagePaths().getValue().isEmpty()) {  //查看图片
                    imageViewerSheet.show(getSupportFragmentManager(), ImageViewerSheet.TAG);
                } else {
                    //选择图片
                    pickMedia.launch(request);
                }
            }
        });

        /******************** date picker **********************/
        //日期
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(ResourceUtils.getString(R.string.pick_date)).build();
        datePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener) selection -> billViewModel.setDate(new Date((Long) selection)));
        binding.billDatePickerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(), "BillDatePickers");
            }
        });
        /******************** local time picker ***********************/
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder().setTitleText(ResourceUtils.getString(R.string.pick_time)).build();
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billViewModel.setTime(LocalTime.of(timePicker.getHour(), timePicker.getMinute()));
            }
        });
        binding.billTimePickerChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.show(getSupportFragmentManager(), "BillTimePicker");
            }
        });

        /**************** leger picker *****************/
        binding.billPickLegerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillActivity.this, LegerActivity.class);
                startActivity(intent);
            }
        });


        /**************** account picker *************/
        //账户
        AccountPicker accountPicker = new AccountPicker();
        accountPicker.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Account account = (Account) item;
                billViewModel.setSrcAccount(account);
                accountPicker.dismiss();
            }
        });
        binding.billPickAccountChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountPicker.show(getSupportFragmentManager(), AccountPicker.TAG);
            }
        });

        /**************** role picker **************/
        //角色
        RolePicker rolePicker = new RolePicker();
        rolePicker.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Role role = (Role) item;
                billViewModel.setRole(role);
                rolePicker.dismiss();
            }
        });
        binding.billRolePickerChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rolePicker.show(getSupportFragmentManager(), RolePicker.TAG);
            }
        });

        /*************** store picker *****************/
        StorePicker storePicker = new StorePicker();
        storePicker.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Store store = (Store) item;
                billViewModel.setStore(store);
                storePicker.dismiss();
            }
        });
        binding.billStorePickerChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storePicker.show(getSupportFragmentManager(), StorePicker.TAG);
            }
        });
        /**************** tags picker *****************/
        TagPicker tagPicker = new TagPicker();
        tagPicker.setOnPickerConfirmClickListener(new BaseMaterialDialog.OnPickerConfirmClickListener<List<Tag>>() {
            @Override
            public void onClick(List<Tag> result) {
                billViewModel.setTags(result);
            }
        });
        binding.billPickTagsChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagPicker.show(getSupportFragmentManager(), TagPicker.TAG);
            }
        });


    }

    private void viewModelObserve() {
        /******************** 获取默认账本、账户、角色 ***********************/
        configViewModel.getSelectedLeger().observe(this, new Observer<Leger>() {
            @Override
            public void onChanged(Leger leger) {
                billViewModel.setLeger(leger);
            }
        });
        configViewModel.getSelectedAccount().observe(this, new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                billViewModel.setSrcAccount(account);
            }
        });
        configViewModel.getSelectedRole().observe(this, new Observer<Role>() {
            @Override
            public void onChanged(Role role) {
                billViewModel.setRole(role);
            }
        });
        /****************** remark 两个窗口 同步 ********************/
        billViewModel.getRemark().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.billRemark.setText(s);
            }
        });
    }

    /**
     * 键盘初始化
     */
    private void calculaterConfig() {
        binding.key0.setOnClickListener(calculatorKeyPressListener);
        binding.key1.setOnClickListener(calculatorKeyPressListener);
        binding.key2.setOnClickListener(calculatorKeyPressListener);
        binding.key3.setOnClickListener(calculatorKeyPressListener);
        binding.key4.setOnClickListener(calculatorKeyPressListener);
        binding.key5.setOnClickListener(calculatorKeyPressListener);
        binding.key6.setOnClickListener(calculatorKeyPressListener);
        binding.key7.setOnClickListener(calculatorKeyPressListener);
        binding.key8.setOnClickListener(calculatorKeyPressListener);
        binding.key9.setOnClickListener(calculatorKeyPressListener);
        binding.keyDot.setOnClickListener(calculatorKeyPressListener);
        binding.keyPlus.setOnClickListener(calculatorKeyPressListener);
        binding.keyMinus.setOnClickListener(calculatorKeyPressListener);
        binding.keyMultiply.setOnClickListener(calculatorKeyPressListener);
        binding.keyDivide.setOnClickListener(calculatorKeyPressListener);
        binding.keyEqual.setOnClickListener(calculatorKeyPressListener);
        binding.keyAllClear.setOnClickListener(calculatorKeyPressListener);
        binding.keyBackspace.setOnClickListener(calculatorKeyPressListener);
        binding.keyRecordAgain.setOnClickListener(calculatorKeyPressListener);


        /***************** calculate full dialog ***************/
        //计算器 dialog
        binding.billCalculateIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 全屏计算器
            }
        });
    }


    public class BillFragmentAdapter extends FragmentStateAdapter {
        private List<String> tabTitles = new ArrayList<>();

        public BillFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);

            tabTitles.add("支出");
            tabTitles.add("收入");
            tabTitles.add("转账");
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            //depend on position create fragment
            //0-outcome; 1-income; 2-transfer
            //tow func: add,edit
            switch (position) {
                case 0:
                    return CategoryFragment.newInstance(Category.TYPE_EXPENDITURE, null);
                case 1:
                    return CategoryFragment.newInstance(Category.TYPE_INCOME, null);
                default:
                    return TransferFragment.newInstance();
            }
        }

        @Override
        public int getItemCount() {
            return tabTitles.size();
        }
    }

    private void save() {

        Log.d(TAG, "save: 保存到数据库");
        int billType = billViewModel.getBillType().getValue();

        Bill bill = new Bill();
        bill.setAmount(billViewModel.getAmount().getValue());
        bill.setBillType(billType);

        Long srcAmount = billViewModel.getSrcAccount().getBalance();


        if (billType == Bill.EXPENDITURE) {
            bill.setCategoryId(billViewModel.getCategoryExpenditure().getValue().getId());
            bill.setCategoryTitle(billViewModel.getCategoryExpenditure().getValue().getTitle());
            bill.setCategoryIconResName(billViewModel.getCategoryExpenditure().getValue().getIconResName());

            billViewModel.getSrcAccount().setBalance(srcAmount -= bill.getAmount());
        } else if (billType == Bill.INCOME) {
            bill.setCategoryId(billViewModel.getCategoryIncome().getValue().getId());
            bill.setCategoryTitle(billViewModel.getCategoryIncome().getValue().getTitle());
            bill.setCategoryIconResName(billViewModel.getCategoryIncome().getValue().getIconResName());

            billViewModel.getSrcAccount().setBalance(srcAmount += bill.getAmount());
        } else {
            //转账 - 设置目标账户
            bill.setTarAccountId(billViewModel.getTarAccount().getId());
            bill.setTarAccountTitle(billViewModel.getTarAccount().getTitle());

            billViewModel.getSrcAccount().setBalance(srcAmount -= bill.getAmount());

            Long tarAmount = billViewModel.getTarAccount().getBalance();
            billViewModel.getTarAccount().setBalance(tarAmount += bill.getAmount());
        }
        //保存账户更改
        accountViewModel.update(billViewModel.getSrcAccount());
        if (billType == Bill.TRANSFER) {
            accountViewModel.update(billViewModel.getTarAccount());
        }

        //获取选中的账本
        bill.setLegerId(billViewModel.getLeger().getId());
        bill.setLegerTitle(billViewModel.getLeger().getTitle());

        //获取选中的角色
        bill.setRoleId(billViewModel.getRole().getId());
        bill.setRoleTitle(billViewModel.getRole().getTitle());

        //获取选中的账户
        bill.setAccountId(billViewModel.getSrcAccount().getId());
        bill.setAccountTitle(billViewModel.getSrcAccount().getTitle());
        bill.setCurrencyCode(billViewModel.getSrcAccount().getCurrencyCode());


        //获取标签
        bill.setTags(billViewModel.getTags().getValue());

        //设置商户
        if (billType == Bill.EXPENDITURE && billViewModel.getStore() != null) {
            bill.setStoreId(billViewModel.getStore().getId());
            bill.setStoreTitle(billViewModel.getStore().getTitle());
        }


        //统计信息
        if (billType != Bill.TRANSFER) {
            //记录收支
            bill.setIncomeExpenditureIncluded(billViewModel.getIncomeExpenditureIncluded());    //收付款？
//        预算
            bill.setBudgetIncluded(billViewModel.getBudgetIncluded());
        }

        if (billType == Bill.EXPENDITURE) {
            //是否退款
            bill.setRefund(binding.billRefundCheckChip.isChecked());
            //报销
            bill.setReimburse(binding.billReimburseCheckChip.isChecked());
        }


        //其他设置
        bill.setDate(billViewModel.getDate());
        bill.setTime(billViewModel.getTime());
        if (billViewModel.getRemark().getValue().isEmpty()) {
            billViewModel.setRemark(binding.billRemark.getText().toString());
        }
        bill.setRemark(billViewModel.getRemark().getValue());
        bill.setCreateTime(new Timestamp(System.currentTimeMillis()));


        if (billViewModel.getImagePaths() == null || billViewModel.getImagePaths().getValue().isEmpty()) {
            //没有选择图片
            billViewModel.insert(bill);
        } else {
            //选择了图片
            ListenableFuture<Boolean> pathLF = FileUtil.saveImagesToInternalStorage(this, billViewModel.getImagePaths().getValue());
            Futures.addCallback(pathLF, new FutureCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    bill.setImagePaths(billViewModel.getImagePaths().getValue());
                    billViewModel.insert(bill);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(TAG, "onFailure: 保存图片失败");
                }
            }, AsyncProcessor.getInstance().getExecutorService());
        }

    }

    private Double calculate(String expression) {
        Double res = null;
        try {
            res = (double) new ScriptEngineManager().getEngineByName("rhino").eval(expression);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return res;
    }

    private void clearResultBoard() {
        expression = new StringBuilder();
        resultBoardText = new StringBuilder();
        binding.resultBoardText.setHint(ResourceUtils.getString(R.string.zero_hint));
    }

}