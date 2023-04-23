package com.vividbobo.easy.ui.bill;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.vividbobo.easy.adapter.adapter.CategoryAdapter;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.CategoryPresent;
import com.vividbobo.easy.databinding.FragmentCategoryBinding;
import com.vividbobo.easy.ui.category.CategorySettingsFullDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.viewmodel.BillViewModel;
import com.vividbobo.easy.viewmodel.CategoryViewModel;

import java.util.List;

public class CategoryFragment extends Fragment {
    private static final String KEY_BILL_KIND = "bill_kind";

    private CategoryViewModel categoryViewModel;
    private BillViewModel billViewModel;
    private int billType;

    static CategoryFragment newInstance(@NonNull int type, Object data) {
        CategoryFragment fragment = new CategoryFragment();
        //如果需要传入数据
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_BILL_KIND, type);

        fragment.setArguments(bundle);
        return fragment;
    }

    FragmentCategoryBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billViewModel = new ViewModelProvider(getActivity()).get(BillViewModel.class);
        categoryViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        billType = getArguments().getInt(KEY_BILL_KIND);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // get the bill type

        binding.billCategoryRv.setLayoutManager(new GridLayoutManager(getContext(), 5));
        //category
        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity());
        categoryAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                CategoryPresent categoryPresent = (CategoryPresent) item;
                if (!categoryPresent.getChildren().isEmpty()) {
                    //展开bottomSheet
                    MoreCategoryBottomSheet.newInstance(categoryPresent, new OnItemChangeListener() {
                        @Override
                        public void onChange() {
                            categoryAdapter.setItemSelected(position);
                            //设置viewModel
                            if (billType == Category.TYPE_EXPENDITURE) {
                                billViewModel.setCategoryExpenditure((Category) item);
                            } else {
                                billViewModel.setCategoryIncome((Category) item);
                            }
                        }
                    }).show(getParentFragmentManager(), MoreCategoryBottomSheet.TAG);
                } else {
                    categoryAdapter.setItemSelected(position);
                    if (billType == Category.TYPE_EXPENDITURE) {
                        billViewModel.setCategoryExpenditure((Category) item);
                    } else {
                        billViewModel.setCategoryIncome((Category) item);
                    }
                }
            }
        });

        categoryAdapter.setOnFooterClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                CategorySettingsFullDialog.newInstance(billType).show(getParentFragmentManager(), CategorySettingsFullDialog.TAG);
            }
        });
        binding.billCategoryRv.setAdapter(categoryAdapter);


        if (billType == Category.TYPE_EXPENDITURE) {
            categoryViewModel.getExpenditureCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryPresent>>() {
                @Override
                public void onChanged(List<CategoryPresent> categoryPresents) {
                    categoryAdapter.updateItems(categoryPresents);
                }
            });
        } else {
            categoryViewModel.getIncomeCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryPresent>>() {
                @Override
                public void onChanged(List<CategoryPresent> categoryPresents) {
                    categoryAdapter.updateItems(categoryPresents);
                }
            });
        }

        //如果是编辑


        return root;
    }


}