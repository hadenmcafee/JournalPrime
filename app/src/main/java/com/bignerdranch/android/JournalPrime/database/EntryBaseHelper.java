package com.bignerdranch.android.JournalPrime.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.JournalPrime.database.EntryDbSchema.EntryTable;

public class EntryBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "entryBase.db";

    public EntryBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + EntryTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        EntryTable.Cols.UUID + ", " +
                        EntryTable.Cols.TITLE + ", " +
                        EntryTable.Cols.DATE + ", " +
                        EntryTable.Cols.TIME + ", " +
                        EntryTable.Cols.JOURNAL_ENTRY + ", " +
                        EntryTable.Cols.TEMP + ", " +
                        EntryTable.Cols.LOCATION + ", " +
                        EntryTable.Cols.SKY_DESCRIPTION + ", " +
                        EntryTable.Cols.SKY_ICON_TEXT +")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}