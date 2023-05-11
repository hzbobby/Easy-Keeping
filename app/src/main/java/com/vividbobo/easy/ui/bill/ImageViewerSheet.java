package com.vividbobo.easy.ui.bill;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.adapter.adapter.ImageViewerAdapter;
import com.vividbobo.easy.databinding.SheetImageViewerBinding;
import com.vividbobo.easy.ui.common.BottomSheetDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.viewmodel.BillViewModel;

import java.util.ArrayList;
import java.util.List;

public class ImageViewerSheet extends BottomSheetDialog<SheetImageViewerBinding> {
    public static final String TAG = "ImageViewerSheet";

    private View.OnClickListener onFooterClickListener;
    private OnItemClickListener onItemClickListener;
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
    }

    @Override
    public SheetImageViewerBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return SheetImageViewerBinding.inflate(inflater);
    }


    @Override
    public void onViewBinding(SheetImageViewerBinding binding) {
        billViewModel = new ViewModelProvider(getActivity()).get(BillViewModel.class);

        ImageViewerAdapter imageViewerAdapter = new ImageViewerAdapter(getContext());
        imageViewerAdapter.setEnableFooter(true);

        imageViewerAdapter.setOnItemClickListener(onItemClickListener);
        imageViewerAdapter.setOnFooterClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Log.d(TAG, "onItemClick: add footer click");
                if (onFooterClickListener != null) {
                    onFooterClickListener.onClick(view);
                }
            }
        });
        binding.imageViewerRv.setAdapter(imageViewerAdapter);

        binding.clearImageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billViewModel.setImageUris(new ArrayList<>());
            }
        });

        billViewModel.getImageUris().observe(getActivity(), new Observer<List<Uri>>() {
            @Override
            public void onChanged(List<Uri> uris) {
                imageViewerAdapter.updateItems(uris);
            }
        });

    }
}
