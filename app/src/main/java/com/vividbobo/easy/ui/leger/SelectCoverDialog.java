package com.vividbobo.easy.ui.leger;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vividbobo.easy.adapter.adapter.CoverAdapter;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.databinding.LayoutSelectCoverBinding;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;
import com.vividbobo.easy.ui.others.OnDialogResult;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;
import com.vividbobo.easy.utils.ConstantValue;
import com.vividbobo.easy.utils.ImageUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class SelectCoverDialog extends BaseFullScreenMaterialDialog<LayoutSelectCoverBinding> {
    public static final String TAG = "SelectCoverDialog";
    private CoverDialogViewModel coverDialogViewModel;
    private OnDialogResult onDialogResult;

    private final ActivityResultLauncher<PickVisualMediaRequest> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (Objects.nonNull(uri)) {
                    //save the image to cover
                    Log.d(TAG, "image uri: " + uri.toString());
                    String fileName = ConstantValue.COVER_PREDIX + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".jpg";
                    String path = ImageUtils.saveImageToAppPrivateFolder(getContext(), uri, fileName, ConstantValue.PRIVATE_DIR_IMAGES);
                    Log.d(TAG, "image path: " + path.toString());
                    coverDialogViewModel.insert(new Resource(fileName, path, Resource.DEF_TYPE_USER_COVER, Resource.ResourceType.USER_COVER));
                }
            });

    public void setOnDialogResult(OnDialogResult onDialogResult) {
        this.onDialogResult = onDialogResult;
    }

    public static SelectCoverDialog newInstance() {
        Bundle args = new Bundle();

        SelectCoverDialog fragment = new SelectCoverDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coverDialogViewModel = new ViewModelProvider(this).get(CoverDialogViewModel.class);
    }

    @Override
    protected LayoutSelectCoverBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return LayoutSelectCoverBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(LayoutSelectCoverBinding binding) {
        binding.appBarLayout.layoutToolBarTitleTv.setText("选择封面");
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        CoverAdapter coverAdapter = new CoverAdapter(getContext());
        coverAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Resource resource = (Resource) item;
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", resource);
                onDialogResult.onResult(bundle);
                dismiss();
            }
        });
        coverAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Object item, int position) {
                Resource resource = (Resource) item;
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("是否删除该封面?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                coverDialogViewModel.delete(resource);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }
        });
        binding.coverRv.setAdapter(coverAdapter);

        binding.fromGalleryFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        coverDialogViewModel.getCovers().observe(this, new Observer<List<Resource>>() {
            @Override
            public void onChanged(List<Resource> resources) {
                coverAdapter.updateItems(resources);
            }
        });

    }

    private void openGallery() {
        imagePickerLauncher.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
}
