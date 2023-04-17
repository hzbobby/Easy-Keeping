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
import com.vividbobo.easy.database.model.ChildRvItem;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.databinding.DialogAddCategoryBinding;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;
import com.vividbobo.easy.ui.others.NestedRvAdapter;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.utils.ToastUtil;
import com.vividbobo.easy.viewmodel.CategoryViewModel;
import com.vividbobo.easy.viewmodel.ResourceViewModel;

import java.util.List;

public class AddCategoryFullDialog extends BaseFullScreenMaterialDialog<DialogAddCategoryBinding> {
    public static final String TAG = "AddCategoryFullDialog";
    private static final String KEY_TYPE = "type";
    private static final String KEY_PARENT_ID = "parent_id";

    private ResourceViewModel resourceViewModel;
    private CategoryViewModel categoryViewModel;

    private Integer categoryType;
    private String iconRes;
    private int parentId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourceViewModel = new ViewModelProvider(this).get(ResourceViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

    }

    public static AddCategoryFullDialog newInstance(Integer type, Integer parentId) {
        Bundle args = new Bundle();
        args.putInt(KEY_TYPE, type);
        if (parentId != null)
            args.putInt(KEY_PARENT_ID, parentId);

        AddCategoryFullDialog fragment = new AddCategoryFullDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected DialogAddCategoryBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogAddCategoryBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(DialogAddCategoryBinding binding) {
        Bundle bundle = getArguments();
        categoryType = bundle.getInt(KEY_TYPE, Category.TYPE_EXPENDITURE);
        parentId = bundle.getInt(KEY_PARENT_ID, Category.DEFAULT_PARENT_ID);
        iconRes = ResourceUtils.getString(R.string.default_icon_res_name);

        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.appBarLayout.layoutToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.confirm) {
                    //insert demo
                    String title = binding.addCategoryTitleTl.getEditText().getText().toString();
                    if (title.isEmpty()) {
                        ToastUtil.makeToast(ResourceUtils.getString(R.string.input_category_title));
                        return false;
                    }
                    Category category = new Category();
                    category.setType(categoryType);
                    category.setTitle(title);
                    category.setIconResName(iconRes);
                    category.setParentId(parentId);

                    categoryViewModel.insert(category);
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        NestedRvAdapter nestedRvAdapter = new NestedRvAdapter(getContext());

        nestedRvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Log.d(TAG, "onItemClick: pos:" + position);
                Resource resource = (Resource) item;
                iconRes = resource.getResName();
                binding.addCategoryTitleTl.setStartIconDrawable(ResourceUtils.getResourceId(resource.getResName(), "drawable"));
                Log.d(TAG, "onItemClick: iconResName:"+resource.getResName());
            }
        });

        binding.addCategoryNestedRecyclerView.setAdapter(nestedRvAdapter);

        resourceViewModel.getIconChildRvItems().observe(this, new Observer<List<ChildRvItem>>() {
            @Override
            public void onChanged(List<ChildRvItem> childRvItems) {
                nestedRvAdapter.setChildRvItems(childRvItems);
            }
        });


    }

}
