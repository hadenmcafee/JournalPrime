package com.bignerdranch.android.JournalPrime;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.Fragment;

public class EntryListActivity extends SingleFragmentActivity {

    private boolean mLocPermissionGranted = false;

    @Override
    protected Fragment createFragment()
    {
        /*LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return new EntryListFragment(lm);*/
        return new EntryListFragment();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        //
        if(requestCode ==0)
        {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                mLocPermissionGranted = true;
            } else {
                mLocPermissionGranted = false;
            }
            return;
        }
    }

    public boolean getmLockPermissionGranted()
    {
        return mLocPermissionGranted;
    }
}
