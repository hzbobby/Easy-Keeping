package com.vividbobo.easy.ui.bill;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
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
import com.vividbobo.easy.BaseActivity;
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.database.model.Store;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.databinding.ActivityBillBinding;
import com.vividbobo.easy.ui.account.AccountPicker;
import com.vividbobo.easy.ui.common.BaseMaterialDialog;
import com.vividbobo.easy.ui.currency.CurrencyPicker;
import com.vividbobo.easy.ui.leger.LegerActivity;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.role.RolePicker;
import com.vividbobo.easy.ui.store.StorePicker;
import com.vividbobo.easy.ui.tags.TagPicker;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.utils.ToastUtil;
import com.vividbobo.easy.viewmodel.BillViewModel;

import java.sql.Date;
import java.sql.Time;
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
                    //finish();
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

    private void save() {
        //TODO save to database
        Log.d(TAG, "save: 保存到数据库");
        Bill bill = new Bill();
        bill.setAmount(billViewModel.getAmount().getValue());
        if (billViewModel.getBillType().getValue() == Bill.EXPENDITURE) {
            bill.setCategoryId(billViewModel.getCategoryExpenditure().getValue().getId());
            bill.setCategoryTitle(billViewModel.getCategoryExpenditure().getValue().getTitle());
            bill.setCategoryIconResName(billViewModel.getCategoryExpenditure().getValue().getIconResName());
        } else {
            bill.setCategoryId(billViewModel.getCategoryIncome().getValue().getId());
            bill.setCategoryTitle(billViewModel.getCategoryIncome().getValue().getTitle());
            bill.setCategoryIconResName(billViewModel.getCategoryIncome().getValue().getIconResName());
        }
        bill.setLegerId(billViewModel.getSelectedLeger().getValue().getId());
        bill.setLegerTitle(billViewModel.getSelectedLeger().getValue().getTitle());
        bill.setRoleId(billViewModel.getSelectedRole().getValue().getId());
        bill.setRoleTitle(billViewModel.getSelectedRole().getValue().getTitle());
        bill.setAccountId(billViewModel.getSelectedAccount().getValue().getId());
        bill.setAccountTitle(billViewModel.getSelectedAccount().getValue().getTitle());
        bill.setTags(billViewModel.getTags().getValue());
//        bill.setImagePaths();
//        bill.setCurrencyCode();
        //isIncomeExpenditureIncluded
        bill.setDate(billViewModel.getDate().getValue());
        bill.setTime(billViewModel.getTime().getValue());
        bill.setRemark(billViewModel.getRemark().getValue());
        bill.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        bill.setStoreId(billViewModel.getStore().getValue().getId());
        bill.setStoreTitle(billViewModel.getStore().getValue().getTitle());


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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        billViewModel = new ViewModelProvider(this).get(BillViewModel.class);


        //toolbar
        binding.billToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.billToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.bill_template:
                        return true;
                    default:
                        return false;
                }
            }
        });

        //set view pager adapter
        BillFragmentAdapter fragmentAdapter = new BillFragmentAdapter(this);
        binding.billViewPager.setAdapter(fragmentAdapter);
        binding.billViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int expenditureVisible = (position == 1 || position == 2) ? View.GONE : View.VISIBLE;
                int transferVisible = position == 2 ? View.GONE : View.VISIBLE;

                binding.billStorePickerChip.setVisibility(expenditureVisible);
                binding.billPickAccountChip.setVisibility(transferVisible);
                binding.billRefundCheckChip.setVisibility(expenditureVisible);
                binding.billReimburseCheckChip.setVisibility(expenditureVisible);
                binding.billReceivePaymentChip.setVisibility(expenditureVisible);
            }
        });
        //viewpager bind with tab layout
        new TabLayoutMediator(binding.billTabLayout, binding.billViewPager, true, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(fragmentAdapter.tabTitles.get(position));
            }
        }).attach();

        observeInit();
        calculatorBoardInit();
        remarkBoardInit();
        optionalInit();

