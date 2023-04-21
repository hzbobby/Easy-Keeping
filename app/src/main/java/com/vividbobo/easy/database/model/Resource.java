package com.vividbobo.easy.database.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.vividbobo.easy.ui.account.ResourceBottomSheet;

@Entity(tableName = "resources")
public class Resource implements ResourceBottomSheet.Itemzable {

    /**
     * resourceType enum type; 枚举类型
     */
    public static enum ResourceType {
        CATEGORY, ACCOUNT
    }

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String title;   //res title
    private String resName; //resource name
    private String defType; //eg: if the resource is drawable, then the defType is "drawable"

    /**
     * use resType instead
     */
    @Deprecated()
    private String group;   // the res type;

    private ResourceType resType; // such category icon res, account icon res

    @Override
    public String getItemTitle() {
        return getTitle();
    }

    @Override
    public String getItemIconResName() {
        return getResName();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ResourceType getResType() {
        return resType;
    }

    public void setResType(ResourceType resType) {
        this.resType = resType;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Resource() {
    }

    @Ignore
    public Resource(String resName, String defType, String group) {
        this.resName = resName;
        this.defType = defType;
        this.group = group;
    }

    @Ignore
    public Resource(String title, String resName, String defType, ResourceType resType) {
        this.title = title;
        this.resName = resName;
        this.defType = defType;
        this.resType = resType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getDefType() {
        return defType;
    }

    public void setDefType(String defType) {
        this.defType = defType;
    }


}
