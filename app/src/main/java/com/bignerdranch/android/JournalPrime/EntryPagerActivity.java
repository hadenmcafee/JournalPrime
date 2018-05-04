package com.bignerdranch.android.JournalPrime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;
import java.util.UUID;

public class EntryPagerActivity extends AppCompatActivity {

    private static final String EXTRA_ENTRY_ID =
            "com.bignerdranch.android.JournalPrime.entry_id";
    private static final String TAG = "EntryPagerActivity";

    private ViewPager mViewPager;
    private List<Entry> mEntries;

    public static Intent newIntent(Context packageContext, UUID entryId) {
        Log.d(TAG, "Inside newIntent function");
        Intent intent = new Intent(packageContext, EntryPagerActivity.class);
        intent.putExtra(EXTRA_ENTRY_ID, entryId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Inside onCreate function");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_pager);

        UUID entryId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_ENTRY_ID);

        mViewPager = (ViewPager) findViewById(R.id.entry_view_pager);

        mEntries = EntryRepository.get(this).getEntries();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Log.d(TAG, "Inside getItem function");
                Entry entry = mEntries.get(position);
                return EntryFragment.newInstance(entry.getId());
            }

            @Override
            public int getCount()
            {
                Log.d(TAG, "Inside getCount function");
                return mEntries.size();
            }
        });

        Log.d(TAG, "Beginning for loop");
        for (int i = 0; i < mEntries.size(); i++) {
            if (mEntries.get(i).getId().equals(entryId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
