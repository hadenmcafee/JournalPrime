package com.bignerdranch.android.JournalPrime;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bignerdranch.android.JournalPrime.database.EntryDbSchema.EntryTable.Cols.*;

public class EntryRepository
{
    private static EntryRepository sEntryRepository;
    private Context mContext;
    private final FirebaseDatabase mDatabase;
//    private SQLiteDatabase mDatabase;

    public static String TAG = "EntryRepository";

    public static EntryRepository get(Context context) {
        Log.d(TAG, "Inside EntryRepository get constructor function");
        if (sEntryRepository == null) {
            sEntryRepository = new EntryRepository(context);
        }

        return sEntryRepository;
    }

    private EntryRepository(Context context) {
        Log.d(TAG, "Inside EntryRepository constructor");
        mContext = context.getApplicationContext();
        mDatabase =  FirebaseDatabase.getInstance();
//        mDatabase = new EntryBaseHelper(mContext)
//                .getWritableDatabase();

    }

    public void addEntry(Entry c)
    {
        Log.d(TAG, "Inside addEntry function");

        //writing to Firebase
        UUID entryId = c.getId();
        DatabaseReference mRef =  mDatabase.getReference().child("Entries").child(String.valueOf(entryId));
        mRef.child("title").setValue(c.getTitle());
        mRef.child("date").setValue(c.getDate());
        Log.d(TAG, "time = " + c.getTime());
        mRef.child("time").setValue(c.getTime());
        mRef.child("entryContent").setValue(c.getEntryContent());
        mRef.child("temp").setValue(c.getTemp());
        mRef.child("location").setValue(c.getLocation());
        mRef.child("skyDescription").setValue(c.getSkyDescription());

        ValueEventListener entryListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Inside onDataChanged function, eventListener");
                // Get Entry object and use the values to update the UI
                Entry entry = dataSnapshot.getValue(Entry.class);
                // TODO
                Log.d(TAG, "entry = " + entry.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Entry failed, log a message
                Log.w(TAG, "loadEntry:onCancelled", databaseError.toException());
                // ...
            }
        };
        mRef.addValueEventListener(entryListener);

//        Log.d(TAG, c.toString());

//        ContentValues values = getContentValues(c);
//        mDatabase.insert(EntryTable.NAME, null, values);
    }

    public List<Entry> getEntries() {
        Log.d(TAG, "Inside getEntries function");
        final List<Entry> entries = new ArrayList<>();

        DatabaseReference ref = mDatabase.getReference("Entries");

        // Attach a listener to read the data at our posts reference
        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.d(TAG, "Inside onDataChange function, getEntries");
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Entry entry = new Entry(UUID.fromString(snapshot.getKey()));
                    Entry tempEntry = snapshot.getValue(Entry.class);

                    entry.setTitle(tempEntry.getTitle());
                    entry.setDate(tempEntry.getDate());
                    entry.setTime(tempEntry.getTime());
                    entry.setTemp(tempEntry.getTemp());
                    entry.setEntryContent(tempEntry.getEntryContent());
                    entry.setSkyIconText(tempEntry.getSkyIconText());
                    entry.setLocation(tempEntry.getLocation());

                    entries.add(entry);
                    Log.e(TAG, "Just updated 1 entry; total entries = " + entries.size());
                    Log.d(TAG, "entry = ");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Inside onCancelled (Single Valued Event) function");
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
//        EntryCursorWrapper cursor = queryEntries(null, null);
//        try {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                entries.add(cursor.getEntry());
//                cursor.moveToNext();
//            }
//        } finally {
//            cursor.close();
//        }
        return entries;
    }

    public Entry getEntry(UUID id)
    {
        Log.d(TAG, "Inside getEntry function");
        final List<Entry> entries = new ArrayList<>();
        DatabaseReference ref =  mDatabase.getReference().child("Entries").child(String.valueOf(id));

        // Attach a listener to read the data at our posts reference
        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.d(TAG, "Inside onDataChange function");
                Entry entry = new Entry(UUID.fromString(dataSnapshot.getKey()));
                Entry tempEntry = dataSnapshot.getValue(Entry.class);

                entry.setTitle(tempEntry.getTitle());
                entry.setDate(tempEntry.getDate());
                entry.setTime(tempEntry.getTime());
                entry.setTemp(tempEntry.getTemp());
                entry.setEntryContent(tempEntry.getEntryContent());
                entry.setSkyIconText(tempEntry.getSkyIconText());
                entry.setLocation(tempEntry.getLocation());

                entries.add(entry);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Inside onCancelled function");
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return entries.get(0);
        /*EntryCursorWrapper cursor = queryEntries(
                EntryTable.Cols.UUID_ENTRY + " = ?",
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
        }*/
    }

    //    finding photo file location
    public File getPhotoFile(Entry entry){
        Log.d(TAG, "Inside getPhotoFile function");
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, entry.getPhotoFilename());
    }

    public void updateEntry(Entry entry)
    {
        Log.d(TAG, "Inside updateEntry function, getEntry");
        UUID entryId = entry.getId();
        DatabaseReference mRef =  mDatabase.getReference().child("Entries").child(String.valueOf(entryId));
        mRef.child("title").setValue(entry.getTitle());
        mRef.child("date").setValue(entry.getDate());
        mRef.child("time").setValue(entry.getTime());
        mRef.child("entryContent").setValue(entry.getEntryContent());
        mRef.child("temp").setValue(entry.getTemp());
        mRef.child("location").setValue(entry.getLocation());
        mRef.child("skyDescription").setValue(entry.getSkyDescription());
        /*String uuidString = entry.getId().toString();
        ContentValues values = getContentValues(entry);
        mDatabase.update(EntryTable.NAME, values,
                EntryTable.Cols.UUID_ENTRY + " = ?",
                new String[]{uuidString});*/
    }

    public void deleteEntry(Entry entry)
    {
        Log.d(TAG, "Inside deleteEntry0 function");
        //delete record from Firebase
        UUID entryId = entry.getId();

        DatabaseReference mRef =  mDatabase.getReference().child("Entries").child(String.valueOf(entryId));
        mRef.removeValue();

//        String uuidString = entry.getId().toString();
//        mDatabase.delete(EntryTable.NAME, EntryTable.Cols.UUID_ENTRY + " = ?",
//                new String[]{uuidString});
    }

    /*private EntryCursorWrapper queryEntries(String whereClause, String[] whereArgs) {
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
        values.put(UUID_ENTRY, entry.getId().toString());
        values.put(TITLE, entry.getTitle());
        values.put(DATE, entry.getDate());
        values.put(TIME, entry.getTime());
        values.put(JOURNAL_ENTRY, entry.getEntryContent());
        values.put(TEMP, entry.getTemp());
        values.put(LOCATION, entry.getLocation());
        values.put(SKY_DESCRIPTION, entry.getSkyDescription());
        values.put(SKY_ICON_TEXT, entry.getSkyIconText());

        return values;
    }*/
}
