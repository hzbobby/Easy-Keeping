package com.vividbobo.easy.ui.bill;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.vividbobo.easy.BaseActivity;
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Payee;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.databinding.ActivityBillBinding;
import com.vividbobo.easy.ui.account.AccountPicker;
import com.vividbobo.easy.ui.common.BaseMaterialDialog;
import com.vividbobo.easy.ui.leger.LegerActivity;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.role.RolePicker;
import com.vividbobo.easy.ui.store.StorePicker;
import com.vividbobo.easy.ui.tags.TagPicker;
import com.vividbobo.easy.utils.ConstantValue;
import com.vividbobo.easy.utils.FormatUtils;
import com.vividbobo.easy.utils.ImageUtils;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.utils.ToastUtil;
import com.vividbobo.easy.viewmodel.BillViewModel;
import com.vividbobo.easy.viewmodel.ConfigViewModel;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.script.ScriptEngineManager;

/***
 * TODO 8.选择账户，该账户没有扣款
 * 9.bill activity -> images use carousel in bottom view
 * 10.chip长按取消设置
 * 11.怎么获取初始Account 和 编辑的 Account
 * 12.设置编辑时，初始Category选中
 * 13.貌似跟初始时的那个category冲突了
 * 14.编辑保存
 */
public class BillActivity extends BaseActivity {
    private static final String TAG = "BillActivity";
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 0x1001;
    public static final String KEY_DATA_BILL = "editBill";
    private ActivityBillBinding binding;
    private BillViewModel billViewModel;
    private ConfigViewModel configViewModel;
    private StringBuilder expression = new StringBuilder();
    private StringBuilder resultBoardText = new StringBuilder();
    private List<Uri> selectedImageUris;
    private final ActivityResultLauncher<PickVisualMediaRequest> imagePickerLauncher = registerForActivityResult(new PickMultipleVisualMediaWithReadPermission(), uris -> {
        if (!uris.isEmpty()) {
            billViewModel.setImageUris(uris);
        } else {
        }
    });


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
//                    if (bill2save == null) {
//                        save();
//                    } else {
//                        update();
//                    }
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
                    billViewModel.setBillAmount((long) (res * 100));
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


    private MaterialTimePicker timePicker;
    private MaterialDatePicker<Long> datePicker;
    private boolean[] statisticsCheckItems = new boolean[2];
    private BillFragmentAdapter fragmentAdapter;
    private Leger billLeger;
    private Date billDate;
    private LocalTime billTime;
    private Role billRole;
    private Payee billPayee;
    private Account billAccount;
    private Account billTarAccount;
    private List<Tag> billTags;
    private List<String> billImages;
    private Boolean billRefund;
    private Boolean billReimburse;
    private Boolean billInExp;
    private Boolean billBudget;
    private Category expCategory;
    private Category inCategory;
    private int billType;
    private Long billAmount;
    private Account lastSelectedAccount;
    private Long billId;
    private String billRemark;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        billViewModel = new ViewModelProvider(this).get(BillViewModel.class);
        configViewModel = new ViewModelProvider(this).get(ConfigViewModel.class);


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
//                    case R.id.bill_template:
//                        templateSheetDialog.show(getSupportFragmentManager(), BillTemplateSheetDialog.TAG);
//                        return true;
                    default:
                        return false;
                }
            }
        });

        /******************* tab layout & view pager2 **********************/
        fragmentAdapter = new BillFragmentAdapter(this);
        binding.billViewPager.setAdapter(fragmentAdapter);
        binding.billViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int expenditureVisible = (position == 1 || position == 2) ? View.GONE : View.VISIBLE;
                int transferVisible = position == 2 ? View.GONE : View.VISIBLE;
                switch (position) {
                    case 0:
                        billType = Bill.EXPENDITURE;
                        billViewModel.setBillType(Bill.EXPENDITURE);
                        break;
                    case 1:
                        billType = Bill.INCOME;

                        billViewModel.setBillType(Bill.INCOME);
                        break;
                    default:
                        billType = Bill.TRANSFER;

                        billViewModel.setBillType(Bill.TRANSFER);
                }

                binding.billStorePickerChip.setVisibility(expenditureVisible);
                binding.billPickAccountChip.setVisibility(transferVisible);
                binding.billRefundCheckChip.setVisibility(expenditureVisible);
                binding.billReimburseCheckChip.setVisibility(expenditureVisible);
                binding.billBudgetChip.setVisibility(transferVisible);
                binding.billInExpChip.setVisibility(transferVisible);
