package com.vividbobo.easy.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.vividbobo.easy.database.converter.Converters;
import com.vividbobo.easy.database.converter.EnumConverter;
import com.vividbobo.easy.database.converter.LocalTimeConverter;
import com.vividbobo.easy.database.dao.AccountDao;
import com.vividbobo.easy.database.dao.AccountTypeDao;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.dao.CategoryDao;
import com.vividbobo.easy.database.dao.ConfigDao;
import com.vividbobo.easy.database.dao.CurrencyDao;
import com.vividbobo.easy.database.dao.LegerDao;
import com.vividbobo.easy.database.dao.PayeeDao;
import com.vividbobo.easy.database.dao.ResourceDao;
import com.vividbobo.easy.database.dao.RoleDao;
import com.vividbobo.easy.database.dao.TagDao;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.AccountType;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Payee;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Database(
        entities = {Bill.class, Category.class, Leger.class,
                Account.class, Tag.class, Currency.class, Resource.class,
                Role.class, Config.class, Payee.class, AccountType.class},
        version = 1,
        exportSchema = false)
@TypeConverters({Converters.class, LocalTimeConverter.class, EnumConverter.class})
public abstract class EasyDatabase extends RoomDatabase {


    public abstract LegerDao legerDao();

    public abstract AccountTypeDao accountTypeDao();

    public abstract AccountDao accountDao();

    public abstract TagDao tagDao();

    public abstract CurrencyDao currencyDao();

    public abstract PayeeDao payeeDao();

    public abstract ConfigDao configDao();

    public abstract BillDao billDao();

    public abstract ResourceDao resourceDao();

    public abstract CategoryDao categoryDao();

    public abstract RoleDao roleDao();

    private static volatile EasyDatabase INSTANCE;