//        //底部键盘拖拽隐藏/显示
//        GestureDetector cardGestureDetector = new GestureDetector(this, new MyGestureListener());
//        binding.billCardBoard.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                cardGestureDetector.onTouchEvent(event);
//                return true;
//            }
//        });
    }


    private boolean isKeyBoardExpand = true;

    private void expandedBottomKeyBoard() {
        final View calView = findViewById(R.id.keyBoardView);

        float translationY = isKeyBoardExpand ? calView.getHeight() : -calView.getHeight();
        calView.animate()
                .translationYBy(translationY)
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        isKeyBoardExpand = !isKeyBoardExpand;
    }


    private void photoPickerInit() {
        // Registers a photo picker activity launcher in single-select mode.


    }

    private void optionalInit() {
        boolean[] statisticsCheckItems = new boolean[2];
        AlertDialog statisticsPicker = new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                .setTitle(R.string.pick_statistics)
                .setMultiChoiceItems(R.array.statistics_items, statisticsCheckItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        binding.billStatisticsChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsPicker.show();
            }
        });

//        //currencyPicker
//        CurrencyPicker currencyPicker = new CurrencyPicker();
//        currencyPicker.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, Object item, int position) {
//                Currency currency = (Currency) item;
//                billViewModel.setCurrencyCode(currency.getCode());
//            }
//        });
        binding.billCalculateIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                currencyPicker.show(getSupportFragmentManager(), CurrencyPicker.TAG);
                //计算器

            }
        });

        //imagePicker
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(9), uris -> {
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            if (!uris.isEmpty()) {
                Log.d("PhotoPicker", "Number of items selected: " + uris.size());
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });
        //编译器报错bug，采用 activityx.1.7以上就不会
        ActivityResultContracts.PickVisualMedia.VisualMediaType mediaType = (ActivityResultContracts.PickVisualMedia.VisualMediaType) ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE;
        PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                .setMediaType(mediaType)
                .build();

        binding.billPhotoPickerChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMedia.launch(request);
            }
        });

        //日期
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(ResourceUtils.getString(R.string.pick_date))
                .build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                billViewModel.setDate(new Date((Long) selection));
            }
        });
        binding.billDatePickerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(), "BillDatePickers");
            }
        });
        //账本
        binding.billPickLegerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillActivity.this, LegerActivity.class);
                startActivity(intent);
            }
        });
        //时间
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTitleText(ResourceUtils.getString(R.string.pick_time))
                .build();
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

        //账户
        AccountPicker accountPicker = new AccountPicker();
        binding.billPickAccountChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: account chip");
                accountPicker.show(getSupportFragmentManager(), AccountPicker.TAG);
            }
        });

        //角色
        RolePicker rolePicker = new RolePicker();
        rolePicker.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Role role = (Role) item;
                billViewModel.setRole(role);
                ToastUtil.makeToast(role.getTitle());
                rolePicker.dismiss();
            }
        });
        binding.billRolePickerChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rolePicker.show(getSupportFragmentManager(), RolePicker.TAG);
            }
        });

        //商店
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


        //标签
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

    private void observeInit() {
        billViewModel.getRemark().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.billRemark.setText(s);
            }
        });
    }

    private void remarkBoardInit() {
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
     * 键盘初始化
     */
    private void calculatorBoardInit() {
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

    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG, "onFling Detected");
            if (e1 == null || e2 == null) {
                return false;
            }

            Log.d(TAG, "onFling1:" + Math.abs(e1.getX() - e2.getX()));
            Log.d(TAG, "onFling2:" + Math.abs(e1.getY() - e2.getY()));

            // 判断垂直滑动
            if (Math.abs(e1.getX() - e2.getX()) < Math.abs(e1.getY() - e2.getY())) {
                Log.d(TAG, "onFling: " + (e1.getY() - e2.getY()));
                if (e1.getY() - e2.getY() > 100 && Math.abs(velocityY) > 200) {
                    // 向上滑动
                    if (!isKeyBoardExpand) {
                        expandedBottomKeyBoard();
                    }
                    return true;
                } else if (e2.getY() - e1.getY() > 100 && Math.abs(velocityY) > 200) {
                    // 向下滑动
                    if (isKeyBoardExpand) {
                        expandedBottomKeyBoard();
                    }
                    return true;
                }
            }

            return false;
        }
    }

}