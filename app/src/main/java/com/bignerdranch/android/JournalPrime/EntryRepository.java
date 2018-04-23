package com.bignerdranch.android.JournalPrime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.JournalPrime.database.EntryBaseHelper;
import com.bignerdranch.android.JournalPrime.database.EntryCursorWrapper;
import com.bignerdranch.android.JournalPrime.database.EntryDbSchema.EntryTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bignerdranch.android.JournalPrime.database.EntryDbSchema.EntryTable.Cols.*;

public class EntryRepository {
    private static EntryRepository sEntryRepository;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static EntryRepository get(Context context) {
        if (sEntryRepository == null) {
            sEntryRepository = new EntryRepository(context);
        }

        return sEntryRepository;
    }

    private EntryRepository(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new EntryBaseHelper(mContext)
                .getWritableDatabase();

    }

    public void addEntry(Entry c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(EntryTable.NAME, null, values);
    }

    public List<Entry> getEntries() {
        List<Entry> entries = new ArrayList<>();
        EntryCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                entries.add(cursor.getEntry());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return entries;
    }

    public Entry getEntry(UUID id) {
        EntryCursorWrapper cursor = queryCrimes(
                EntryTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getEntry();
        } finally {
            cursor.close();
        }
    }

    public void updateEntry(Entry entry) {
        String uuidString = entry.getId().toString();
        ContentValues values = getContentValues(entry);
        mDatabase.update(EntryTable.NAME, values,
                EntryTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private EntryCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                EntryTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new EntryCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Entry entry) {
        ContentValues values = new ContentValues();
        values.put(UUID, entry.getId().toString());
        values.put(TITLE, entry.getTitle());
        values.put(DATE, entry.getDate().getTime());
        values.put(TIME, entry.getTime().getTime());
        values.put(MOOD, entry.getMood());
        values.put(JOURNAL_ENTRY, entry.getJournal_entry());
        values.put(TEMP, entry.getTemp());
        values.put(LOCATION, entry.getLocation());
        values.put(SKY, entry.getSky());
//        values.put(IMAGE_ONE, entry.getImage_one());
//        values.put(IMAGE_TWO, entry.getImage_two());
//        values.put(IMAGE_THREE, entry.getImage_three());

        return values;
    }


}
