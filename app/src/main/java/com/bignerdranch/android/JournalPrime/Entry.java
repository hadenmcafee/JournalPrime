package com.bignerdranch.android.JournalPrime;

import android.location.Location;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

public class Entry {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private Time mTime;
    private String mMood;
    private String mJournal_entry;
    private String mTemp;
    private String mLocation;
    private String mSky;
    private String mImage_one;
    private String mImage_two;
    private String mImage_three;

    public Entry() {
        this(UUID.randomUUID());
    }

    public Entry(UUID id) {
        mId = id;
        mDate = new Date();
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

    public String getMood() {return mMood;}

    public void setMood(String mood){mMood = mood;}

    public String getJournal_entry() {
        return mJournal_entry;
    }

    public void setJournal_entry(String journal_entry) {
        mJournal_entry = journal_entry;
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

    public String getSky() {
        return mSky;
    }

    public void setSky(String sky) {
        mSky = sky;
    }

    public String getImage_one() {
        return mImage_one;
    }

    public void setImage_one(String image_one) {
        mImage_one = image_one;
    }

    public String getImage_two() {
        return mImage_two;
    }

    public void setImage_two(String image_two) {
        mImage_two = image_two;
    }

    public String getImage_three() {
        return mImage_three;
    }

    public void setImage_three(String image_three) {
        mImage_three = image_three;
    }
}
