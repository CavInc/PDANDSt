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
    private int mWorking = 0;
    private int mUsedWorking = 0;
    private int mWarning = 0;
    private float mDebit = 0.0f;
    private Date mDebitDate = null;


    public AbonementModel(int id,int pos_id, int spId, Date createDate, Date startDate, Date endDate,
                          int countTraining, float pay,int type, String comment,
                          int usedTraining,int working,int warning,float debit,Date debitDate,int usedWorking) {
        mAbonementId = id;
        mId = pos_id;
        mSpId = spId;
        mCreateDate = createDate;
        mStartDate = startDate;
        mEndDate = endDate;
        mCountTraining = countTraining;
        mType = type;
        mPay = pay;
        mComment = comment;
        mUsedTraining = usedTraining;
        mWorking = working;
        mWarning = warning;
        mDebit = debit;
        mDebitDate =debitDate;
        mUsedWorking = usedWorking;
    }


    protected AbonementModel(Parcel in) {
        mAbonementId = in.readInt();
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
        mWorking = in.readInt();
        mWarning = in.readInt();
        mDebit = in.readFloat();
        mDebitDate = (Date) in.readSerializable();
        mUsedWorking = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mAbonementId);
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
        dest.writeInt(mWorking);
        dest.writeInt(mWarning);
        dest.writeFloat(mDebit);
        dest.writeSerializable(mDebitDate);
        dest.writeInt(mUsedWorking);
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

    public int getWorking() {
        return mWorking;
    }

    public void setWorking(int working) {
        mWorking = working;
    }

    public int getWarning() {
        return mWarning;
    }

    public float getDebit() {
        return mDebit;
    }

    public void setDebit(float debit) {
        mDebit = debit;
    }

    public Date getDebitDate() {
        return mDebitDate;
    }

    public void setDebitDate(Date debitDate) {
        mDebitDate = debitDate;
    }

    public int getUsedWorking() {
        return mUsedWorking;
    }

    public void setUsedWorking(int usedWorking) {
        mUsedWorking = usedWorking;
    }
}
