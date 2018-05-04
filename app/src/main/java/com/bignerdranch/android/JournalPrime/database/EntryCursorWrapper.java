package com.bignerdranch.android.JournalPrime.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.JournalPrime.Entry;
import com.bignerdranch.android.JournalPrime.database.EntryDbSchema.EntryTable;

import java.util.UUID;

public class EntryCursorWrapper extends CursorWrapper {

    public EntryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Entry getEntry() {
        String uuidString = getString(getColumnIndex(EntryTable.Cols.UUID_ENTRY));
        String title = getString(getColumnIndex(EntryTable.Cols.TITLE));
        long date = getLong(getColumnIndex(EntryTable.Cols.DATE));
        long time = getLong(getColumnIndex(EntryTable.Cols.TIME));
        String journal_entry = getString(getColumnIndex(EntryTable.Cols.JOURNAL_ENTRY));
        String temp = getString(getColumnIndex(EntryTable.Cols.TEMP));
        String location = getString(getColumnIndex(EntryTable.Cols.LOCATION));
        String sky_description = getString(getColumnIndex(EntryTable.Cols.SKY_DESCRIPTION));
        String sky_icon_text = getString(getColumnIndex(EntryTable.Cols.SKY_ICON_TEXT));

        Entry entry = new Entry(UUID.fromString(uuidString));
        entry.setTitle(title);
        entry.setDate(date);
        entry.setTime(time);
        entry.setEntryContent(journal_entry);
        entry.setTemp(temp);
        entry.setLocation(location);
        entry.setSkyDescription(sky_description);
        entry.setSkyIconText(sky_icon_text);

        return entry;
    }

}
