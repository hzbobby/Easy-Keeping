package com.vividbobo.easy.ui.tags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.adapter.TagAdapter;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.databinding.DialogBaseEntityBinding;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;
import com.vividbobo.easy.ui.common.ContextOperationMenuDialog;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;
import com.vividbobo.easy.viewmodel.TagViewModel;

import java.util.List;

public class TagFullDialog extends BaseFullScreenMaterialDialog<DialogBaseEntityBinding> {
    private TagViewModel tagViewModel;

    private ContextOperationMenuDialog<Tag> operationMenuDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagViewModel = new ViewModelProvider(getActivity()).get(TagViewModel.class);
        operationMenuDialog = new ContextOperationMenuDialog<>();
    }

    @Override
    protected DialogBaseEntityBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogBaseEntityBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(DialogBaseEntityBinding binding) {
        binding.appBarLayout.layoutToolBarTitleTv.setText(R.string.tag_manage);
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.appBarLayout.layoutToolBar.inflateMenu(R.menu.menu_add);
        binding.appBarLayout.layoutToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add) {
                    TagAddFullDialog.newInstance().show(getParentFragmentManager(), TagAddFullDialog.TAG);
                    return true;
                }
                return false;
            }
        });


        TagAdapter tagAdapter = new TagAdapter(getContext());
        binding.baseEntityRv.setAdapter(tagAdapter);

        tagViewModel.getAllTags().observe(getViewLifecycleOwner(), new Observer<List<Tag>>() {
            @Override
            public void onChanged(List<Tag> tags) {
                tagAdapter.updateItems(tags);
            }
        });

        tagAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Object item, int position) {
                operationMenuDialog.show(getParentFragmentManager(), (Tag) item);
            }
        });

        operationMenuDialog.setOnEditClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Tag>() {
            @Override
            public void onMenuItemClick(Tag item) {
                //edit
                TagAddFullDialog.newInstance(item).show(getParentFragmentManager(), TagAddFullDialog.TAG);
            }
        });
        operationMenuDialog.setOnDetailClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Tag>() {
            @Override
            public void onMenuItemClick(Tag item) {

            }
        });
        operationMenuDialog.setOnDeleteClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Tag>() {
            @Override
            public void onMenuItemClick(Tag item) {
                tagViewModel.delete(item);
            }
        });
    }
}
