package cav.pdst.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class AbonementModel implements Parcelable {
    private int mAbonementId;
    private int mId;
    private int mSpId;
    private Date mCreateDate;
    private Date mStartDate;
    private Date mEndDate;
    private int mCountTraining;
    private float mPay;
    private int mType;
    private String mComment;
    private int mUsedTraining = 0;


    public AbonementModel(int id, int spId, Date createDate, Date startDate, Date endDate,
                          int countTraining, float pay,int type, String comment, int usedTraining) {
        mId = id;
        mSpId = spId;
        mCreateDate = createDate;
        mStartDate = startDate;
        mEndDate = endDate;
        mCountTraining = countTraining;
        mType = type;
        mPay = pay;
        mComment = comment;
        mUsedTraining = usedTraining;
    }

    protected AbonementModel(Parcel in) {
        mId = in.readInt();
        mSpId = in.readInt();
        mCreateDate = (Date) in.readSerializable();
        mStartDate = (Date) in.readSerializable();
        mEndDate = (Date) in.readSerializable();
        mCountTraining = in.readInt();
        mPay = in.readFloat();
        mType = in.readInt();
        mComment = in.readString();
        mUsedTraining = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mSpId);
        dest.writeSerializable(mCreateDate);
        dest.writeSerializable(mStartDate);
        dest.writeSerializable(mEndDate);
        dest.writeInt(mCountTraining);
        dest.writeFloat(mPay);
        dest.writeInt(mType);
        dest.writeString(mComment);
        dest.writeInt(mUsedTraining);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AbonementModel> CREATOR = new Creator<AbonementModel>() {
        @Override
        public AbonementModel createFromParcel(Parcel in) {
            return new AbonementModel(in);
        }

        @Override
        public AbonementModel[] newArray(int size) {
            return new AbonementModel[size];
        }
    };

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

    public String getComment() {
        return mComment;
    }

    public int getUsedTraining() {
        return mUsedTraining;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setSpId(int spId) {
        mSpId = spId;
    }

    public void setCreateDate(Date createDate) {
        mCreateDate = createDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public void setCountTraining(int countTraining) {
        mCountTraining = countTraining;
    }

    public void setPay(float pay) {
        mPay = pay;
    }

    public void setType(int type) {
        mType = type;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public void setUsedTraining(int usedTraining) {
        mUsedTraining = usedTraining;
    }

    public int getAbonementId() {
        return mAbonementId;
    }

    public void setAbonementId(int abonementId) {
        mAbonementId = abonementId;
    }
}
