package com.vividbobo.easy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.viewholder.BaseCurrencyVH;
import com.vividbobo.easy.adapter.viewholder.CurrencyVH;
import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrenciesAdapter extends CommonAdapter<Currency, BaseCurrencyVH, CurrencyVH, RecyclerView.ViewHolder> {
    public interface OnItemCheckedChangeListener {
        void onItemCheckedChange(CompoundButton buttonView, Object item, boolean isCheck);
    }


    // if the item checked changed, its status will be store in this map.
    // which is the holder position and the status of check
//    private Map<Integer, Boolean> checkedMap;

//    private Map<String, Float> rateMap;


    private OnItemCheckedChangeListener onItemCheckedChangeListener;


    public void setOnItemCheckedChangeListener(OnItemCheckedChangeListener onItemCheckedChangeListener) {
        this.onItemCheckedChangeListener = onItemCheckedChangeListener;
    }

    public CurrenciesAdapter(Context mContext) {
        super(mContext);
        setEnableHeader(true);
//        checkedMap = new HashMap<>();
//        rateMap = new HashMap<>();
    }

//    public void updateRateMap(Map<String, Float> rateMap) {
//        this.rateMap = rateMap;
//        notifyDataSetChanged();
//    }

//    public List<Currency> getNeedUpdateCurrencyList() {
//        List<Currency> updateList = new ArrayList<>();
//        for (Map.Entry<Integer, Boolean> entry : checkedMap.entrySet()) {
//            if (entry.getValue() == !getItemByHolderPosition(entry.getKey()).isEnable()) {
//                // if the map value and the item enable value is contrary.
//                // then this item need to update
//                updateList.add(getItemByHolderPosition(entry.getKey()));
//            }
//        }
//        return updateList;
//    }


    @Override
    protected BaseCurrencyVH onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item3, parent, false);
        return new BaseCurrencyVH(view);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected CurrencyVH onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency, parent, false);
        return new CurrencyVH(view);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull CurrencyVH holder, int position) {
        Currency currency = getItemByHolderPosition(holder.getAdapterPosition());
        holder.bind(currency);

//        // bind rate
//        if (rateMap != null && rateMap.containsKey(currency.getCode())) {
//            holder.rateTv.setText(String.format("%.4f", rateMap.get(currency.getCode())));
//        } else {
//            holder.rateTv.setText("");
//        }


        // switch check change
        holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if you click the switch, then add the status to this map
//                checkedMap.put(holder.getAdapterPosition(), isChecked);
                currency.setEnable(isChecked);
                if (onItemCheckedChangeListener != null) {
                    onItemCheckedChangeListener.onItemCheckedChange(buttonView, currency, isChecked);
                }
            }
        });
    }


    @Override
    protected void onBindHeaderViewHolder(@NonNull BaseCurrencyVH holder, int position) {
        Currency currency = (Currency) getHeaderItem();
        if (currency != null) holder.bind(currency);
    }

    @Override
    protected void onBindFooterViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //do nothing
    }
}
