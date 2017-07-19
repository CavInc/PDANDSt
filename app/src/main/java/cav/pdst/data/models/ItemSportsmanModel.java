package cav.pdst.data.models;


public class ItemSportsmanModel {
    private int mId;
    private String mName;
    private boolean mCheckItem=false;
    private String mGroup;


    public ItemSportsmanModel(int _id,boolean checkItem, String name,  String group) {
        this.mId = _id;
        mName = name;
        mCheckItem = checkItem;
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


    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    public void setCheckItem(boolean checkItem) {
        mCheckItem = checkItem;
    }
}
