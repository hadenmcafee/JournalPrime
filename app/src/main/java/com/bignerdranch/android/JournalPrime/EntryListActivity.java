package com.bignerdranch.android.JournalPrime;

import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

public class EntryListActivity extends SingleFragmentActivity
{
    private static String TAG = "EntryListActivity";
    private static boolean mLocPermissionDetermined = false;
    private static boolean mLocPermissionGranted = false;

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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0)
        {
            Log.d(TAG, "Request code = 0");

            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.d(TAG, "Permission granted");
                mLocPermissionGranted = true;
                mLocPermissionDetermined = true;
                Log.d(TAG, "PermissionDetermined flag set");
            }
            else
            {
                Log.d(TAG, "Permission not granted");
                mLocPermissionGranted = false;
                mLocPermissionDetermined = true;
                Log.d(TAG, "PermissionDetermined flag set");

            }
            return;
        }

        Log.d(TAG, "Request code != 0");

        return;
    }

    public static boolean isLocPermissionGranted() {
        return mLocPermissionGranted;
    }

    public static void setLocPermissionGranted(boolean flag) {
        mLocPermissionGranted = flag;
    }

    public static boolean isLocPermissionDetermined() {
        return mLocPermissionDetermined;
    }

    public static void  setLocPermissionDetermined(boolean flag) {
        mLocPermissionDetermined = flag;
    }
}
