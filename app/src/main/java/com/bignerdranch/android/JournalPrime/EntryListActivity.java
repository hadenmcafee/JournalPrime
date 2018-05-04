package com.bignerdranch.android.JournalPrime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class EntryListActivity extends SingleFragmentActivity implements EntryListFragment.Callbacks, EntryFragment.Callbacks{
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
    protected int getLayoutResId(){
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onEntrySelected(Entry entry){
        if(findViewById(R.id.detail_fragment_container) == null){
            Intent intent = EntryPagerActivity.newIntent(this,entry.getId());
            startActivity(intent);
        }else{
            Fragment newDetail = EntryFragment.newInstance(entry.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    public void onEntryUpdated(Entry entry){
        EntryListFragment listFragment = (EntryListFragment)
                getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
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
