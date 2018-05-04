package com.bignerdranch.android.JournalPrime.database;

public class EntryDbSchema {
    public static final class EntryTable {
        public static final String NAME = "entries";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String TIME = "time";
            //            public static final String MOOD = "mood";
            public static final String JOURNAL_ENTRY = "journal_entry";
            public static final String TEMP = "temperature";
            public static final String LOCATION = "location";
            public static final String SKY_DESCRIPTION = "sky_description";
            public static final String SKY_ICON_TEXT = "sky_icon_text";

        }
    }

//    public static final class ImageTable{
//        public static final String NAME = "images";
//
//        public static final class Cols {
//            public static final String UUID = "uuid";
//            public static final String ENTRY_ID = "corresponding_entry";
//            public static final String IMAGE_ONE = "image";
//        }
//    }
}