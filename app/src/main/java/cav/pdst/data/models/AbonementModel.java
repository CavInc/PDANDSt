package cav.pdst.data.models;

import java.util.Date;

public class AbonementModel {
    private int mId;
    private int mSpId;
    private Date mCreateDate;
    private Date mStartDate;
    private Date mEndDate;
    private int mCountTraining;
    private float mPay;
    private int mType;

    public AbonementModel(int id, int spId, Date createDate, Date startDate, Date endDate, int countTraining, float pay, int type) {
        mId = id;
        mSpId = spId;
        mCreateDate = createDate;
        mStartDate = startDate;
        mEndDate = endDate;
        mCountTraining = countTraining;
        mPay = pay;
        mType = type;
    }

    public int getId() {
        return mId;
    }

    public int getSpId() {
        return mSpId;
    }

    public Date getCreateDate() {
        return mCreateDate;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public int getCountTraining() {
        return mCountTraining;
    }

    public float getPay() {
        return mPay;
    }

    public int getType() {
        return mType;
    }
}
