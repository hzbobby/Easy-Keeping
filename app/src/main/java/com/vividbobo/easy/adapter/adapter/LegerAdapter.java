package com.vividbobo.easy.adapter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;
import com.vividbobo.easy.utils.GlideUtils;
import com.vividbobo.easy.utils.ResourceUtils;

import java.util.Objects;

public class LegerAdapter extends CommonAdapter<Leger, RecyclerView.ViewHolder, LegerAdapter.LegerVH, RecyclerView.ViewHolder> {
    private OnItemClickListener onEditClickListener;

    private int selectedLegerId = 1;
    private int oldSelectedPos = 0;
    private int newSelectedPos = oldSelectedPos;

    public LegerAdapter(Context mContext) {
        super(mContext);
    }

    public void setOnEditClickListener(OnItemClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    public int getSelectedLegerId() {
        return selectedLegerId;
    }

    public void setSelectedLegerId(int selectedLegerId) {
        this.selectedLegerId = selectedLegerId;
        notifyItemChanged(oldSelectedPos);
        notifyItemChanged(newSelectedPos);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected LegerVH onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leger, parent, false);
        return new LegerVH(view);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull LegerVH holder, int position) {
        Leger leger = getItemByHolderPosition(position);
        holder.bind(mContext, leger);

        holder.editIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditClickListener != null) {
                    onEditClickListener.onItemClick(v, getItemByHolderPosition(holder.getAdapterPosition()), holder.getAdapterPosition());
                }
            }
        });
        //重写其item点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldSelectedPos = newSelectedPos;
                newSelectedPos = position;
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, getItemByHolderPosition(position), position);
                }
            }
        });

        if (selectedLegerId != leger.getId()) {
            //选中的不是当前这个
            Log.d("TAG", "onBindNormalViewHolder: hide label: " + position);
            holder.labelLayout.setVisibility(View.GONE);
        } else {
            //选中的是当前这个
            Log.d("TAG", "onBindNormalViewHolder: show label: " + position);
            holder.labelLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindFooterViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }


    public class LegerVH extends RecyclerView.ViewHolder {
        public ImageView iconIv;
        public TextView titleTv, descTv;
        private ConstraintLayout labelLayout;
        private ImageView editIb;


        public LegerVH(@NonNull View itemView) {
            super(itemView);
            labelLayout = itemView.findViewById(R.id.item_selected_label);
            editIb = itemView.findViewById(R.id.item_leger_edit_iv);
            iconIv = itemView.findViewById(R.id.item_icon_iv);
            titleTv = itemView.findViewById(R.id.item_title_tv);
            descTv = itemView.findViewById(R.id.item_desc_tv);
        }

        public void bind(Context context, Leger entity) {
            if (Objects.isNull(entity.getItemIconResName())) {
                ResourceUtils.bindImageDrawable(context, ResourceUtils.getTextImageIcon(entity.getItemTitle(), ResourceUtils.getColor(R.color.black))).centerCrop().into(iconIv);
            } else {
                GlideUtils.bindLegerCover(context, entity.getItemIconResName(), entity.getCoverType()).centerCrop().into(iconIv);
            }

            titleTv.setText(entity.getItemTitle());
            if (entity.getItemDesc().isEmpty()) {
                descTv.setVisibility(View.GONE);
            } else {
                descTv.setVisibility(View.VISIBLE);
                descTv.setText(entity.getItemDesc());
            }
        }

    }
}
