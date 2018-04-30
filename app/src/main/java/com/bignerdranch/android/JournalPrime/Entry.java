package com.bignerdranch.android.JournalPrime;

import android.util.Log;

import java.util.Calendar;
import java.sql.Time;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class Entry {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private Time mTime;
    private String mMood;
    private String mEntryContent;
    private String mTemp;
    private String mLocation;
    private String mSkyDescription;
    private String mSkyIconText;
//    private String mImage_one;
//    private String mImage_two;
//    private String mImage_three;

    //private Calendar mCalendar;

    public Entry() {
        this(UUID.randomUUID());
    }

    public Entry(UUID id) {
        mId = id;
        mDate = new Date();
        mTime = new Time(Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis());
        mSkyDescription = "";
        mSkyIconText = "";
    }

    public Entry(UUID id, String skyDescription, String skyIconText, String temp)
    {
        mId = id;
        mDate = new Date();
        mTime = new Time(Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis());
        mSkyDescription = skyDescription;
        mSkyIconText = skyIconText;
        mTemp = temp;
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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Time getTime() {return mTime;}

    public void setTime(Time time){mTime = time;}

//    public String getMood() {return mMood;}
//
//    public void setMood(String mood){mMood = mood;}

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

//    public String getImage_one() {
//        return mImage_one;
//    }
//
//    public void setImage_one(String image_one) {
//        mImage_one = image_one;
//    }
//
//    public String getImage_two() {
//        return mImage_two;
//    }
//
//    public void setImage_two(String image_two) {
//        mImage_two = image_two;
//    }
//
//    public String getImage_three() {
//        return mImage_three;
//    }
//
//    public void setImage_three(String image_three) {
//        mImage_three = image_three;
//    }

    public String toString()
    {
        return "\n\nTitle: " + mTemp + ",\n" +
                "Date: " + mDate.toString() + ",\n" +
                "Time: " + mTime.toString();

                /*"Mood: " + mTemp + ",\n" +
                "Sky: " + mTemp + ",\n" +
                "Entry: " + mTemp + ",\n" +
                "Mood: " + mTemp + ",\n" +
                "Mood: " + mTemp + ",\n" +
                "Mood: " + mTemp + ",\n" +
                "Mood: " + mTemp + ",\n" +

                private UUID mId;
                private String mTitle;
                private Date mDate;
                private Time mTime;
                private String mMood;
                private String mEntryContent;
                private String mTemp;
                private String mLocation;
                private String mSkyDescription;
                private String mImage_one;
                private String mImage_two;
                private String mImage_three;
                 */
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
