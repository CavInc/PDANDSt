package cav.pdst.data.models;


import android.text.format.Time;

import java.util.Date;

public class TrainingModel {
    private int mId;
    private String mName;
    private int mType;
    private int mCount;
    private Date mDate;
    private String mTime;

    public TrainingModel(String name, int type, int count, Date date) {
        mName = name;
        mType = type;
        mCount = count;
        mDate = date;
    }

    public TrainingModel(String name, int type, int count, Date date, String time) {
        mName = name;
        mType = type;
        mCount = count;
        mDate = date;
        mTime = time;
    }

    public TrainingModel(int id, String name, int type, int count, Date date, String time) {
        mId = id;
        mName = name;
        mType = type;
        mCount = count;
        mDate = date;
        mTime = time;
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
}
