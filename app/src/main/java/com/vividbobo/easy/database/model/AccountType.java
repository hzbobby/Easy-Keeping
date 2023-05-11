package com.vividbobo.easy.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.vividbobo.easy.adapter.Itemzable;
import com.vividbobo.easy.adapter.adapter.DropdownMenuAdapter;

import java.io.Serializable;

@Entity(tableName = "accountTypes")
public class AccountType extends BaseEntity implements Serializable, Parcelable, DropdownMenuAdapter.DropdownMenuItem, Itemzable {
    public static final Integer DEFAULT_PARENT_ID = -1;
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Integer parentId;    //父类型ID
    private Integer order;      //顺序

//    private List<AccountType> children;   //暂时不做子账户类型，比如说银行->工商、农业、等这些


    public AccountType() {
    }

    @Ignore
    public AccountType(String title, String iconResName, int order) {
        this.setTitle(title);
        this.setIconResName(iconResName);
        this.setOrder(order);
        this.parentId = DEFAULT_PARENT_ID;
    }

    @Ignore
    public AccountType(String title, String desc, String iconResName, int order) {
        this(title, iconResName, order);
        this.setDesc(desc);
    }

    protected AccountType(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            parentId = null;
        } else {
            parentId = in.readInt();
        }
        if (in.readByte() == 0) {
            order = null;
        } else {
            order = in.readInt();
        }
//        children = in.createTypedArrayList(AccountType.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (parentId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(parentId);
        }
        if (order == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(order);
        }
//        dest.writeTypedList(children);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AccountType> CREATOR = new Creator<AccountType>() {
        @Override
        public AccountType createFromParcel(Parcel in) {
            return new AccountType(in);
        }

        @Override
        public AccountType[] newArray(int size) {
            return new AccountType[size];
        }
    };

    public Integer getId() {
        return id;
    }

//    public List<AccountType> getChildren() {
//        return children;
//    }

//    public void setChildren(List<AccountType> children) {
//        this.children = children;
//    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String getItemIconResName() {
        return this.getIconResName();
    }

    @Override
    public String getItemTitle() {
        return this.getTitle();
    }

    @Override
    public String getItemDesc() {
        return getDesc();
    }
}