//                binding.billStatisticsChip.setVisibility(transferVisible);
//                binding.billReceivePaymentChip.setVisibility(expenditureVisible);
            }
        });
        //viewpager bind with tab layout
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.billTabLayout, binding.billViewPager, true, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(fragmentAdapter.tabTitles.get(position));
            }
        });
        tabLayoutMediator.attach();


        configDataInitial();
        pickerInitial();
        remarkConfig();
        calculaterConfig();
        pickersConfig();
        viewModelObserve();
        chipConfig();

        /***************** edit init **********************/
        configEdit();
    }

    private void chipConfig() {

        binding.billRefundCheckChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                billViewModel.setRefundChecked(isChecked);
            }
        });
        binding.billReimburseCheckChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                billViewModel.setReimburseChecked(isChecked);
            }
        });
        binding.billInExpChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                billViewModel.setInExpIncludedChecked(isChecked);
            }
        });
        binding.billBudgetChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                billViewModel.setBudgetIncludedChecked(isChecked);
            }
        });
    }

    private void configDataInitial() {
        billViewModel.setImageUris(new ArrayList<>());
        billViewModel.setBillDate(new Date(System.currentTimeMillis()));
        billViewModel.setBillTime(LocalTime.now());
        billViewModel.setFilterIncCategoryId(24);
        billViewModel.setFilterExpCategoryId(1);
    }

    /**
     * 编辑时，初始化数据
     */
    private void configEdit() {
        if (Objects.isNull(getIntent())) {
            return;
        }
        Bill editBill = (Bill) getIntent().getSerializableExtra(KEY_DATA_BILL);
        if (Objects.isNull(editBill)) {
            return;
        }
        // eidtBill is not null, then initial the data for UI
        // change viewpager
        binding.billViewPager.setCurrentItem(editBill.getBillType(), false);
        billId = editBill.getId();

        // set chosen category
        Integer chosenCategoryId = editBill.getCategoryId();
        if (editBill.getBillType() == Bill.EXPENDITURE) {
            billViewModel.setFilterExpCategoryId(chosenCategoryId);
        } else if (editBill.getBillType() == Bill.INCOME) {
            billViewModel.setFilterIncCategoryId(chosenCategoryId);
        }

        //config expression
        expression.append(FormatUtils.getAmount(editBill.getAmount()));

        // set amount
        billViewModel.setBillAmount(editBill.getAmount());
        //set leger, then it will change selectedLeger
        billViewModel.setChosenLegerId(editBill.getLegerId());
        // set date
        billViewModel.setBillDate(editBill.getDate());
        // set time
        billViewModel.setBillTime(editBill.getTime());
        // set role
        billViewModel.setChosenRoleId(editBill.getRoleId());
        // set payee
        billViewModel.setChosenPayeeId(editBill.getPayeeId());
        // set account
        billViewModel.setLastSelectedAccountId(editBill.getAccountId());
        // set tags
        billViewModel.setChosenTags(editBill.getTags());
        // set images
        billViewModel.setBillImages(editBill.getImagePaths());
        // set refund
        billViewModel.setRefundChecked(editBill.getRefund());
        // set reimburse
        billViewModel.setReimburseChecked(editBill.getReimburse());
        // set in.. and exp.. included
        billViewModel.setInExpIncludedChecked(editBill.getIncomeExpenditureIncluded());
        // set budget
        billViewModel.setBudgetIncludedChecked(editBill.getBudgetIncluded());
        // set tar account
        billViewModel.setChosenTarAccountId(editBill.getTarAccountId());
        // set remark
        billViewModel.setRemark(editBill.getRemark());
    }

    private void pickerInitial() {
        timePicker = new MaterialTimePicker.Builder().setTitleText(ResourceUtils.getString(R.string.pick_time)).build();
        initialDatePicker(null);
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


    private void openGallery() {
        imagePickerLauncher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    }

    /**
     * 顶部chip bar 的一些相关配置：点击事件、...
     */
    private void pickersConfig() {

        /***************** image picker *******************/
        //image viewer sheet
        ImageViewerSheet imageViewerSheet = ImageViewerSheet.newInstance();
        imageViewerSheet.setOnFooterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        binding.billPhotoPickerChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if 选择了附件，进入图片查看器 bottom sheet？
                //else 进行选择图片

                if (Objects.nonNull(billViewModel.getImageUris()) && !billViewModel.getImageUris().getValue().isEmpty()) {  //查看图片
                    imageViewerSheet.show(getSupportFragmentManager(), ImageViewerSheet.TAG);
                } else {
                    //选择图片
                    openGallery();
                }
            }
        });

        /******************** date picker **********************/
        //日期
        binding.billDatePickerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(), "BillDatePickers");
            }
        });
        /******************** local time picker ***********************/
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billViewModel.setBillTime(LocalTime.of(timePicker.getHour(), timePicker.getMinute()));
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
                billViewModel.setLastSelectedAccountId(account.getId());
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
                billViewModel.setChosenRoleId(role.getId());
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
                Payee payee = (Payee) item;
                billViewModel.setChosenPayeeId(payee.getId());
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
                billViewModel.setChosenTags(result);
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
        billViewModel.getBillAmount().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                billAmount = aLong;
                binding.resultBoardText.setText(FormatUtils.getAmount(aLong));
            }
        });
        billViewModel.getInputRemark().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.billRemark.setText(s);
            }
        });
        billViewModel.getChosenLeger().observe(this, new Observer<Leger>() {

            @Override
            public void onChanged(Leger leger) {
                billLeger = leger;
            }
        });
        billViewModel.getBillDate().observe(this, new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                billDate = date;
            }
        });
        billViewModel.getBillTime().observe(this, new Observer<LocalTime>() {
            @Override
            public void onChanged(LocalTime localTime) {
                billTime = localTime;
            }
        });
        billViewModel.getChosenRole().observe(this, new Observer<Role>() {
            @Override
            public void onChanged(Role role) {
                billRole = role;
            }
        });
        billViewModel.getChosenPayee().observe(this, new Observer<Payee>() {
            @Override
            public void onChanged(Payee payee) {
                if (payee != null) {
                    Log.d(TAG, "onChanged: payee");
                }
                billPayee = payee;
            }
        });


        billViewModel.getChosenTags().observe(this, new Observer<List<Tag>>() {
            @Override
            public void onChanged(List<Tag> tags) {
                billTags = tags;
            }
        });
        billViewModel.getBillImages().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                billImages = strings;
            }
        });
        billViewModel.getRefundChecked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                billRefund = aBoolean;
                binding.billRefundCheckChip.setChecked(aBoolean);
            }
        });
        billViewModel.getReimburseChecked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                billReimburse = aBoolean;
                binding.billReimburseCheckChip.setChecked(aBoolean);
            }
        });
        billViewModel.getInExpIncludedChecked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                billInExp = aBoolean;
                binding.billInExpChip.setChecked(aBoolean);
            }
        });
        billViewModel.getBudgetIncludedChecked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                billBudget = aBoolean;
                binding.billBudgetChip.setChecked(aBoolean);
            }
        });
        billViewModel.getChosenExpCategory().observe(this, new Observer<Category>() {
            @Override
            public void onChanged(Category category) {
                expCategory = category;
            }
        });
        billViewModel.getChosenInCategory().observe(this, new Observer<Category>() {
            @Override
            public void onChanged(Category category) {
                inCategory = category;
            }
        });

        billViewModel.getLastSelectedAccount().observe(this, new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                if (account != null) {
                    Log.d(TAG, "onChanged: lastSelectedAccount: " + account.getTitle());
                }
                lastSelectedAccount = account;
            }
        });

        billViewModel.getSelectedLeger().observe(this, new Observer<Leger>() {
            @Override
            public void onChanged(Leger leger) {
                billLeger = leger;
            }
        });
        billViewModel.getImageUris().observe(this, new Observer<List<Uri>>() {
            @Override
            public void onChanged(List<Uri> uris) {
                selectedImageUris = uris;
            }
        });

        /********** config *********/

    }

    //初始化 date picker
    private void initialDatePicker(Date date) {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker().setTitleText("选择日期");
        if (Objects.nonNull(date)) builder.setSelection(date.getTime());
        datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener) selection -> billViewModel.setBillDate(new Date((Long) selection)));
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
        private List<Fragment> fragments = new ArrayList<>();

        public BillFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);

            tabTitles.add("支出");
            tabTitles.add("收入");
            tabTitles.add("转账");
            fragments.add(CategoryFragment.newInstance(Category.TYPE_EXPENDITURE));
            fragments.add(CategoryFragment.newInstance(Category.TYPE_INCOME));
            fragments.add(TransferFragment.newInstance());
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            //depend on position create fragment
            //0-outcome; 1-income; 2-transfer
            //tow func: add,edit
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return tabTitles.size();
        }

    }

    private void save() {


        Category category = null;
        if (billType == Bill.EXPENDITURE) {
            category = expCategory;
        } else {
            category = inCategory;
        }
        Bill bill = Bill.createBill(billType, billAmount, billLeger);

        if (billType == Bill.TRANSFER) {
            if (Objects.isNull(billViewModel.getSrcAccount())) {
                ToastUtil.makeToast("请选择转出账户");
            }
            if (Objects.isNull(billViewModel.getTarAccount())) {
                ToastUtil.makeToast("请选择转入账户");
            }

            bill.setAccount(billViewModel.getSrcAccount());
            bill.setTarAccount(billViewModel.getTarAccount());
        } else {
            if (billType == Bill.EXPENDITURE) {
                bill.setRefund(billRefund);
                bill.setReimburse(billReimburse);
                bill.setPayee(billPayee);
            }
            bill.setCategory(category);
            bill.setIncomeExpenditureIncluded(billInExp);
            bill.setBudgetIncluded(billBudget);
            bill.setAccount(lastSelectedAccount);
        }

        bill.setRole(billRole);
        bill.setTags(billTags);
        bill.setImagePaths(billImages);
        bill.setDate(billDate);
        bill.setTime(billTime);
        bill.setRemark(billViewModel.getInputRemark().getValue());

        if (Objects.nonNull(selectedImageUris) && !selectedImageUris.isEmpty()) {
            List<String> imagePaths = new ArrayList<>();
            Timestamp timestamp = bill.getCreateTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestampStr = sdf.format(timestamp);
            for (int i = 0; i < selectedImageUris.size(); i++) {
                Uri uri = selectedImageUris.get(i);
                String fileName = "bill_" + timestampStr + "_" + i;
                String path = ImageUtils.saveImageToAppPrivateFolder(this, uri, fileName, ConstantValue.PRIVATE_DIR_IMAGES);
                imagePaths.add(path);
            }
            bill.setImagePaths(imagePaths);
        }
        Log.d(TAG, "save: " + bill.toString());

        if (Objects.nonNull(billId)) {
            bill.setId(billId);
        }
        billViewModel.save(bill);
    }


    private Double calculate(String expression) {
        Double res = null;
        try {
            res = (double) new ScriptEngineManager().getEngineByName("rhino").eval(expression);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private void clearResultBoard() {
        expression = new StringBuilder();
        resultBoardText = new StringBuilder();
        binding.resultBoardText.setHint(ResourceUtils.getString(R.string.zero_hint));
    }


    public class PickMultipleVisualMediaWithReadPermission extends ActivityResultContract<PickVisualMediaRequest, List<Uri>> {

        private static final int MAX_SELECTION_COUNT = 9;

        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, PickVisualMediaRequest input) {


            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            String mimeType;
//            if (input.getMediaType() instanceof PickVisualMedia.ImageOnly) {
            mimeType = "image/*";
//            } else if (input.getMediaType() instanceof PickVisualMedia.VideoOnly) {
//                mimeType = "video/*";
//            } else {
//                mimeType = "*/*";
//            }

            intent.setType(mimeType);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            return intent;
        }

        @Override
        public List<Uri> parseResult(int resultCode, @Nullable Intent intent) {
            List<Uri> result = new ArrayList<>();
            if (resultCode == Activity.RESULT_OK && intent != null) {
                if (intent.getClipData() != null) {
                    int count = Math.min(intent.getClipData().getItemCount(), MAX_SELECTION_COUNT);
                    for (int i = 0; i < count; i++) {
                        result.add(intent.getClipData().getItemAt(i).getUri());
                    }
                } else if (intent.getData() != null) {
                    result.add(intent.getData());
                }
            }
            return result;
        }
    }
}