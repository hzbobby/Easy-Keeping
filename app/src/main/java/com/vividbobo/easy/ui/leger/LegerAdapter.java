package com.vividbobo.easy.ui.leger;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.model.LegerItem;
import com.vividbobo.easy.ui.others.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class LegerAdapter extends RecyclerView.Adapter<LegerAdapter.LegerViewHolder> {

    private int selectPosition = 0;
    private List<LegerItem> legers;
    private OnItemClickListener onEditBtnClickListener;
    private OnItemClickListener onClickListener;   //select click

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setOnClickListener(OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnEditBtnClickListener(OnItemClickListener onEditBtnClickListener) {
        this.onEditBtnClickListener = onEditBtnClickListener;
    }

    public void setLegers(List<LegerItem> legers) {
        this.legers = legers;
    }

    public LegerAdapter() {
        legers = new ArrayList<>();
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
        legers.add(new LegerItem());
    }

    @NonNull
    @Override
    public LegerAdapter.LegerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leger, parent, false);

        return new LegerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LegerAdapter.LegerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(position== getSelectPosition()){
            //set current label
            holder.label.setVisibility(View.VISIBLE);
        }else{
            //let it gone
            holder.label.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    //TODO change position to data.id
                    onClickListener.OnItemClick(position, position);
                }
            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onEditBtnClickListener != null) {
                    //TODO set data
                    onEditBtnClickListener.OnItemClick(null, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return legers.size();
    }

    public class LegerViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        ImageButton editBtn;
        TextView leger_title, leger_desc;

        ConstraintLayout label;

        public LegerViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.leger_cover);
            editBtn = itemView.findViewById(R.id.leger_edit_image_btn);
            leger_title = itemView.findViewById(R.id.leger_title);
            leger_desc = itemView.findViewById(R.id.leger_desc);

            label=itemView.findViewById(R.id.leger_selected_label);
        }
    }


}
