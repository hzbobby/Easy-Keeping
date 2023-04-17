package com.vividbobo.easy.ui.category;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.CategoryPresent;
import com.vividbobo.easy.databinding.DialogCategorySettingsBinding;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.others.expandableRv.ExpandableGroupRvAdapter;
import com.vividbobo.easy.viewmodel.CategoryViewModel;

import java.util.List;

public class CategorySettingsFullDialog extends BaseFullScreenMaterialDialog<DialogCategorySettingsBinding> {
    public static final String TAG = "CategorySettingsFullDia";
    private static final String KEY_BILL_TYPE = "bill_type";

    public static CategorySettingsFullDialog newInstance(@NonNull int billType) {
        Bundle args = new Bundle();
        args.putInt(KEY_BILL_TYPE, billType);
        CategorySettingsFullDialog fragment = new CategorySettingsFullDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private CategoryViewModel categoryViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    @Override
    protected DialogCategorySettingsBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogCategorySettingsBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(DialogCategorySettingsBinding binding) {
        int billType = getArguments().getInt(KEY_BILL_TYPE);

        binding.categorySettingsAppBarLayout.layoutToolBar.inflateMenu(R.menu.menu_add);
        binding.categorySettingsAppBarLayout.layoutToolBarTitleTv.setText(R.string.category_settings);
        binding.categorySettingsAppBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.categorySettingsAppBarLayout.layoutToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add) {
                    AddCategoryFullDialog.newInstance(billType, null).show(getParentFragmentManager(), AddCategoryFullDialog.TAG);
                    return true;
                }
                return false;
            }
        });

        ExpandableGroupRvAdapter expandableGroupRvAdapter = new ExpandableGroupRvAdapter(getContext());
        expandableGroupRvAdapter.setOnAddClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Category category = (Category) item;
                AddCategoryFullDialog.newInstance(category.getType(), category.getId()).show(getParentFragmentManager(), AddCategoryFullDialog.TAG);
            }
        });

        binding.categorySettingsRv.setAdapter(expandableGroupRvAdapter);

        if (billType == Category.TYPE_EXPENDITURE) {
            categoryViewModel.getExpenditureCategories().observe(this, new Observer<List<CategoryPresent>>() {
                @Override
                public void onChanged(List<CategoryPresent> categoryPresents) {
                    Log.d(TAG, "onChanged: update categories, the size:" + categoryPresents.size());
                    expandableGroupRvAdapter.setItems(categoryPresents);
                }
            });
        } else {
            categoryViewModel.getIncomeCategories().observe(this, new Observer<List<CategoryPresent>>() {
                @Override
                public void onChanged(List<CategoryPresent> categoryPresents) {
                    expandableGroupRvAdapter.setItems(categoryPresents);
                }
            });
        }


    }
}
