package com.vividbobo.easy.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.checkerframework.checker.interning.qual.FindDistinct;

@Entity(tableName = "categories")
public class Category extends ServerBaseEntity implements Parcelable {
    public final static int TYPE_EXPENDITURE = 0;
    public final static int TYPE_INCOME = 1;
    public final static int DEFAULT_PARENT_ID = -1;

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String title;       //类型名称
    private String iconResName;    //图标资源
    private Integer parentId;   //父类型ID
    private Integer orderNum;   //层级
    private Integer type;       //类型种类

    public Category() {
    }

    @Ignore
    public Category(String title, String iconResName, Integer parentId, Integer orderNum, Integer type) {
        this.title = title;
        this.iconResName = iconResName;
        this.parentId = parentId;
        this.orderNum = orderNum;
        this.type = type;
    }

    @Ignore
    protected Category(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();

        iconResName = in.readString();
        if (in.readByte() == 0) {
            parentId = null;
        } else {
            parentId = in.readInt();
        }
        if (in.readByte() == 0) {
            orderNum = null;
        } else {
            orderNum = in.readInt();
        }
        if (in.readByte() == 0) {
            type = null;
        } else {
            type = in.readInt();
        }
    }

    @Ignore
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };


    @Ignore
    public Category(String title, String resName) {
        super();
        this.title = title;
        this.iconResName = resName;
    }

    public String getIconResName() {
        return iconResName;
    }

    public void setIconResName(String iconResName) {
        this.iconResName = iconResName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    protected void setCategory(Category category) {
        this.id = category.id;
        this.iconResName = category.iconResName;
        this.title = category.title;
        this.type = category.type;
        this.parentId = category.parentId;
        this.orderNum = category.orderNum;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", iconResName='" + iconResName + '\'' +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                ", type=" + type +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(title);

        dest.writeString(iconResName);
        if (parentId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(parentId);
        }
        if (orderNum == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(orderNum);
        }
        if (type == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(type);
        }
    }
}
