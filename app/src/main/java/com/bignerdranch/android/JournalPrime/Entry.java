package com.bignerdranch.android.JournalPrime;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class Entry {

    private UUID mId;
    private String mTitle;
    private long mDate;
    private long mTime;
    private String mEntryContent;
    private String mTemp;
    private String mLocation;
    private String mSkyDescription;
    private String mSkyIconText;

    public Entry() {
        this(UUID.randomUUID());
    }

    public Entry(UUID id) {
        mId = id;
        mDate = new Date().getTime();
        mTime = Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis();
        mSkyDescription = "";
        mSkyIconText = "";
        mEntryContent = "";
    }

    public Entry(UUID id, String skyDescription, String skyIconText, String temp)
    {
        mId = id;
        mDate = new Date().getTime();
        mTime = Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis();
        mSkyDescription = skyDescription;
        mSkyIconText = skyIconText;
        mTemp = temp;
        mEntryContent = "";
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public long getTime() {return mTime;}

    public void setTime(long time){mTime = time;}

    public String getEntryContent() {
        return mEntryContent;
    }

    public void setEntryContent(String entryContent) {
        mEntryContent = entryContent;
    }

    public String getTemp() {
        return mTemp;
    }

    public void setTemp(String temp) {
        mTemp = temp;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getSkyDescription() {
        return mSkyDescription;
    }

    public void setSkyDescription(String skyDescription) {
        mSkyDescription = skyDescription;
    }


    public String toString()
    {
        return "\n\n" +
                "Title: " + mTitle + ",\n" +
                "Date: " + mDate + ",\n" +
                "Time: " + mTime + "\n" +
                "Location: " + mLocation + "\n" +
                "Sky Description: " + mSkyDescription + ",\n" +
                "Sky Icon: " + mSkyIconText + ",\n" +
                "Temp: " + mTemp + ",\n" +
                "Entry Content: " + mEntryContent + ",\n";
    }

    //    designating a picture location--need to change getId()
    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }

    public static int getSkyImageIndex(String skyIconText)
    {
        int resourceIndex;

        switch (skyIconText)
        {
            case "clear-day":
            {
                resourceIndex=R.drawable.icon_clear_day;
                break;
            }

            case "clear-night":
            {
                resourceIndex=R.drawable.icon_clear_night;
                break;
            }

            case "rain":
            {
                resourceIndex=R.drawable.icon_rain;
                break;
            }

            case "snow":
            {
                resourceIndex=R.drawable.icon_snow;
                break;
            }

            case "sleet":
            {
                resourceIndex=R.drawable.icon_sleet;
                break;
            }

            case "wind":
            {
                resourceIndex=R.drawable.icon_wind;
                break;
            }

            case "fog":
            {
                resourceIndex=R.drawable.icon_fog;
                break;
            }

            case "cloudy":
            {
                resourceIndex=R.drawable.icon_cloudy;
                break;
            }

            case "partly-cloudy-day":
            {
                resourceIndex=R.drawable.icon_partly_cloudy_day;
                break;
            }

            case "partly-cloudy-night":
            {
                resourceIndex=R.drawable.icon_partly_cloudy_night;
                break;
            }

            default:
            {
                Log.e("ERROR", "Error: SKY TEXT NOT FOUND, searching for: " + skyIconText);
                resourceIndex=R.drawable.icon_error;
            }
        }

        return resourceIndex;
    }

    public String getSkyIconText() {
        return mSkyIconText;
    }

    public void setSkyIconText(String skyIconText) {
        mSkyIconText = skyIconText;
    }
}
