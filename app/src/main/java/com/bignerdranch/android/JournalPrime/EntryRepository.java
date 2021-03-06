package com.bignerdranch.android.JournalPrime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bignerdranch.android.JournalPrime.database.EntryBaseHelper;
import com.bignerdranch.android.JournalPrime.database.EntryCursorWrapper;
import com.bignerdranch.android.JournalPrime.database.EntryDbSchema.EntryTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bignerdranch.android.JournalPrime.database.EntryDbSchema.EntryTable.Cols.*;

public class EntryRepository {
    private static EntryRepository sEntryRepository;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static String TAG = "EntryRepository";

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
//        Log.d(TAG, c.toString());
        ContentValues values = getContentValues(c);
        mDatabase.insert(EntryTable.NAME, null, values);
    }

    public List<Entry> getEntries() {
        List<Entry> entries = new ArrayList<>();
        EntryCursorWrapper cursor = queryEntries(null, null);
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
        EntryCursorWrapper cursor = queryEntries(
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

    //    finding photo file location
    public File getPhotoFile(Entry entry){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, entry.getPhotoFilename());
    }

    public void updateEntry(Entry entry) {
        String uuidString = entry.getId().toString();
        ContentValues values = getContentValues(entry);
        mDatabase.update(EntryTable.NAME, values,
                EntryTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void deleteEntry(Entry entry)
    {
        String uuidString = entry.getId().toString();
        mDatabase.delete(EntryTable.NAME, EntryTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private EntryCursorWrapper queryEntries(String whereClause, String[] whereArgs) {
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
        values.put(DATE, entry.getDate());
        values.put(TIME, entry.getTime());
        values.put(JOURNAL_ENTRY, entry.getEntryContent());
        values.put(TEMP, entry.getTemp());
        values.put(LOCATION, entry.getLocation());
        values.put(SKY_DESCRIPTION, entry.getSkyDescription());
        values.put(SKY_ICON_TEXT, entry.getSkyIconText());

        return values;
    }
}
