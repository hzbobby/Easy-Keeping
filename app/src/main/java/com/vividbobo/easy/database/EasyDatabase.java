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
import com.vividbobo.easy.database.converter.LocalTimeConverter;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.dao.CategoryDao;
import com.vividbobo.easy.database.dao.ConfigDao;
import com.vividbobo.easy.database.dao.CurrencyDao;
import com.vividbobo.easy.database.dao.ResourceDao;
import com.vividbobo.easy.database.dao.RoleDao;
import com.vividbobo.easy.database.dao.StoreDao;
import com.vividbobo.easy.database.dao.TagDao;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.database.model.CurrencyRate;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.database.model.Store;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Database(
        entities = {Bill.class, Category.class, Leger.class,
                Account.class, Tag.class, Currency.class, CurrencyRate.class, Resource.class,
                Role.class, Config.class, Store.class},
        version = 1,
        exportSchema = false)
@TypeConverters({Converters.class, LocalTimeConverter.class})
public abstract class EasyDatabase extends RoomDatabase {

    public abstract TagDao tagDao();

    public abstract CurrencyDao currencyDao();

    public abstract StoreDao storeDao();

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
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback currencyInit = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };


    private static List<Currency> getInitCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        //构造主流货币的
//        currencies.add(new Currency())


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
