package cav.pdst.data.models;


public class ItemSportsmanModel {
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
}
