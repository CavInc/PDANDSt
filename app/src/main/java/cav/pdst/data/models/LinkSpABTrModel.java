package cav.pdst.data.models;


public class LinkSpABTrModel {
    private int mSportsman;
    private int mAbonement; // или же тут список абонементов

    public LinkSpABTrModel(int sportsman, int abonement) {
        mSportsman = sportsman;
        mAbonement = abonement;
    }

    public int getSportsman() {
        return mSportsman;
    }

    public int getAbonement() {
        return mAbonement;
    }
}
