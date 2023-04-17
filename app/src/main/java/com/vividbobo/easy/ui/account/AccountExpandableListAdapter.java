package com.vividbobo.easy.ui.account;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.AccountGroup;
import com.vividbobo.easy.database.model.AccountItem;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class AccountExpandableListAdapter extends BaseExpandableListAdapter {

    private List<AccountGroup> accountGroups;
    private boolean hideBalance = true;

    private int lastClickGroupPos = -1;
    private int lastClickChildPos = -1;

    private void setLastClickPos(int groupPos, int childPos) {
        lastClickGroupPos = groupPos;
        lastClickChildPos = childPos;
    }

    public AccountItem getLastClickItem() {
        if (lastClickGroupPos >= 0 && lastClickChildPos >= 0
                && lastClickGroupPos < accountGroups.size() && lastClickChildPos < accountGroups.get(lastClickGroupPos).getChildren().size()) {
            return accountGroups.get(lastClickGroupPos).getChildren().get(lastClickChildPos);
        }
        return null;
    }

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AccountItem getItem(int groupPos, int childPos) {
        if (groupPos >= 0 && childPos >= 0 && groupPos < accountGroups.size() && childPos < accountGroups.get(groupPos).getChildren().size()) {
            return accountGroups.get(groupPos).getChildren().get(childPos);
        }
        return null;
    }


    public void setHideBalance(boolean hideBalance) {
        this.hideBalance = hideBalance;
        notifyDataSetChanged();
    }

    public AccountExpandableListAdapter() {
        accountGroups = new ArrayList<>();

        //just for test
        for (int i = 0; i < 15; i++) {
            String title = String.format("账户#%d", i);
            AccountGroup accountGroup = new AccountGroup();
            accountGroup.setTitle(title);
            for (int j = 0; j < i + 1; j++) {
                String childTitle = String.format("子账户#%d", j);
                AccountItem accountItem = new AccountItem();
                accountItem.setTitle(childTitle);
                accountItem.setBalance(i * j * 10000);
                accountGroup.getChildren().add(accountItem);
            }
            accountGroups.add(accountGroup);
        }


    }

    public void setAccountGroups(List<AccountGroup> accountGroups) {
        this.accountGroups = accountGroups;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return accountGroups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return accountGroups.get(i).getChildren().size();
    }

    @Override
    public Object getGroup(int i) {
        return accountGroups.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return accountGroups.get(i).getChildren().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String groupTitle = ((AccountGroup) getGroup(i)).getTitle();
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.header_account_group, viewGroup, false);
        }
        TextView groupTitleView = view.findViewById(R.id.header_account_group_title);
        groupTitleView.setText(groupTitle);
        return view;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        AccountItem accountItem = ((AccountGroup) getGroup(i)).getChildren().get(i1);
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_account, viewGroup, false);
        }
        //icon
        ImageView iconImage = view.findViewById(R.id.item_account_icon);
        //text
        TextView titleText = view.findViewById(R.id.item_account_title_tv);
        titleText.setText(accountItem.getTitle());
        //balance
        TextView balanceText = view.findViewById(R.id.item_account_balance_tv);
        balanceText.setText(String.format("%.2f", 1.0 * accountItem.getBalance() / 100));


        if (hideBalance) {
            balanceText.setVisibility(View.GONE);
        } else {
            balanceText.setVisibility(View.VISIBLE);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLastClickPos(i, i1);
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view,accountItem, i1);
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setLastClickPos(i, i1);
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(accountItem, i1);
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


}
