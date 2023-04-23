package com.vividbobo.easy.ui.bill;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.adapter.adapter.ImageViewerAdapter;
import com.vividbobo.easy.databinding.SheetImageViewerBinding;
import com.vividbobo.easy.ui.common.BottomSheetDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.viewmodel.BillViewModel;

import java.util.List;

public class ImageViewerSheet extends BottomSheetDialog<SheetImageViewerBinding> {
    public static final String TAG = "ImageViewerSheet";

    private View.OnClickListener onFooterClickListener;
    private OnItemClickListener onItemClickListener;
    private List<String> imagePaths;
    private BillViewModel billViewModel;


    public static ImageViewerSheet newInstance() {
        Bundle args = new Bundle();
        ImageViewerSheet fragment = new ImageViewerSheet();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnFooterClickListener(View.OnClickListener onFooterClickListener) {
        this.onFooterClickListener = onFooterClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billViewModel = new ViewModelProvider(getActivity()).get(BillViewModel.class);
    }

    @Override
    public SheetImageViewerBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return SheetImageViewerBinding.inflate(inflater);
    }

    @Override
    public void onViewBinding(SheetImageViewerBinding binding) {

        ImageViewerAdapter imageViewerAdapter = new ImageViewerAdapter(getContext());
        imageViewerAdapter.setOnItemClickListener(onItemClickListener);
        imageViewerAdapter.setOnFooterClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                if (onFooterClickListener != null) {
                    onFooterClickListener.onClick(view);
                }
            }
        });
        binding.imageViewerRv.setAdapter(imageViewerAdapter);

        billViewModel.getImagePaths().observe(getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                imageViewerAdapter.updateItems(strings);
            }
        });
    }
}
