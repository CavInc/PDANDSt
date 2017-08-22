package cav.pdst.data.models;

public class AlarmAbonementModel {
    private int mId;
    private int mSpId;
    private int mPostId;
    private String mDate;
    private String mSportsmanName;


    public AlarmAbonementModel(int id, int spId, int postId, String date, String sportsmanName) {
        mId = id;
        mSpId = spId;
        mPostId = postId;
        mDate = date;
        mSportsmanName = sportsmanName;
    }

    public int getId() {
        return mId;
    }

    public int getSpId() {
        return mSpId;
    }

    public int getPostId() {
        return mPostId;
    }

    public String getDate() {
        return mDate;
    }

    public String getSportsmanName() {
        return mSportsmanName;
    }
}