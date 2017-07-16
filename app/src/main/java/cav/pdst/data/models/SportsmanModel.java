package cav.pdst.data.models;


public class SportsmanModel {
    private int mId;
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

    public SportsmanModel(int id, String name, String tel, int trainingAll, int traninigWrk, String comment) {
        mId = id;
        mName = name;
        mTel = tel;
        mTrainingAll = trainingAll;
        mTraninigWrk = traninigWrk;
        mComment = comment;
    }

    public int getId() {
        return mId;
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
