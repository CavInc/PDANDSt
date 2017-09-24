package cav.pdst.data.models;

public class TestAbonementModel {
    private int mId;
    private int mWorking; // отработки
    private int mTraining; // тренировки


    public TestAbonementModel(int id, int working,int training) {
        mId = id;
        mWorking = working;
        mTraining = training;
    }

    public int getId() {
        return mId;
    }

    public int getWorking() {
        return mWorking;
    }

    public int getTraining() {
        return mTraining;
    }
}