package com.vividbobo.easy.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.CategoryDao;
import com.vividbobo.easy.database.dao.ResourceDao;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.ChildRvItem;
import com.vividbobo.easy.database.model.Resource;

import java.util.ArrayList;
import java.util.List;

public class ResourceRepo {

    //the Dao
    private final ResourceDao resourceDao;

    //the liveData
    private final LiveData<List<ChildRvItem>> iconChildRvItems;
    private final ListenableFuture<List<Resource>> accountResLF;
    private final ListenableFuture<List<Resource>> sysCoverLF;


    public ResourceRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        resourceDao = db.resourceDao();
        iconChildRvItems = getIconChildRvItemsLiveData(getCategoryDrawableResources());
        accountResLF = resourceDao.getResourcesByResType(Resource.ResourceType.ACCOUNT);
        sysCoverLF = resourceDao.getResourcesByResType(Resource.ResourceType.SYSTEM_COVER);
    }

    public ListenableFuture<List<Resource>> getSysCoverLF() {
        return sysCoverLF;
    }

    public ListenableFuture<List<Resource>> getAccountResLF() {
        return accountResLF;
    }

    public LiveData<List<ChildRvItem>> getIconChildRvItems() {
        return iconChildRvItems;
    }

    private LiveData<List<ChildRvItem>> getIconChildRvItemsLiveData(List<Resource> resources) {
        if (resources == null || resources.isEmpty()) return null;

        List<ChildRvItem> childRvItems = new ArrayList<>();
        for (int i = 0; i < resources.size(); i++) {
            Resource res = resources.get(i);
            String title = res.getGroup();

            if (childRvItems.isEmpty() || !title.equals(childRvItems.get(childRvItems.size() - 1).getTitle())) {
                childRvItems.add(new ChildRvItem(title));
            }
            ChildRvItem childRvItem = childRvItems.get(childRvItems.size() - 1);
            childRvItem.getChildItems().add(res);
        }

        return new MutableLiveData<List<ChildRvItem>>(childRvItems);
    }

    private List<Resource> getCategoryDrawableResources() {
        List<Resource> resources = new ArrayList<>();

        resources.add(new Resource("category_ente_bathing", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_concert", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_gym", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_ktv", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_movie", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_party", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_pedicure", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_poker", "drawable", "娱乐"));
        resources.add(new Resource("category_ente_travel", "drawable", "娱乐"));
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
        resources.add(new Resource("category_scho_book", "drawable", "学校"));
        resources.add(new Resource("category_scho_exam", "drawable", "学校"));
        resources.add(new Resource("category_scho_fee", "drawable", "学校"));
        resources.add(new Resource("category_scho_print", "drawable", "学校"));
        resources.add(new Resource("category_scho_train", "drawable", "学校"));
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
        resources.add(new Resource("category_vip_apple music", "drawable", "会员"));
        resources.add(new Resource("category_vip_bilibili", "drawable", "会员"));
        resources.add(new Resource("category_vip_iqiyi", "drawable", "会员"));
        resources.add(new Resource("category_vip_kugou", "drawable", "会员"));
        resources.add(new Resource("category_vip_netease", "drawable", "会员"));
        resources.add(new Resource("category_vip_netflix", "drawable", "会员"));
        resources.add(new Resource("category_vip_qq", "drawable", "会员"));
        resources.add(new Resource("category_vip_qq_music", "drawable", "会员"));
        resources.add(new Resource("category_vip_tx_video", "drawable", "会员"));
        resources.add(new Resource("category_vip_vip", "drawable", "会员"));

        return resources;
    }


}
