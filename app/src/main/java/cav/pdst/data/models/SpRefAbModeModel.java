package cav.pdst.data.models;


public class SpRefAbModeModel {
    private int mSpId;
    private int mAbonement;
    private int mMode;
    private int mCount;

    public SpRefAbModeModel(int spId, int abonement, int mode) {
        mSpId = spId;
        mAbonement = abonement;
        mMode = mode;
    }

    public SpRefAbModeModel(int spId, int abonement, int mode, int count) {
        mSpId = spId;
        mAbonement = abonement;
        mMode = mode;
        mCount = count;
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

    public int getCount() {
        return mCount;
    }

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
            SpRefAbModeModel tmp = (SpRefAbModeModel) obj;
            if (tmp.getSpId() == this.getSpId()) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + mSpId+mAbonement;
        return result;
    }

}
