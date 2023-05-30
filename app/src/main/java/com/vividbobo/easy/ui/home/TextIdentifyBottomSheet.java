package com.vividbobo.easy.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.databinding.LayoutVoiceBillBinding;
import com.vividbobo.easy.googleml.ner.GoogleBillExtractor;
import com.vividbobo.easy.ui.bill.MoreCategoryBottomSheet;
import com.vividbobo.easy.ui.common.BottomSheetDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.utils.SharedPrefsUtils;
import com.vividbobo.easy.viewmodel.TextIdentifyViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TextIdentifyBottomSheet extends BottomSheetDialog<LayoutVoiceBillBinding> {
    public static final String TAG = "TextIdentifyBottomSheet";
    private GoogleBillExtractor googleEntityExtractor;
    private TextIdentifyViewModel textIdentifyViewModel;
    private Category selectedCategory;
    private Leger lastLeger;
    private Account lastAccount;
    private Category expCategory;
    private Category inCategory;
    private Map<String, String> categoryMapping;

    public static TextIdentifyBottomSheet newInstance() {

        Bundle args = new Bundle();

        TextIdentifyBottomSheet fragment = new TextIdentifyBottomSheet();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public LayoutVoiceBillBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return LayoutVoiceBillBinding.inflate(inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textIdentifyViewModel = new ViewModelProvider(this).get(TextIdentifyViewModel.class);
        categoryMapping = SharedPrefsUtils.loadMap(getContext(), SharedPrefsUtils.SHARE_PREFS_CATEGORY_MAPPING);

        if (categoryMapping.size() == 0) {
            Map<String, String> categoryMap = new HashMap<String, String>();
            // 餐饮
            categoryMap.put("热干面", "餐饮");
            categoryMap.put("可乐", "餐饮");
            categoryMap.put("咖啡", "餐饮");
            categoryMap.put("披萨", "餐饮");
            categoryMap.put("汉堡", "餐饮");

            // 服饰
            categoryMap.put("衬衫", "服饰");
            categoryMap.put("裤子", "服饰");
            categoryMap.put("裙子", "服饰");
            categoryMap.put("鞋子", "服饰");
            categoryMap.put("帽子", "服饰");

            // 日用
            categoryMap.put("洗发水", "日用");
            categoryMap.put("肥皂", "日用");
            categoryMap.put("牙膏", "日用");
            categoryMap.put("毛巾", "日用");
            categoryMap.put("洗衣液", "日用");

            // 数码
            categoryMap.put("手机", "数码");
            categoryMap.put("电脑", "数码");
            categoryMap.put("相机", "数码");
            categoryMap.put("耳机", "数码");
            categoryMap.put("平板", "数码");

            // 美妆
            categoryMap.put("口红", "美妆");
            categoryMap.put("眼影", "美妆");
            categoryMap.put("粉底", "美妆");
            categoryMap.put("睫毛膏", "美妆");
            categoryMap.put("卸妆油", "美妆");

            // 应用软件
            categoryMap.put("游戏", "应用软件");
            categoryMap.put("社交软件", "应用软件");
            categoryMap.put("学习软件", "应用软件");
            categoryMap.put("音乐软件", "应用软件");
            categoryMap.put("视频软件", "应用软件");

            // 住房
            categoryMap.put("租金", "住房");
            categoryMap.put("房贷", "住房");
            categoryMap.put("物业费", "住房");
            categoryMap.put("电费", "住房");
            categoryMap.put("水费", "住房");

            // 交通
            categoryMap.put("公交", "交通");
            categoryMap.put("地铁", "交通");
            categoryMap.put("的士", "交通");
            categoryMap.put("火车", "交通");
            categoryMap.put("飞机", "交通");

            // 娱乐
            categoryMap.put("电影", "娱乐");
            categoryMap.put("音乐会", "娱乐");
            categoryMap.put("游乐园", "娱乐");
            categoryMap.put("体育赛事", "娱乐");
            categoryMap.put("桌游", "娱乐");
            // 医疗
            categoryMap.put("药品", "医疗");
            categoryMap.put("看病", "医疗");
            categoryMap.put("手术", "医疗");
            categoryMap.put("挂号", "医疗");
            categoryMap.put("体检", "医疗");

// 通讯
            categoryMap.put("电话费", "通讯");
            categoryMap.put("网络费", "通讯");
            categoryMap.put("话费", "通讯");
            categoryMap.put("短信", "通讯");
            categoryMap.put("流量", "通讯");

// 学习
            categoryMap.put("教材", "学习");
            categoryMap.put("课程", "学习");
            categoryMap.put("学费", "学习");
            categoryMap.put("培训", "学习");
            categoryMap.put("考试", "学习");

// 办公
            categoryMap.put("办公用品", "办公");
            categoryMap.put("打印", "办公");
            categoryMap.put("笔记本", "办公");
            categoryMap.put("文件夹", "办公");
            categoryMap.put("文具", "办公");

// 运动
            categoryMap.put("健身", "运动");
            categoryMap.put("游泳", "运动");
            categoryMap.put("球类", "运动");
            categoryMap.put("跑步", "运动");
            categoryMap.put("瑜伽", "运动");

// 社交
            categoryMap.put("聚会", "社交");
            categoryMap.put("餐饮", "社交");
            categoryMap.put("娱乐", "社交");
            categoryMap.put("礼物", "社交");
            categoryMap.put("旅行", "社交");

// 人情
            categoryMap.put("礼物", "人情");
            categoryMap.put("红包", "人情");
            categoryMap.put("请客", "人情");
            categoryMap.put("结婚", "人情");
            categoryMap.put("生日", "人情");

// 育儿
            categoryMap.put("奶粉", "育儿");
            categoryMap.put("尿不湿", "育儿");
            categoryMap.put("儿童用品", "育儿");
            categoryMap.put("玩具", "育儿");
            categoryMap.put("学费", "育儿");

// 宠物
            categoryMap.put("狗粮", "宠物");
            categoryMap.put("猫砂", "宠物");
            categoryMap.put("宠物食品", "宠物");
            categoryMap.put("宠物用品", "宠物");
            categoryMap.put("宠物医疗", "宠物");

// 旅行
            categoryMap.put("机票", "旅行");
            categoryMap.put("酒店", "旅行");
            categoryMap.put("旅游团", "旅行");
            categoryMap.put("旅游景点", "旅行");
            categoryMap.put("旅行用品", "旅行");

// 烟酒
            categoryMap.put("香烟", "烟酒");
            categoryMap.put("啤酒", "烟酒");
            categoryMap.put("白酒", "烟酒");
            categoryMap.put("红酒", "烟酒");
            categoryMap.put("烟草", "烟酒");

            // 会员
            categoryMap.put("会员费", "会员");
            categoryMap.put("年费", "会员");
            categoryMap.put("月费", "会员");
            categoryMap.put("季费", "会员");
            categoryMap.put("续费", "会员");

// 购物
            categoryMap.put("衣物", "购物");
            categoryMap.put("食品", "购物");
            categoryMap.put("家电", "购物");
            categoryMap.put("家具", "购物");
            categoryMap.put("图书", "购物");

// 其他
            categoryMap.put("捐款", "其他");
            categoryMap.put("赠品", "其他");
            categoryMap.put("随机", "其他");
            categoryMap.put("未知", "其他");
            categoryMap.put("其他", "其他");
            Log.d(TAG, "onAppCreateInitial: categoryMapping size: " + categoryMap.size());
            SharedPrefsUtils.saveMap(getContext(), categoryMap, SharedPrefsUtils.SHARE_PREFS_CATEGORY_MAPPING);
            categoryMap = SharedPrefsUtils.loadMap(getContext(), SharedPrefsUtils.SHARE_PREFS_CATEGORY_MAPPING);
        }

    }

    @Override
    public void onViewBinding(LayoutVoiceBillBinding binding) {
        googleEntityExtractor = new GoogleBillExtractor();
        googleEntityExtractor.setOnAnalyzeDoneListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleEntityExtractor.isSuccess()) {

                    String dateStr = googleEntityExtractor.getDate();
                    String amountStr = googleEntityExtractor.getAmount();
                    binding.calendarTv.setText(dateStr);
                    binding.amountTv.setText(amountStr);
                    String categoryName = null;
                    try {
                        categoryName = categoryMapping.get(googleEntityExtractor.getItemName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    textIdentifyViewModel.setFilterCategoryName(categoryName);

                    binding.analyzeResultLayout.setVisibility(View.VISIBLE);
                    binding.saveTv.setVisibility(View.VISIBLE);
                } else {
                    binding.analyzeResultLayout.setVisibility(View.GONE);
                    binding.saveTv.setVisibility(View.GONE);
                }
            }
        });

        binding.calendarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder<Long> datePickerBuilder = MaterialDatePicker.Builder.datePicker().setTitleText("选择日期");
                if (googleEntityExtractor.isSuccess()) {
                    String dateStr = binding.calendarTv.getText().toString();
                    Log.d(TAG, "onClick: datePicker: " + dateStr);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    try {
                        Date date = sdf.parse(dateStr);
                        datePickerBuilder.setSelection(date.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                MaterialDatePicker<Long> datePicker = datePickerBuilder.build();
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        binding.calendarTv.setText(sdf.format(selection));
                    }
                });
                datePicker.show(getParentFragmentManager(), "DatePicker");
            }
        });

        binding.categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreCategoryBottomSheet categoryBottomSheet = MoreCategoryBottomSheet.newInstance(googleEntityExtractor.getBillType());
                categoryBottomSheet.show(getParentFragmentManager(), MoreCategoryBottomSheet.TAG);
                categoryBottomSheet.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, Object item, int position) {
                        selectedCategory = (Category) item;
                        ResourceUtils.bindImageDrawable(getContext(), ResourceUtils.getDrawable(selectedCategory.getIconResName())).centerInside().into(binding.categoryIv);
                        binding.categoryTv.setText(selectedCategory.getTitle());
                        categoryBottomSheet.dismiss();
                    }
                });
            }
        });
        binding.textInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: ");
                googleEntityExtractor.extract(s.toString());
                Log.d(TAG, "onTextChanged: isSuccess" + googleEntityExtractor.isSuccess());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double amount = null;
                try {
                    amount = Double.parseDouble(googleEntityExtractor.getAmount());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                java.sql.Date date = null;
                try {
                    date = new java.sql.Date(sdf.parse(googleEntityExtractor.getDate()).getTime());
                } catch (ParseException e) {
//                    throw new RuntimeException(e);
                    e.printStackTrace();
                }
                if (Objects.isNull(date)) {
                    Toast.makeText(getContext(), "识别日期出错", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Objects.isNull(amount)) {
                    Toast.makeText(getContext(), "识别金额出错", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Objects.isNull(selectedCategory)) {
                    if (googleEntityExtractor.getBillType() == Bill.EXPENDITURE) {
                        selectedCategory = expCategory;
                    } else {
                        selectedCategory = inCategory;
                    }
                }
                if (Objects.nonNull(googleEntityExtractor.getItemName())) {
                    categoryMapping.put(googleEntityExtractor.getItemName(), selectedCategory.getTitle());
                }

                Bill bill = Bill.createBill(googleEntityExtractor.getBillType(), (long) (amount * 100), lastLeger);

                bill.setCategory(selectedCategory);
                bill.setAccount(lastAccount);
                bill.setDate(date);
                bill.setTime(LocalTime.now());
                bill.setRemark(googleEntityExtractor.getRemark());

                textIdentifyViewModel.insert(bill);
                SharedPrefsUtils.saveMap(getContext(), categoryMapping, SharedPrefsUtils.SHARE_PREFS_CATEGORY_MAPPING);
                dismiss();
            }
        });

        configViewModel();
    }

    private void configViewModel() {
        textIdentifyViewModel.getLastAccount().observe(this, new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                lastAccount = account;
            }
        });
        textIdentifyViewModel.getLastLeger().observe(this, new Observer<Leger>() {
            @Override
            public void onChanged(Leger leger) {
                lastLeger = leger;
            }
        });
        textIdentifyViewModel.getExpCategory().observe(this, new Observer<Category>() {
            @Override
            public void onChanged(Category category) {
                expCategory = category;
                if (category != null) {
                    Log.d(TAG, "onChanged: expCategory: " + category.toString());
                    ResourceUtils.bindImageDrawable(getContext(), ResourceUtils.getDrawable(expCategory.getIconResName()))
                            .centerInside().into(binding.categoryIv);
                    binding.categoryTv.setText(expCategory.getTitle());
                }
            }
        });
        textIdentifyViewModel.getInCategory().observe(this, new Observer<Category>() {
            @Override
            public void onChanged(Category category) {
                inCategory = category;
                if (category != null) {
                    Log.d(TAG, "onChanged: inCategory: " + category.toString());
                    ResourceUtils.bindImageDrawable(getContext(), ResourceUtils.getDrawable(inCategory.getIconResName()))
                            .centerInside().into(binding.categoryIv);
                    binding.categoryTv.setText(inCategory.getTitle());
                }
            }
        });

    }
}
