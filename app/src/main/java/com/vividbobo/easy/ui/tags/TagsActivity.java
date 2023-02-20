package com.vividbobo.easy.ui.tags;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.vividbobo.easy.BaseActivity;
import com.vividbobo.easy.databinding.ActivityTagsBinding;
import com.vividbobo.easy.model.TagItem;
import com.vividbobo.easy.ui.others.OnItemClickListener;

public class TagsActivity extends BaseActivity {
    private static final String TAG = "TagsActivity";
    private ActivityTagsBinding binding;
    private TagItem clickTagItem;
    private int clickPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTagsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tagsToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //tag列表
        binding.tagsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        TagAdapter tagAdapter = new TagAdapter();
        //操作dialog
        final String[] items = new String[]{"明细", "编辑", "删除"};
        AlertDialog operationDialog = new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                .setTitle("操作")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:

                                break;
                            case 1:
                                AddTagDialog.newInstance(binding.getRoot().getContext(), clickTagItem).show();
                                break;
                            case 2:
                                tagAdapter.deleteItem(clickPosition);

                                break;
                            default:
                        }
                    }
                }).create();

        tagAdapter.setFooterEnable(true);
        tagAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(Object item, int position) {
                clickTagItem = (TagItem) item;
                clickPosition = position;
                Log.d(TAG, "OnItemClick: clickPosition: "+clickPosition);
                operationDialog.show();
            }
        });
        tagAdapter.setOnFooterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddTagDialog(binding.getRoot().getContext()).show();
            }
        });

        binding.tagsRecyclerView.setAdapter(tagAdapter);


    }
}
