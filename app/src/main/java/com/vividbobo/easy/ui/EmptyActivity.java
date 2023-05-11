package com.vividbobo.easy.ui;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.ui.bill.MoreCategoryBottomSheet;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.Objects;
import java.util.concurrent.Callable;

public class EmptyActivity extends AppCompatActivity {
    public static final String KEY_BILL = "bill";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        //启动bottomsheet
        Bill bill = (Bill) getIntent().getSerializableExtra(KEY_BILL);
        if (Objects.nonNull(bill)) {
            MoreCategoryBottomSheet dialog = MoreCategoryBottomSheet.newInstance(bill.getBillType());
            dialog.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, Object item, int position) {
                    Category category = (Category) item;
                    bill.setCategoryId(category.getId());
                    bill.setCategoryTitle(category.getTitle());
                    bill.setCategoryIconResName(category.getIconResName());

                    insert(getApplication(), bill);
                    finish();
                }
            });
            dialog.show(getSupportFragmentManager(), "CategoryPicker");
        }

    }

    public void insert(Application application, Bill bill) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Log.d("TAG", "call: insert Bill: " + bill.toString());
                EasyDatabase db = EasyDatabase.getDatabase(application);
                BillDao billDao = db.billDao();
                billDao.insert(bill);
                return null;
            }
        }).addListener(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EmptyActivity.this, "自动记账完成", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }, AsyncProcessor.getInstance().getExecutorService());
    }

}
