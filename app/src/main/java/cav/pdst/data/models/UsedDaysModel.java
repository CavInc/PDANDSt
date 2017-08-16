package cav.pdst.data.models;

import java.util.Date;

public class UsedDaysModel {
    private Date mDate;
    private int mType;

    public UsedDaysModel(Date date, int type) {
        mDate = date;
        mType = type;
    }

    public Date getDate() {
        return mDate;
    }

    public int getType() {
        return mType;
    }
}