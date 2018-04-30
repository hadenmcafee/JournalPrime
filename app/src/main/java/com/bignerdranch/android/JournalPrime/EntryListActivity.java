package com.bignerdranch.android.JournalPrime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

public class EntryListActivity extends SingleFragmentActivity implements EntryListFragment.Callbacks, EntryFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
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
}
