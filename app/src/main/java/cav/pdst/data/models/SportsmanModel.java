package cav.pdst.data.models;


public class SportsmanModel {
    private String mName;
    private String mTel;
    private int mTrainingAll;
    private int mTraninigWrk;
    private String mComment;

    public SportsmanModel(String name, String tel, int trainingAll, int traninigWrk, String comment) {
        mName = name;
        mTel = tel;
        mTrainingAll = trainingAll;
        mTraninigWrk = traninigWrk;
        mComment = comment;
    }

    public String getName() {
        return mName;
    }

    public String getTel() {
        return mTel;
    }

    public int getTrainingAll() {
        return mTrainingAll;
    }

    public int getTraninigWrk() {
        return mTraninigWrk;
    }

    public String getComment() {
        return mComment;
    }
}
