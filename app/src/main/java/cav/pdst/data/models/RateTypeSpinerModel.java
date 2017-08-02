package cav.pdst.data.models;

public class RateTypeSpinerModel {
    private int mId;
    private String mName;

    public RateTypeSpinerModel(int id, String name) {
        mId = id;
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return mName;
    }
}
