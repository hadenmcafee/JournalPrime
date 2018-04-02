package com.bignerdranch.android.JournalPrime.database;

public class EntryDbSchema {
    public static final class EntryTable {
        public static final String NAME = "entries";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String TIME = "time";
            public static final String MOOD = "mood";
            public static final String JOURNAL_ENTRY = "journal_entry";
            public static final String TEMP = "temp";
            public static final String LOCATION = "location";
            public static final String SKY = "sky";
            public static final String IMAGE_ONE = "image_one";
            public static final String IMAGE_TWO = "image_two";
            public static final String IMAGE_THREE = "image_three";
        }
    }
}