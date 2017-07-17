package cav.pdst.data.models;


import android.os.Parcel;
import android.os.Parcelable;

public class SportsmanModel implements Parcelable {
    private int mId = -1;
    private String mName = null;
    private String mTel = null;
    private int mTrainingAll;
    private int mTraninigWrk;
    private String mComment= null;

    public SportsmanModel(String name, String tel, int trainingAll, int traninigWrk, String comment) {
        mName = name;
        mTel = tel;
        mTrainingAll = trainingAll;
        mTraninigWrk = traninigWrk;
        mComment = comment;
    }

    public SportsmanModel(int id, String name, String tel, int trainingAll, int traninigWrk, String comment) {
        mId = id;
        mName = name;
        mTel = tel;
        mTrainingAll = trainingAll;
        mTraninigWrk = traninigWrk;
        mComment = comment;
    }

    public SportsmanModel() {

    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getTel() {
        return mTel;
    }

    public int getTrainingAll() {
        return mTrainingAll;
    }

    public int getTraninigWrk() {
        return mTraninigWrk;
    }

    public String getComment() {
        return mComment;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setTel(String tel) {
        mTel = tel;
    }

    public void setTrainingAll(int trainingAll) {
        mTrainingAll = trainingAll;
    }

    public void setTraninigWrk(int traninigWrk) {
        mTraninigWrk = traninigWrk;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mName);
        parcel.writeString(mTel);
        parcel.writeString(mComment);

    }
}
