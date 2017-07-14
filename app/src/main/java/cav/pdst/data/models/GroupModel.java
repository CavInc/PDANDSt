package cav.pdst.data.models;


public class GroupModel {
    private String mName;
    private int mCount;

    public GroupModel(String name, int count) {
        mName = name;
        mCount = count;
    }

    public String getName() {
        return mName;
    }

    public int getCount() {
        return mCount;
    }
}
