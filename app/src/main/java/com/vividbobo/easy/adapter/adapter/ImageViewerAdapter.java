package com.vividbobo.easy.adapter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vividbobo.easy.R;
import com.vividbobo.easy.ui.bill.ImageViewerSheet;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;
import com.vividbobo.easy.utils.ResourceUtils;

public class ImageViewerAdapter extends CommonAdapter<String, RecyclerView.ViewHolder, ImageViewerAdapter.ImageVH, ImageViewerAdapter.ImageVH> {

    public ImageViewerAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected ImageViewerAdapter.ImageVH onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewerAdapter.ImageVH(v);
    }

    @Override
    protected ImageViewerAdapter.ImageVH onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageVH(v);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull ImageViewerAdapter.ImageVH holder, int position) {
        String item = getItemByHolderPosition(position);
        holder.bind(mContext, item);
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindFooterViewHolder(@NonNull ImageViewerAdapter.ImageVH holder, int position) {
        ResourceUtils.bindImageDrawable(mContext, ResourceUtils.getDrawable(R.drawable.ic_add))
                .centerCrop().into(holder.imageIv);
    }

    public static class ImageVH extends RecyclerView.ViewHolder {
        private ImageView imageIv;

        public ImageVH(@NonNull View itemView) {
            super(itemView);
            imageIv = itemView.findViewById(R.id.item_image_iv);
        }

        public void bind(Context context, String image) {
            Log.d("TAG", "bind: image path: " + image);
            Glide.with(context).load(image).centerCrop().into(imageIv);
        }
    }
}
