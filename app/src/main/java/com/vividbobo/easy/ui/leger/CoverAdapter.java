package com.vividbobo.easy.ui.leger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.model.ImageItem;

import java.util.ArrayList;
import java.util.List;

public class CoverAdapter extends RecyclerView.Adapter<CoverAdapter.CoverViewHolder> {

    private List<ImageItem> covers;

    public CoverAdapter() {
        covers=new ArrayList<>();
        covers.add(new ImageItem());
        covers.add(new ImageItem());
        covers.add(new ImageItem());
        covers.add(new ImageItem());
        covers.add(new ImageItem());
    }

    public void setCovers(List<ImageItem> covers) {
        this.covers = covers;
    }

    @NonNull
    @Override
    public CoverAdapter.CoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cover, parent, false);
        return new CoverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoverAdapter.CoverViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return covers.size();
    }

    public class CoverViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;

        public CoverViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.item_cover);
        }
    }
}
