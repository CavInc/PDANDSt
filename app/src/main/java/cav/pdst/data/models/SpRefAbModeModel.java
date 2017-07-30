package cav.pdst.data.models;


public class SpRefAbModeModel {
    private int mSpId;
    private int mAbonement;
    private int mMode;

    public SpRefAbModeModel(int spId, int abonement, int mode) {
        mSpId = spId;
        mAbonement = abonement;
        mMode = mode;
    }

    public int getSpId() {
        return mSpId;
    }

    public int getAbonement() {
        return mAbonement;
    }

    public int getMode() {
        return mMode;
    }
}
