package cav.pdst.data.models;

public class RateTypeSpinerModel {
    private int mId;
    private String mName;

    public RateTypeSpinerModel(int id, String name) {
        mId = id;
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return mName;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == this)
            return true;
     /* obj ссылается на null */

        if(obj == null)
            return false;
        /* Удостоверимся, что ссылки имеют тот же самый тип */
        if(!(getClass() == obj.getClass())) {
            return false;
        }else {
            RateTypeSpinerModel tmp = (RateTypeSpinerModel) obj;
            if (tmp.mId == this.mId && tmp.getName().equals(this.mName)) return true;
            else return false;
        }
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + mName.hashCode();
        return result;
    }
}
