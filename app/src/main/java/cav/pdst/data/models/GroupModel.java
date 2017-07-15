package cav.pdst.data.models;


public class GroupModel {
    private int mId;
    private String mName;
    private int mCount;

    public GroupModel(String name, int count) {
        mName = name;
        mCount = count;
    }

    public GroupModel(int id, String name, int count) {
        mId = id;
        mName = name;
        mCount = count;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getCount() {
        return mCount;
    }
}
