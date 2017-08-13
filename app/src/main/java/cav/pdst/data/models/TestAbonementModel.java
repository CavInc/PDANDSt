package cav.pdst.data.models;

public class TestAbonementModel {
    private int mId;
    private int mWorking; // отработки

    public TestAbonementModel(int id, int working) {
        mId = id;
        mWorking = working;
    }

    public int getId() {
        return mId;
    }

    public int getWorking() {
        return mWorking;
    }
}