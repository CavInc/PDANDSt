package cav.pdst.data.models;

import java.util.Date;

public class AbEndingModel {
    private String mSportsman;
    private int mAbonementNum;
    private int mAbonementID;
    private Date mStart;
    private Date mEnd;
    private int mNoUseCount;
    private int mNoUseWorking;
    private String mTypeEnding;
    private int mSpId;

    public AbEndingModel(String sportsman,int spId, int abonementNum, int abonementID, Date start, Date end, int noUseCount, int noUseWorking) {
        mSportsman = sportsman;
        mSpId = spId;
        mAbonementNum = abonementNum;
        mAbonementID = abonementID;
        mStart = start;
        mEnd = end;
        mNoUseCount = noUseCount;
        mNoUseWorking = noUseWorking;
        mTypeEnding = "Остались тренировки";
    }

    public AbEndingModel(String sportsman,int spId, int abonementNum, int abonementID, Date start, Date end, int noUseCount, int noUseWorking, String typeEnding) {
        mSportsman = sportsman;
        mSpId = spId;
        mAbonementNum = abonementNum;
        mAbonementID = abonementID;
        mStart = start;
        mEnd = end;
        mNoUseCount = noUseCount;
        mNoUseWorking = noUseWorking;
        mTypeEnding = typeEnding;
    }

    public String getSportsman() {
        return mSportsman;
    }

    public int getAbonementNum() {
        return mAbonementNum;
    }

    public int getAbonementID() {
        return mAbonementID;
    }

    public Date getStart() {
        return mStart;
    }

    public Date getEnd() {
        return mEnd;
    }

    public int getNoUseCount() {
        return mNoUseCount;
    }

    public String getTypeEnding() {
        return mTypeEnding;
    }

    public int getSpId() {
        return mSpId;
    }
}