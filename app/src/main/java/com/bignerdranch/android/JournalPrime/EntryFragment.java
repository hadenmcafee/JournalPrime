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
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.widget.CompoundButton.*;

public class EntryFragment extends Fragment {

    private static final String TAG = "EntryFragment";
    private static final String ARG_ENTRY_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_PHOTO=2;

    private static final int REQUEST_DATE = 0;

    private Entry mEntry;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;
//    private RecyclerView mWeatherRecyclerView;
//    private List<DarkSkyItem> mItems = new ArrayList<>();

    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;

    public static EntryFragment newInstance(UUID entryID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ENTRY_ID, entryID);

        EntryFragment fragment = new EntryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void updatePhotoView(){
        if (mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID entryID = (UUID) getArguments().getSerializable(ARG_ENTRY_ID);
        mEntry = EntryRepository.get(getActivity()).getEntry(entryID);
        new FetchItemsTask().execute();

        //grabbing photo file location
        mPhotoFile = EntryRepository.get(getActivity()).getPhotoFile(mEntry);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entry, container, false);

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

        mPhotoButton = (ImageButton) v. findViewById(R.id.entry_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.bignerdranch.android.JournalPrime.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for(ResolveInfo activity : cameraActivities){
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
        }else if (requestCode == REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.bignerdranch.android.JournalPrime.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }

    private void updateDate() {
        DateFormat formatter = new SimpleDateFormat("EEE, MM/dd/yyyy");
        mDateButton.setText(formatter.format(mEntry.getDate())); //mDateButton.setText(mEntry.getDate().toString());
    }

//    Writing an AsyncTask
    private class FetchItemsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params){
            new DarkSkyFetchr().fetchItems();
            return null;
        }
    }
}