    public static EasyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EasyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    EasyDatabase.class, "easy_database.sqlite")
                            .addCallback(resourceInit)
                            .addCallback(categoryInit)
                            .addCallback(currencyInit)
                            .addCallback(accountTypeInit)
                            .addCallback(resourceAccountInit)
                            .addCallback(coverInit)
                            .addCallback(legerInit)
                            .addCallback(accountInit)
                            .addCallback(roleInit)
                            .addCallback(configInit)
                            .build();

                }
            }
        }
        return INSTANCE;
    }


    private static Callback roleInit = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            AsyncProcessor.getInstance().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Role role = new Role();
                    role.setId(1);
                    role.setTitle("自己");
                    INSTANCE.roleDao().insert(role);
                    return null;
                }
            });
        }
    };


    private static Callback configInit = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //初始时的配置
            AsyncProcessor.getInstance().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    ConfigDao dao = INSTANCE.configDao();
                    //默认账户
                    dao.insert(new Config(Config.TYPE_ACCOUNT, 1));
                    //默认账本
                    dao.insert(new Config(Config.TYPE_LEGER, 1));
                    //默认角色
                    dao.insert(new Config(Config.TYPE_ROLE, 1));

                    //自动记账
                    //默认账本
                    dao.insert(new Config(Config.TYPE_AUTO_BILLING_LEGER, 1));
                    //默认账户
                    dao.insert(new Config(Config.TYPE_AUTO_BILLING_ACCOUNT_WECHAT, 2));
                    dao.insert(new Config(Config.TYPE_AUTO_BILLING_ACCOUNT_ALIPAY, 3));

                    dao.insert(new Config(Config.TYPE_IMPORT_ACCOUNT_WECHAT, 2));
                    dao.insert(new Config(Config.TYPE_IMPORT_ACCOUNT_ALIPAY, 3));
                    //默认类别
                    dao.insert(new Config(Config.TYPE_CATEGORY_EXPENDITURE, 1));
                    dao.insert(new Config(Config.TYPE_CATEGORY_INCOME, 24));
                    return null;
                }
            });
        }
    };


    private static Callback legerInit = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            AsyncProcessor.getInstance().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    LegerDao legerDao = INSTANCE.legerDao();
                    legerDao.insert(new Leger(1, "日常账本", "默认账本", "bg1", Resource.ResourceType.SYSTEM_COVER));
                    return null;
                }
            });
        }
    };

    private static RoomDatabase.Callback coverInit = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            AsyncProcessor.getInstance().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    ResourceDao dao = INSTANCE.resourceDao();
                    dao.insert(new Resource("bg1", "bg1", Resource.DEF_TYPE_SYS_COVER, Resource.ResourceType.SYSTEM_COVER));
                    dao.insert(new Resource("bg2", "bg2", Resource.DEF_TYPE_SYS_COVER, Resource.ResourceType.SYSTEM_COVER));
                    dao.insert(new Resource("bg3", "bg3", Resource.DEF_TYPE_SYS_COVER, Resource.ResourceType.SYSTEM_COVER));
                    dao.insert(new Resource("bg4", "bg4", Resource.DEF_TYPE_SYS_COVER, Resource.ResourceType.SYSTEM_COVER));
                    dao.insert(new Resource("bg5", "bg5", Resource.DEF_TYPE_SYS_COVER, Resource.ResourceType.SYSTEM_COVER));
                    dao.insert(new Resource("bg6", "bg6", Resource.DEF_TYPE_SYS_COVER, Resource.ResourceType.SYSTEM_COVER));
                    dao.insert(new Resource("bg7", "bg7", Resource.DEF_TYPE_SYS_COVER, Resource.ResourceType.SYSTEM_COVER));
                    return null;
                }
            });
        }
    };
    private static Callback accountInit = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            AsyncProcessor.getInstance().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    AccountDao dao = INSTANCE.accountDao();
                    Account cash = new Account("零钱", 0L, 1, "现金", "ic_wallet", "CNY", "at_cash");
                    Account wechat = new Account("微信", 0L, 4, "网络账户", "ic_public", "CNY", "at_wechat");
                    Account alipay = new Account("支付宝", 0L, 4, "网络账户", "ic_public", "CNY", "at_alipay");
                    dao.insert(cash);
                    dao.insert(wechat);
                    dao.insert(alipay);
                    return null;
                }
            });
        }
    };

    private static RoomDatabase.Callback accountTypeInit = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            AsyncProcessor.getInstance().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    AccountTypeDao dao = INSTANCE.accountTypeDao();
                    dao.insert(new AccountType("现金", "ic_wallet", 1));
                    dao.insert(new AccountType("储蓄卡", "银行储蓄卡、借记卡", "ic_redeem", 2));
                    dao.insert(new AccountType("信用卡", "银行信用卡、花呗、白条", "ic_credit_card", 3));
                    dao.insert(new AccountType("网络账户", "支付宝、财付通", "ic_public", 4));
                    dao.insert(new AccountType("投资账户", "证券、基金", "ic_circle_dollar", 5));
                    dao.insert(new AccountType("储值卡", "购物卡、餐卡、医保卡", "ic_card_membership", 6));
                    dao.insert(new AccountType("虚拟账户", "积分、点卡", "ic_cloud", 7));
                    dao.insert(new AccountType("借贷", "亲友借还、押金", "ic_at_loan", 8));
                    return null;
                }
            });
        }
    };

    private static RoomDatabase.Callback currencyInit = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            AsyncProcessor.getInstance().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    CurrencyDao currencyDao = INSTANCE.currencyDao();
                    currencyDao.insert(getInitCurrencies());
                    return null;
                }
            });
        }
    };


    private static List<Currency> getInitCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        //构造主流货币的
        Currency rmb = new Currency("CNY", 156, "人民币元", "中国");
        rmb.setRate(1f);
        rmb.setEnable(true);
        currencies.add(rmb);
        currencies.add(new Currency("MOP", 446, "澳元(中国)", "中国澳门"));
        currencies.add(new Currency("HKD", 344, "港元", "中国香港"));
        currencies.add(new Currency("TWD", 901, "新台币元", "中国台湾"));
        currencies.add(new Currency("USD", 840, "美元", "美国"));
        currencies.add(new Currency("GBP", 826, "英镑", "大不列颠及北爱尔兰联合王国"));
        currencies.add(new Currency("EUR", 978, "欧元", "欧盟"));
        currencies.add(new Currency("JPY", 392, "日元", "日本"));
        currencies.add(new Currency("KRW", 410, "韩元", "韩国"));
        currencies.add(new Currency("INR", 356, "印度卢比", "印度"));
        currencies.add(new Currency("CAD", 124, "加元", "加拿大"));
        currencies.add(new Currency("RUB", 643, "卢布", "俄罗斯"));
        currencies.add(new Currency("AUD", 036, "澳元", "澳大利亚"));

        return currencies;
    }


    //初始化账单类别
    private static RoomDatabase.Callback categoryInit = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            AsyncProcessor.getInstance().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    CategoryDao categoryDao = INSTANCE.categoryDao();
                    categoryDao.insert(getInitCategories());
                    return null;
                }
            });
        }
    };

    private static List<Category> getInitCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("其他", "category_othe_others", Category.DEFAULT_PARENT_ID, 1, Category.TYPE_EXPENDITURE));
        categories.add(new Category("餐饮", "category_food_lunch", Category.DEFAULT_PARENT_ID, 2, Category.TYPE_EXPENDITURE));
        categories.add(new Category("购物", "category_shop_car", Category.DEFAULT_PARENT_ID, 3, Category.TYPE_EXPENDITURE));
        categories.add(new Category("服饰", "category_shop_clothes", Category.DEFAULT_PARENT_ID, 4, Category.TYPE_EXPENDITURE));
        categories.add(new Category("日用", "category_shop_home", Category.DEFAULT_PARENT_ID, 5, Category.TYPE_EXPENDITURE));
        categories.add(new Category("数码", "category_shop_digit", Category.DEFAULT_PARENT_ID, 6, Category.TYPE_EXPENDITURE));
        categories.add(new Category("美妆", "category_shop_makeup", Category.DEFAULT_PARENT_ID, 7, Category.TYPE_EXPENDITURE));
        categories.add(new Category("应用软件", "category_google_play", Category.DEFAULT_PARENT_ID, 8, Category.TYPE_EXPENDITURE));
        categories.add(new Category("住房", "category_home_property", Category.DEFAULT_PARENT_ID, 9, Category.TYPE_EXPENDITURE));
        categories.add(new Category("交通", "category_tran_taxi", Category.DEFAULT_PARENT_ID, 10, Category.TYPE_EXPENDITURE));
        categories.add(new Category("娱乐", "category_ente_game", Category.DEFAULT_PARENT_ID, 11, Category.TYPE_EXPENDITURE));
        categories.add(new Category("医疗", "category_medi_outpatient", Category.DEFAULT_PARENT_ID, 12, Category.TYPE_EXPENDITURE));
        categories.add(new Category("通讯", "category_home_call", Category.DEFAULT_PARENT_ID, 13, Category.TYPE_EXPENDITURE));
        categories.add(new Category("学习", "category_scho_fee", Category.DEFAULT_PARENT_ID, 14, Category.TYPE_EXPENDITURE));
        categories.add(new Category("办公", "category_scho_print", Category.DEFAULT_PARENT_ID, 15, Category.TYPE_EXPENDITURE));
        categories.add(new Category("运动", "category_ente_gym", Category.DEFAULT_PARENT_ID, 16, Category.TYPE_EXPENDITURE));
        categories.add(new Category("社交", "category_othe_social", Category.DEFAULT_PARENT_ID, 17, Category.TYPE_EXPENDITURE));
        categories.add(new Category("人情", "category_othe_favor", Category.DEFAULT_PARENT_ID, 18, Category.TYPE_EXPENDITURE));
        categories.add(new Category("育儿", "category_shop_baby", Category.DEFAULT_PARENT_ID, 19, Category.TYPE_EXPENDITURE));
        categories.add(new Category("宠物", "category_othe_pets", Category.DEFAULT_PARENT_ID, 20, Category.TYPE_EXPENDITURE));
        categories.add(new Category("旅行", "category_ente_travel2", Category.DEFAULT_PARENT_ID, 21, Category.TYPE_EXPENDITURE));
        categories.add(new Category("烟酒", "category_ente_smoke_drink", Category.DEFAULT_PARENT_ID, 22, Category.TYPE_EXPENDITURE));
        categories.add(new Category("会员", "category_vip_vip", Category.DEFAULT_PARENT_ID, 23, Category.TYPE_EXPENDITURE));

        categories.add(new Category("其他", "category_othe_others", Category.DEFAULT_PARENT_ID, 0, Category.TYPE_INCOME));
        categories.add(new Category("工资", "category_inve_salary", Category.DEFAULT_PARENT_ID, 1, Category.TYPE_INCOME));
        categories.add(new Category("奖金", "category_inve_scholarship2", Category.DEFAULT_PARENT_ID, 2, Category.TYPE_INCOME));
        categories.add(new Category("兼职", "category_inve_parttime", Category.DEFAULT_PARENT_ID, 3, Category.TYPE_INCOME));
        categories.add(new Category("利息", "category_inve_interest", Category.DEFAULT_PARENT_ID, 4, Category.TYPE_INCOME));
        categories.add(new Category("基金股票", "category_inve_stock", Category.DEFAULT_PARENT_ID, 5, Category.TYPE_INCOME));
        categories.add(new Category("生活费", "category_inve_financial", Category.DEFAULT_PARENT_ID, 6, Category.TYPE_INCOME));
        categories.add(new Category("奖学金", "category_inve_scholarship", Category.DEFAULT_PARENT_ID, 7, Category.TYPE_INCOME));
        categories.add(new Category("红包", "category_othe_lucky_money", Category.DEFAULT_PARENT_ID, 8, Category.TYPE_INCOME));

        return categories;
    }

    private static RoomDatabase.Callback resourceAccountInit = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            AsyncProcessor.getInstance().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    ResourceDao dao = INSTANCE.resourceDao();
                    dao.insert(getResourceAccountList());

                    return null;
                }
            });
        }
    };

    private static List<Resource> getResourceAccountList() {
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource("现金", "at_cash", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("支付宝", "at_alipay", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("微信支付", "at_wechat_pay", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("Paypal", "at_paypal", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("银联", "at_union_pay", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("Apple Pay", "at_apple_pay", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("VISA", "at_visa", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("比特币", "at_bitcoin", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("云闪付", "at_quick_pass", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("花呗", "at_huabei", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("淘宝", "at_taobao", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("京东", "at_jd", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("拼多多", "at_pdd", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("qq", "at_qq", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("微信", "at_wechat", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("美团", "at_meituan", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("饿了么", "at_eleme", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("肯德基", "at_kfc", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("麦当劳", "at_mcdonalds", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("中国银行", "at_bank_boc", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("农业银行", "at_bank_abc", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("工商银行", "at_bank_icbc", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("邮政银行", "at_bank_psbc", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("农村信用社", "at_bank_pbc", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("建设银行", "at_bank_ccb", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("招商银行", "at_bank_cmb", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("交通银行", "at_bank_comm", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("浦发银行", "at_bank_spdb", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("东亚银行", "at_bank_bea", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("光大银行", "at_bank_ceb", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("中信银行", "at_bank_citic", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("民生银行", "at_bank_cmbc", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("恒丰银行", "at_bank_hf", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("汇丰银行", "at_bank_hsbc", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("华夏银行", "at_bank_hx", "drawable", Resource.ResourceType.ACCOUNT));
        resources.add(new Resource("中原银行", "at_bank_zy", "drawable", Resource.ResourceType.ACCOUNT));
        return resources;
    }

    private static RoomDatabase.Callback resourceInit = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            AsyncProcessor.getInstance().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    ResourceDao resourceDao = INSTANCE.resourceDao();
                    resourceDao.insert(getInitResources());
                    return null;
                }
            });
        }
    };

    public static List<Resource> getInitResources() {
        List<Resource> resources = new ArrayList<>();

        resources.add(new Resource("category_ente_game", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_bathing", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_concert", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_gym", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_ktv", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_movie", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_party", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_pedicure", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_poker", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_travel", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_travel2", "drawable", "娱乐"));
        resources.add(new Resource("category_food_afternoon_tea", "drawable", "餐饮"));
        resources.add(new Resource("category_food_breakfast", "drawable", "餐饮"));
        resources.add(new Resource("category_food_candy", "drawable", "餐饮"));
        resources.add(new Resource("category_food_dinner", "drawable", "餐饮"));
        resources.add(new Resource("category_food_drink", "drawable", "餐饮"));
        resources.add(new Resource("category_food_fruit", "drawable", "餐饮"));
        resources.add(new Resource("category_food_lunch", "drawable", "餐饮"));
        resources.add(new Resource("category_food_milk_tea", "drawable", "餐饮"));
        resources.add(new Resource("category_food_night_snack", "drawable", "餐饮"));
        resources.add(new Resource("category_food_snack", "drawable", "餐饮"));
        resources.add(new Resource("category_food_waimai", "drawable", "餐饮"));
        resources.add(new Resource("category_food_wine", "drawable", "餐饮"));
        resources.add(new Resource("category_home_clean", "drawable", "家居"));
        resources.add(new Resource("category_home_electricity", "drawable", "家居"));
        resources.add(new Resource("category_home_gas", "drawable", "家居"));
        resources.add(new Resource("category_home_property", "drawable", "家居"));
        resources.add(new Resource("category_home_rent", "drawable", "家居"));
        resources.add(new Resource("category_home_water", "drawable", "家居"));
        resources.add(new Resource("category_home_call", "drawable", "家居"));
        resources.add(new Resource("category_inve_financial", "drawable", "理财"));
        resources.add(new Resource("category_inve_fund", "drawable", "理财"));
        resources.add(new Resource("category_inve_insurance", "drawable", "理财"));
        resources.add(new Resource("category_inve_interest", "drawable", "理财"));
        resources.add(new Resource("category_inve_parttime", "drawable", "理财"));
        resources.add(new Resource("category_inve_salary", "drawable", "理财"));
        resources.add(new Resource("category_inve_scholarship", "drawable", "理财"));
        resources.add(new Resource("category_inve_scholarship2", "drawable", "理财"));
        resources.add(new Resource("category_inve_stock", "drawable", "理财"));
        resources.add(new Resource("category_medi_health_prod", "drawable", "医疗"));
        resources.add(new Resource("category_medi_hospitalized", "drawable", "医疗"));
        resources.add(new Resource("category_medi_medician", "drawable", "医疗"));
        resources.add(new Resource("category_medi_outpatient", "drawable", "医疗"));
        resources.add(new Resource("category_medi_physical_exami", "drawable", "医疗"));
        resources.add(new Resource("category_medi_vaccine", "drawable", "医疗"));
        resources.add(new Resource("category_othe_lucky_money", "drawable", "其他"));
        resources.add(new Resource("category_othe_others", "drawable", "其他"));
        resources.add(new Resource("category_othe_social", "drawable", "其他"));
        resources.add(new Resource("category_othe_favor", "drawable", "其他"));
        resources.add(new Resource("category_othe_pets", "drawable", "其他"));
        resources.add(new Resource("category_scho_book", "drawable", "学校"));
        resources.add(new Resource("category_scho_exam", "drawable", "学校"));
        resources.add(new Resource("category_scho_fee", "drawable", "学校"));
        resources.add(new Resource("category_scho_print", "drawable", "学校"));
        resources.add(new Resource("category_scho_train", "drawable", "学校"));
        resources.add(new Resource("category_shop_car", "drawable", "购物"));
        resources.add(new Resource("category_shop_appliances", "drawable", "购物"));
        resources.add(new Resource("category_shop_baby", "drawable", "购物"));
        resources.add(new Resource("category_shop_clothes", "drawable", "购物"));
        resources.add(new Resource("category_shop_digit", "drawable", "购物"));
        resources.add(new Resource("category_shop_furnish", "drawable", "购物"));
        resources.add(new Resource("category_shop_hat", "drawable", "购物"));
        resources.add(new Resource("category_shop_home", "drawable", "购物"));
        resources.add(new Resource("category_shop_makeup", "drawable", "购物"));
        resources.add(new Resource("category_shop_shoes", "drawable", "购物"));
        resources.add(new Resource("category_shop_watch", "drawable", "购物"));
        resources.add(new Resource("category_shop_work", "drawable", "购物"));
        resources.add(new Resource("category_google_play", "drawable", "购物"));
        resources.add(new Resource("category_tran_bike", "drawable", "交通"));
        resources.add(new Resource("category_tran_bus", "drawable", "交通"));
        resources.add(new Resource("category_tran_car", "drawable", "交通"));
        resources.add(new Resource("category_tran_care", "drawable", "交通"));
        resources.add(new Resource("category_tran_metro", "drawable", "交通"));
        resources.add(new Resource("category_tran_oil", "drawable", "交通"));
        resources.add(new Resource("category_tran_parking", "drawable", "交通"));
        resources.add(new Resource("category_tran_plant", "drawable", "交通"));
        resources.add(new Resource("category_tran_road_fee", "drawable", "交通"));
        resources.add(new Resource("category_tran_ship", "drawable", "交通"));
        resources.add(new Resource("category_tran_taxi", "drawable", "交通"));
        resources.add(new Resource("category_tran_train", "drawable", "交通"));
        resources.add(new Resource("category_vip_apple_music", "drawable", "会员"));
        resources.add(new Resource("category_vip_bilibili", "drawable", "会员"));
        resources.add(new Resource("category_vip_iqiyi", "drawable", "会员"));
        resources.add(new Resource("category_vip_kugou", "drawable", "会员"));
        resources.add(new Resource("category_vip_netease", "drawable", "会员"));
        resources.add(new Resource("category_vip_netflix", "drawable", "会员"));
        resources.add(new Resource("category_vip_qq", "drawable", "会员"));
        resources.add(new Resource("category_vip_qq_music", "drawable", "会员"));
        resources.add(new Resource("category_vip_tx_video", "drawable", "会员"));
        resources.add(new Resource("category_vip_vip", "drawable", "会员"));


        Log.d("RESOURCE_TEST", "get resources init: " + resources.size());

        return resources;
    }


}
