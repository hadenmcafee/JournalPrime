package com.bignerdranch.android.JournalPrime;

/**
 * Created by ad939564 on 4/11/2018.
 */

//model class from darksky
public class DarkSkyItem {
    private String mSky;
    private String mTemp;

    @Override
    public String toString(){
        return mSky;
    }


    public String getmSky() {
        return mSky;
    }

    public void setmSky(String mSky) {
        this.mSky = mSky;
    }

    public String getmTemp() {
        return mTemp;
    }

    public void setmTemp(String mTemp) {
        this.mTemp = mTemp;
    }
}
