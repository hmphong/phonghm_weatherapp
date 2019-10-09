package com.example.phonghm_weatherapp.model;

public class City {
    private String mName;
    private String mIndex;
    private String mRanking;
    private String mTime;


    public City() {
    }

    public City(String mName, String mIndex, String mRanking, String mTime) {
        this.mName = mName;
        this.mIndex = mIndex;
        this.mRanking = mRanking;
        this.mTime = mTime;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmIndex() {
        return mIndex;
    }

    public void setmIndex(String mIndex) {
        this.mIndex = mIndex;
    }

    public String getmRanking() {
        return mRanking;
    }

    public void setmRanking(String mRanking) {
        this.mRanking = mRanking;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }
}
