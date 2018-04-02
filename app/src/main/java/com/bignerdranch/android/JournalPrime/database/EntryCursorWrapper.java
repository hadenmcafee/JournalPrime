package com.bignerdranch.android.JournalPrime.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.media.Image;

import com.bignerdranch.android.JournalPrime.Entry;
import com.bignerdranch.android.JournalPrime.database.EntryDbSchema.EntryTable;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

public class EntryCursorWrapper extends CursorWrapper {

    public EntryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Entry getEntry() {
        String uuidString = getString(getColumnIndex(EntryTable.Cols.UUID));
        String title = getString(getColumnIndex(EntryTable.Cols.TITLE));
        long date = getLong(getColumnIndex(EntryTable.Cols.DATE));
        long time = getLong(getColumnIndex(EntryTable.Cols.TIME));
        String mood = getString(getColumnIndex(EntryTable.Cols.MOOD));
        String journal_entry = getString(getColumnIndex(EntryTable.Cols.JOURNAL_ENTRY));
        String temp = getString(getColumnIndex(EntryTable.Cols.TEMP));
        String location = getString(getColumnIndex(EntryTable.Cols.LOCATION));
        String sky = getString(getColumnIndex(EntryTable.Cols.SKY));
//        String image_one = getString(getColumnIndex(EntryTable.Cols.IMAGE_ONE));
//        String image_two = getString(getColumnIndex(EntryTable.Cols.IMAGE_TWO));
//        String image_three = getString(getColumnIndex(EntryTable.Cols.IMAGE_THREE));

        Entry entry = new Entry(UUID.fromString(uuidString));
        entry.setTitle(title);
        entry.setDate(new Date(date));
        entry.setTime(new Time(time));
        entry.setMood(mood);
        entry.setJournal_entry(journal_entry);
        entry.setTemp(temp);
        entry.setLocation(location);
        entry.setSky(sky);
//        entry.setImage_one(image_one);
//        entry.setImage_two(image_two);
//        entry.setImage_three(image_three);

        return entry;
    }

    public Image getImage(){

    }
}
