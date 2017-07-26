package cav.pdst.data.models;

public class SportsmanTrainingModel {
    private int mId;
    private String mName;
    private int mCount;
    private boolean mCheck;
    private int mMode = -1;

    public SportsmanTrainingModel(int id, String name, int count) {
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

    public boolean isCheck() {
        return mCheck;
    }

    public void setCheck(boolean check) {
        mCheck = check;
    }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mode) {
        mMode = mode;
    }
}
