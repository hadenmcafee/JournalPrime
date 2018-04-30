package com.bignerdranch.android.JournalPrime;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EntryFragment extends Fragment {

    private static final String TAG = "EntryFragment";
    private static final String ARG_ENTRY_ID = "entry_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_PHOTO = 2;

    //data variables
    private Entry mEntry;

    //view component variables
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private Spinner mLocationSpinner;
    private TextView mSkyDescriptionText;
    private TextView mTemperatureText;
    private EditText mEntryContentField;
    private Button mCancelButton;
    private Button mSaveButton;

    //image capture variables
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;

//    private CheckBox mSolvedCheckbox;
//    private RecyclerView mWeatherRecyclerView;
//    private List<DarkSkyItem> mItems = new ArrayList<>();

    public static EntryFragment newInstance(UUID entryID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ENTRY_ID, entryID);

        EntryFragment fragment = new EntryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID entryID = (UUID) getArguments().getSerializable(ARG_ENTRY_ID);
        //mEntry = EntryRepository.get(getActivity()).getEntry(entryID);    - old code, prior to API implementation
        mEntry = EntryRepository.get(getActivity()).getEntry(entryID);

        //grabbing photo file location
        mPhotoFile = EntryRepository.get(getActivity()).getPhotoFile(mEntry);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entry, container, false);

        //title field
        mTitleField = (EditText) v.findViewById(R.id.entry_title);
        mTitleField.setText(mEntry.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEntry.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //date button
        mDateButton = (Button) v.findViewById(R.id.entry_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mEntry.getDate());
                dialog.setTargetFragment(EntryFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        //time button //TODO
        mTimeButton = (Button) v.findViewById(R.id.entry_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mEntry.getTime());
                dialog.setTargetFragment(EntryFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        //location spinner //TODO
        mLocationSpinner = (Spinner) v.findViewById(R.id.entry_location);
        mLocationSpinner.setPrompt("Please Select a Location");

        //sky description //TODO
        mSkyDescriptionText = (EditText) v.findViewById(R.id.entry_sky);
        mSkyDescriptionText.setText(mEntry.getSkyDescription());
        mSkyDescriptionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEntry.setSkyDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //set sky description to sky at current location (Dark Sky API) OR in response to selected time?

        //temperature text //TODO
        mTemperatureText = (EditText) v.findViewById(R.id.entry_temp);
        mTemperatureText.setText(mEntry.getTemp());
        mTemperatureText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEntry.setTemp(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //set temperature to temp at current location (Dark Sky API)

        //entry content
        mEntryContentField = (EditText) v.findViewById(R.id.entry_content);
        mEntryContentField.setText(mEntry.getEntryContent());
        mEntryContentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEntry.setEntryContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //cancel button
        mCancelButton = (Button) v.findViewById(R.id.entry_cancelBtn);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return to previous screen, do not save data
            }

        });

        //save button
        mSaveButton = (Button) v.findViewById(R.id.entry_saveButton);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return to previous screen, save data
            }

        });

        /*mSolvedCheckbox = (CheckBox) v.findViewById(R.id.entry_solved);
        mSolvedCheckbox.setChecked(mEntry.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                mEntry.setSolved(isChecked);
            }
        });*/


        //Camera intent
        PackageManager packageManager = getActivity().getPackageManager();

        //Camera Button
        mPhotoButton = (ImageButton) v.findViewById(R.id.entry_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.bignerdranch.android.JournalPrime.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.entry_photo);
        updatePhotoView();

        return v;
    }

//    private class WeatherHolder extends RecyclerView.ViewHolder{
//        private TextView mSkyTextView;
//
//        public WeatherHolder(View itemView){
//            super(itemView);
//
//            mSkyTextView = (TextView) itemView;
//        }
//
//        public void bindDarkSkyItem(DarkSkyItem item){
//            mSkyTextView.setText(item.toString());
//        }
//    }

//    private class WeatherAdapter extends RecyclerView.Adapter<WeatherHolder>{
//        private List<DarkSkyItem> mDarkSkyItems;
//
//        public WeatherAdapter(List<DarkSkyItem> darkskyItems){
//            mDarkSkyItems = darkskyItems;
//        }
//
//        @Override
//        public WeatherHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
//            TextView textView = new TextView(getActivity());
//            return new WeatherHolder(textView);
//        }
//
//        @Override
//        public void onBindViewHolder(WeatherHolder weatherHolder, int position){
//            DarkSkyItem darkSkyItem = mDarkSkyItems.get(position);
//            weatherHolder.bindDarkSkyItem(darkSkyItem);
//        }
//
//        @Override
//        public int getItemCount(){
//            return mDarkSkyItems.size();
//        }
//    }

    @Override
    public void onPause() {
        super.onPause();

        EntryRepository.get(getActivity())
                .updateEntry(mEntry);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mEntry.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.bignerdranch.android.JournalPrime.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        } else if (requestCode == REQUEST_TIME) {
            Time time = (Time) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mEntry.setTime(time);
            updateTime();
        }
    }

    private void updateDate() {
        DateFormat formatter = new SimpleDateFormat("EEE, MM/dd/yyyy");
        mDateButton.setText(formatter.format(mEntry.getDate())); //mDateButton.setText(mEntry.getDate().toString());
    }

    private void updateTime() {
        DateFormat formatter = new SimpleDateFormat("hh:mm a");
        mTimeButton.setText(formatter.format(mEntry.getTime())); //mDateButton.setText(mEntry.getDate().toString());

    }
}
