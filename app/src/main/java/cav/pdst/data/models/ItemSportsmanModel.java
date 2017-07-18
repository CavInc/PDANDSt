package cav.pdst.data.models;


public class ItemSportsmanModel {
    private int mId;
    private String mName;
    private boolean mCheckItem=false;
    private int mGroupCount;
    private String mGroup;

    public ItemSportsmanModel(boolean checkItem, String name, int groupCount, String group) {
        mCheckItem = checkItem;
        mName = name;
        mGroupCount = groupCount;
        mGroup = group;
    }

    public ItemSportsmanModel(int _id,boolean checkItem, String name, int groupCount, String group) {
        this.mId = _id;
        mName = name;
        mCheckItem = checkItem;
        mGroupCount = groupCount;
        mGroup = group;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public boolean isCheckItem() {
        return mCheckItem;
    }

    public int getGroupCount() {
        return mGroupCount;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setCheckItem(boolean checkItem) {
        mCheckItem = checkItem;
    }
}
