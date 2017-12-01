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
        if (mLinkAbonement==0) {
            mCheck = false;
            mMode =-1;
        }else {
            mCheck = true;
        }
    }

    public SportsmanTrainingModel(int id, int linkAbonement, int mode) {
        mId = id;
        mLinkAbonement = linkAbonement;
        mMode = mode;
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

    public void setCount(int count) {
        mCount = count;
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

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if(obj == null)
            return false;
                /* Удостоверимся, что ссылки имеют тот же самый тип */
        if(!(getClass() == obj.getClass())) {
            return false;
        }else {
            SportsmanTrainingModel tmp = (SportsmanTrainingModel) obj;
            if (tmp.getId() == this.mId){
                return true;
            } else {
                return false;
            }

        }

    }
}
