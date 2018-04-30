package com.bignerdranch.android.JournalPrime;

/**
 * Created by ad939564 on 4/11/2018.
 */

//model class from darksky
public class DarkSkyItem {
    private String mSkyDescription;
    private String mSkyIcon;
    private String mTemp;

    @Override
    public String toString(){
        return mSkyDescription;
    }


    public String getmSkyDescription() {
        return mSkyDescription;
    }

    public void setmSkyDescription(String mSky) {
        this.mSkyDescription = mSky;
    }

    public String getmTemp() {
        return mTemp;
    }

    public void setmTemp(String mTemp) {
        this.mTemp = mTemp;
    }

    public String getmSkyIcon() {
        return mSkyIcon;
    }

    public void setmSkyIcon(String skyIcon) {
        mSkyIcon = skyIcon;
    }
}
