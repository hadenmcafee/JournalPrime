package com.bignerdranch.android.JournalPrime;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
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

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class EntryListFragment extends Fragment /*implements LocationListener*/{

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mEntryRecyclerView;
    private EntryAdapter mAdapter;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

//    Required interface for hosting activities
    public interface Callbacks{
        void onEntrySelected(Entry entry);
}

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mActivity = getActivity();

        mLocationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        Log.d(TAG, "Location Manager successfully assigned");

        mCallbacks = (Callbacks) context;
    }

    //variables to store API results
    private boolean apiCallFinished;
    private String apiTempText;
    private String apiSkyDescriptionText;
    private String apiSkyIconText;

    //variables to access GPS location
    Activity mActivity;
    private LocationManager mLocationManager;


    public static String TAG = "EntryListFragment";

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
    public void onDetach(){
        super.onDetach();
        mCallbacks=null;
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
        switch (item.getItemId())
        {
            case R.id.new_entry:
            {
                Entry entry;

                //1. Get the current/last known location
                Pair latLong = null;
                try {
                    latLong = getLatLong();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "getLatLong returned value of " + latLong.toString());

                //2. if the location was successfully obtained, call the API to get the current weather
                if(latLong!=null)
                {
                    new FetchItemsTask(latLong).execute();
                    try {
                        while(!apiCallFinished)
                        {
                            Thread.sleep(250);// WAIT for API call thread to complete
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "prior to object create, API results = skyDescr: " +
                            apiSkyDescriptionText + ", skyIcon: " + apiSkyIconText + ", temp: " + apiTempText);

                    //Create a new entry using the obtained weather information
                    entry = new Entry(UUID.randomUUID(), apiSkyDescriptionText, apiSkyIconText, apiTempText);
                }
                else
                {
                    //Create a new entry using default constructor
                    entry = new Entry();
                }

                //3. Add the entry to the repository
                EntryRepository.get(getActivity()).addEntry(entry);
//                Intent intent = EntryPagerActivity
//                        .newIntent(getActivity(), entry.getId());
//                startActivity(intent);
                updateUI();
                mCallbacks.onEntrySelected(entry);
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

    public void updateUI() {
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

    /*@Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(TAG, "Provider " + provider + " has now status: " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "Provider " + provider + " is enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "Provider " + provider + " is disabled");
    }*/

    private Pair<Double, Double> getLatLong() throws InterruptedException {
        //check permission to access location; if the required permission is not granted, request it
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "App does not have location permission, requesting access...");
//            ActivityCompat.requestPermissions(this.getActivity(), stringToStringArray(Manifest.permission.ACCESS_FINE_LOCATION), 0);
            requestPermissions(stringToStringArray(Manifest.permission.ACCESS_FINE_LOCATION), 0);

            Thread.sleep(4000);
        }

//        //ensure user
//        while(!EntryListActivity.isLocPermissionDetermined())
//        {
//            Thread.sleep(250);
//        }

        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "App has requested permissions");
            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.d(TAG, "Location Manager Created");
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Log.d(TAG, "Location gathered: latitude = " + latitude + ", longitude = " + longitude);

            return new Pair(latitude, longitude);
        }

        return null;

    }

    private String[] stringToStringArray(String givenString)
    {
        String[] stringsArray = {givenString};
        return stringsArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String [] permissions, int[] grantResults)
    {
        if(requestCode == 0)
        {
            Log.d(TAG, "Request code = 0");

            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.d(TAG, "Permission granted");
                EntryListActivity.setLocPermissionGranted(true);
                EntryListActivity.setLocPermissionDetermined(true);
                Log.d(TAG, "PermissionDetermined flag set");
            }
            else
            {
                Log.d(TAG, "Permission not granted");
                EntryListActivity.setLocPermissionGranted(false);
                EntryListActivity.setLocPermissionDetermined(true);
                Log.d(TAG, "PermissionDetermined flag set");

            }
            return;
        }

        Log.d(TAG, "Request code != 0");
    }

    private class EntryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Entry mEntry;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mFirstLineContentView;
        private TextView mTimeTextView;
        private ImageView mSkyIconImageView;
        private TextView mTempTextView;

        public EntryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_entry, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.entry_list_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.entry_list_date);
            mFirstLineContentView = (TextView) itemView.findViewById(R.id.entry_list_content);
            mTimeTextView = (TextView) itemView.findViewById(R.id.entry_list_time);
            mSkyIconImageView = (ImageView) itemView.findViewById(R.id.entry_list_sky);
            mTempTextView = (TextView) itemView.findViewById(R.id.entry_list_temp);
        }

        public void bind(Entry entry) {
            DateFormat formatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy");

            //assign the entry
            mEntry = entry;

            //get the number of characters within the content string, to avoid array index out of bounds exception
            int numCharactersInEntryContent = mEntry.getEntryContent().length();
            int displayIndex = Math.min(numCharactersInEntryContent, 17);

            //get the hour and minute from the entry's time
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            calendar.setTime(new Time(mEntry.getTime()));
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            int ampm = calendar.get(Calendar.AM_PM);
            String am_pm = (ampm == 0 ? "am" : "pm");
//            Log.e(TAG, "AM/PM = " + ampm);

            mTitleTextView.setText(mEntry.getTitle());
            mDateTextView.setText(formatter.format(mEntry.getDate())); //mDateButton.setText(mEntry.getDate().toString());
            mFirstLineContentView.setText(mEntry.getEntryContent().substring(0,displayIndex) + "...");
            mTimeTextView.setText(hour + ":" + minute + " " + am_pm);
            mTempTextView.setText(mEntry.getTemp());
            mSkyIconImageView.setImageResource(Entry.getSkyImageIndex(mEntry.getSkyIconText()));

            //mSkyIconImageView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View view) {
//            Intent intent = EntryPagerActivity.newIntent(getActivity(), mEntry.getId());
////            startActivity(intent);
            mCallbacks.onEntrySelected(mEntry);
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
    private class FetchItemsTask extends AsyncTask<Void, Void, Void>
    {
        private Pair<Double,Double> mLatLong;

        public FetchItemsTask(Pair<Double, Double> latLong)
        {
            mLatLong = latLong;
        }

        @Override
        protected Void doInBackground(Void... params){
            List<DarkSkyItem> items = new DarkSkyFetchr(mLatLong).fetchItems();
            DarkSkyItem item = items.get(0);

            //get the weather description, icon text, and temperature from the API call
            apiSkyDescriptionText = item.getmSkyDescription();
            apiSkyIconText = item.getmSkyIcon();
            apiTempText = item.getmTemp();

            apiCallFinished = true;

            return null;
        }
    }
}
