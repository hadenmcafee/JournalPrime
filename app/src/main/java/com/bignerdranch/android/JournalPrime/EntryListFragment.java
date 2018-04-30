package com.bignerdranch.android.JournalPrime;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public class EntryListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mEntryRecyclerView;
    private EntryAdapter mAdapter;
    private boolean mSubtitleVisible;

    //variables to store API results
    private String apiTempText;
    private String apiSkyDescriptionText;
    private String apiSkyIconText;

    public static String TAG = "TROUBLESHOOT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry_list, container, false);

        mEntryRecyclerView = (RecyclerView) view
                .findViewById(R.id.entry_recycler_view);
        mEntryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_entry_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_entry:
            {
                //call the API to get the current weather - WAIT for thread to complete
                new FetchItemsTask().execute();
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                Log.e(TAG, "prior to object create, API results = skyDescr: " + apiSkyDescriptionText + ", skyIcon: " + apiSkyIconText + ", temp: " + apiTempText);
                Entry entry = new Entry(UUID.randomUUID(), apiSkyDescriptionText, apiSkyIconText, apiTempText);
//                Log.d(TAG, entry.toString());
                //EntryRepository.get(getActivity()).addEntry(entry);
                EntryRepository.get(getActivity()).addEntry(entry);
                Intent intent = EntryPagerActivity
                        .newIntent(getActivity(), entry.getId());
                startActivity(intent);
                return true;
            }
            case R.id.show_subtitle: {
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void updateSubtitle() {
        EntryRepository entryRepository = EntryRepository.get(getActivity());
        int entryCount = entryRepository.getEntries().size();
        String subtitle = getString(R.string.subtitle_format, entryCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        EntryRepository entryRepository = EntryRepository.get(getActivity());
        List<Entry> entries = entryRepository.getEntries();

        if (mAdapter == null) {
            mAdapter = new EntryAdapter(entries);
            mEntryRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setEntries(entries);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class EntryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Entry mEntry;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mFirstLineContentView;
        private ImageView mSkyIconImageView;
        private TextView mTempTextView;

        public EntryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_entry, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.entry_list_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.entry_list_date);
            mFirstLineContentView = (TextView) itemView.findViewById(R.id.entry_list_content);
            mSkyIconImageView = (ImageView) itemView.findViewById(R.id.entry_list_sky);
            mTempTextView = (TextView) itemView.findViewById(R.id.entry_list_temp);
        }

        public void bind(Entry entry) {
            DateFormat formatter = new SimpleDateFormat("EEE, MM/dd/yyyy");

            mEntry = entry;
            mTitleTextView.setText(mEntry.getTitle());
            mDateTextView.setText(formatter.format(mEntry.getDate())); //mDateButton.setText(mEntry.getDate().toString());
            mFirstLineContentView.setText(mEntry.getEntryContent());
            mTempTextView.setText(mEntry.getTemp());
            mSkyIconImageView.setImageResource(Entry.getSkyImageIndex(mEntry.getSkyIconText()));

            //mSkyIconImageView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View view) {
            Intent intent = EntryPagerActivity.newIntent(getActivity(), mEntry.getId());
            startActivity(intent);
        }
    }

    private class EntryAdapter extends RecyclerView.Adapter<EntryHolder> {

        private List<Entry> mEntries;

        public EntryAdapter(List<Entry> entries) {
            mEntries = entries;
        }

        @Override
        public EntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new EntryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(EntryHolder holder, int position) {
            Entry entry = mEntries.get(position);
            holder.bind(entry);
        }

        @Override
        public int getItemCount() {
            return mEntries.size();
        }

        public void setEntries(List<Entry> entries) {
            mEntries = entries;
        }
    }

    //    Writing an AsyncTask
    private class FetchItemsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params){
            List<DarkSkyItem> items = new DarkSkyFetchr().fetchItems();
            DarkSkyItem item = items.get(0);

            //get the weather description, icon text, and temperature from the API call
            apiSkyDescriptionText = item.getmSkyDescription();
            apiSkyIconText = item.getmSkyIcon();
            apiTempText = item.getmTemp();

            return null;
        }
    }
}
