package com.vividbobo.easy.adapter.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
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
import com.vividbobo.easy.ui.others.OnItemLongClickListener;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;
import com.vividbobo.easy.utils.ResourceUtils;

public class ImageViewerAdapter extends CommonAdapter<Uri, RecyclerView.ViewHolder, ImageViewerAdapter.ImageVH, ImageViewerAdapter.ImageVH> {

    public ImageViewerAdapter(Context mContext) {
        super(mContext);
    }

    private int maxItems = 9;

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
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
        Uri item = getItemByHolderPosition(position);
        holder.bind(mContext, item);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.deleteIv.setVisibility(View.VISIBLE);
                return true;
            }
        });
        holder.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.deleteIv.getVisibility() == View.VISIBLE) {
                    removeItem(getItemPosition(holder.getAdapterPosition()));
                }
            }
        });
        holder.deleteIv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.deleteIv.setVisibility(View.GONE);
                return true;
            }
        });
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindFooterViewHolder(@NonNull ImageViewerAdapter.ImageVH holder, int position) {
        if (items.size() >= maxItems) {
            holder.imageIv.setVisibility(View.GONE);
        } else {
            holder.imageIv.setVisibility(View.VISIBLE);
        }
        ResourceUtils.bindImageDrawable(mContext, ResourceUtils.getDrawable(R.drawable.ic_add))
                .centerCrop().into(holder.imageIv);
    }

    public static class ImageVH extends RecyclerView.ViewHolder {
        private ImageView imageIv, deleteIv;

        public ImageVH(@NonNull View itemView) {
            super(itemView);
            imageIv = itemView.findViewById(R.id.item_image_iv);
            deleteIv = itemView.findViewById(R.id.item_delete_iv);
        }

        public void bind(Context context, Uri image) {
            Log.d("TAG", "bind: image path: " + image);

            Glide.with(context).load(image).centerCrop().into(imageIv);
        }

        public static String getRealPathFromURI(Context context, Uri contentUri) {
            String[] projection = {MediaStore.Images.Media.DATA};
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(contentUri, projection, null, null, null);
            if (cursor == null) {
                return contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String realPath = cursor.getString(columnIndex);
                cursor.close();
                Log.d("TAG", "getRealPathFromURI: " + realPath);
                return realPath;
            }
        }
    }
}
