package cav.pdst.data.models;

public class SportsmanTrainingModel {
    private int mId;
    private String mName;
    private int mCount;
    private boolean mCheck;
    private int mMode = -1;
    private int mLinkAbonement = -1;

    public SportsmanTrainingModel(int id, String name, int count) {
        mId = id;
        mName = name;
        mCount = count;
    }

    public SportsmanTrainingModel(int id, String name, int count, int mode, int linkAbonement) {
        mId = id;
        mName = name;
        mCount = count;
        mMode = mode;
        mLinkAbonement = linkAbonement;
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

    public int getLinkAbonement() {
        return mLinkAbonement;
    }

    public void setLinkAbonement(int linkAbonement) {
        mLinkAbonement = linkAbonement;
    }
}
