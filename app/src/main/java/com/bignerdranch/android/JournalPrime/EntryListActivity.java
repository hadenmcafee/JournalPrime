package com.bignerdranch.android.JournalPrime;

import android.support.v4.app.Fragment;

public class EntryListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new EntryListFragment();
    }
}
