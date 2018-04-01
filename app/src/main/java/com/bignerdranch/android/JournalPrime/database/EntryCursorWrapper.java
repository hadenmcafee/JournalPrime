package com.bignerdranch.android.JournalPrime.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.JournalPrime.Entry;
import com.bignerdranch.android.JournalPrime.database.EntryDbSchema.EntryTable;

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
        int isSolved = getInt(getColumnIndex(EntryTable.Cols.SOLVED));

        Entry entry = new Entry(UUID.fromString(uuidString));
        entry.setTitle(title);
        entry.setDate(new Date(date));
        entry.setSolved(isSolved != 0);

        return entry;
    }
}
