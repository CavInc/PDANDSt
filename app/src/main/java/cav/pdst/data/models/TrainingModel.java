package cav.pdst.data.models;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

import java.util.Date;

public class TrainingModel implements Parcelable {
    private int mId;
    private String mName;
    private int mType;
    private int mCount;
    private Date mDate;
    private String mTime;
    private int mAbonentNum;
    private int mTypeTraining = 0;
    private int mRepeatType = 0;


    public TrainingModel(String name, int type, int count, Date date, String time) {
        mName = name;
        mType = type;
        mCount = count;
        mDate = date;
        mTime = time;
    }

    public TrainingModel(int id, String name, int type, int count, Date date, String time, int abonentNum) {
        mId = id;
        mName = name;
        mType = type;
        mCount = count;
        mDate = date;
        mTime = time;
        mAbonentNum = abonentNum;
    }

    public TrainingModel(int id, String name, int type, int count, Date date, String time, int abonentNum,int typeTraining) {
        mId = id;
        mName = name;
        mType = type;
        mCount = count;
        mDate = date;
        mTime = time;
        mAbonentNum = abonentNum;
        mTypeTraining = typeTraining;
    }

    public TrainingModel(int id, String name, int type, int count, Date date, String time, int abonentNum,int typeTraining,int repeatType) {
        mId = id;
        mName = name;
        mType = type;
        mCount = count;
        mDate = date;
        mTime = time;
        mAbonentNum = abonentNum;
        mTypeTraining = typeTraining;
        mRepeatType = repeatType;
    }

    public TrainingModel(Parcel parcel) {
        mId = parcel.readInt();
        mName = parcel.readString();
        mType = parcel.readInt();
        mCount = parcel.readInt();
        mDate = (Date) parcel.readSerializable();
        mTime = parcel.readString();
        mAbonentNum = parcel.readInt();
        mTypeTraining = parcel.readInt();
        mRepeatType = parcel.readInt();
    }


    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getType() {
        return mType;
    }

    public int getCount() {
        return mCount;
    }

    public Date getDate() {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setType(int type) {
        mType = type;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public int getAbonentNum() {
        return mAbonentNum;
    }

    public int getTypeTraining() {
        return mTypeTraining;
    }

    public int getRepeatType() {
        return mRepeatType;
    }

    public void setRepeatType(int repeatType) {
        mRepeatType = repeatType;
    }

    public static final Creator<TrainingModel> CREATOR = new Creator<TrainingModel>() {
        @Override
        public TrainingModel createFromParcel(Parcel parcel) {
            return new TrainingModel(parcel);
        }

        @Override
        public TrainingModel[] newArray(int i) {
            return new TrainingModel[0];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getName());
        parcel.writeInt(getType());
        parcel.writeInt(getCount());
        parcel.writeSerializable(getDate());
        parcel.writeString(getTime());
        parcel.writeInt(mAbonentNum);
        parcel.writeInt(mTypeTraining);
        parcel.writeInt(mRepeatType);
    }
}
