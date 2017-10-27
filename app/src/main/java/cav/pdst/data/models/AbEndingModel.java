package cav.pdst.data.models;


import android.util.Log;

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

    // проверяем только по имени спортсмена остальное нафиг
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
            AbEndingModel tmp = (AbEndingModel) obj;
            //if (tmp.getSportsman().equals(this.mSportsman)) return true;
            if (tmp.getSpId() == this.mSpId){
                //Log.d("ABE","True "+this.mSpId+" "+tmp.mSpId);
                return true;
            }
            else return false;
        }
    }


    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + mSpId;
        return result;
    }
}