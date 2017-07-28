package cav.pdst.data.models;


public class TrainingGroupModel {
    private int mId;
    private String mName;

    public TrainingGroupModel(int id, String name) {
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
}
