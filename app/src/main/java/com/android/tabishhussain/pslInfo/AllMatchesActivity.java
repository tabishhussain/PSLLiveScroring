package com.android.tabishhussain.pslInfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.tabishhussain.pslInfo.fragments.LiveScoringFragment;
import com.android.tabishhussain.pslInfo.fragments.PslScheduleFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AllMatchesActivity extends AppCompatActivity {

    public static final String JSON_RESPONSE = "JSON_RESPONSE";
    public static final String XML_RESPONSE = "XML_RESPONSE";

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    Toolbar mToolbar;
    LiveScoringFragment liveScoringFragment;
    SharedPreferences sharedPreferences;
    DrawerListAdapter mAdapter;
    List<DrawerItem> drawerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_matches);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitle("Live Scoring");
        mToolbar.setTitleTextColor(getColor(R.color.colorPrimaryDark));
        setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        int appLaunchCount = sharedPreferences.getInt(getString(R.string.app_launch), 0);
        if (appLaunchCount > 15) {
            Constants.shouldShowAds = true;
        } else {
            appLaunchCount++;
            sharedPreferences.edit().putInt(getString(R.string.app_launch), appLaunchCount).apply();
        }
        if (Constants.shouldShowAds) {
            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(getString(R.string.banner_ad_unit_id))
                    .build();
            mAdView.loadAd(adRequest);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        setDrawerProperties();
        liveScoringFragment = new LiveScoringFragment();
        if (savedInstanceState == null) {
            Random randomBackGround = new Random();
            int back = randomBackGround.nextInt(4);
            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);
            switch (back) {
                case 0:
                    frameLayout.setBackgroundResource(R.drawable.back1_blur);
                    break;
                case 1:
                    frameLayout.setBackgroundResource(R.drawable.back2);
                    break;
                case 2:
                    frameLayout.setBackgroundResource(R.drawable.back3);
                    break;
                case 3:
                    frameLayout.setBackgroundResource(R.drawable.back4);
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, liveScoringFragment).commit();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private void setDrawerProperties() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        drawerItems = new ArrayList<>();
        String[] array = getResources().getStringArray(R.array.filter);
        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                drawerItems.add(new DrawerItem(array[i], true));
            } else {
                drawerItems.add(new DrawerItem(array[i], false));
            }
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mAdapter = new DrawerListAdapter(drawerItems);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                Handler mHandler =  new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (pos) {
                            case 1:
                                startActivityForResult(new Intent(AllMatchesActivity.this, TeamsActivity.class), 0);
                                break;
                            case 2:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frame, new PslScheduleFragment()).commit();
                                break;
                            case 0:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frame, liveScoringFragment).commit();
                        }
                    }
                },250);
                view.setBackgroundColor(ContextCompat.getColor(AllMatchesActivity.this
                        , R.color.colorPrimary));
                ((TextView) view).setTextColor(ContextCompat.getColor(AllMatchesActivity.this
                        , R.color.colorPrimaryDark));
                for (int i = 0; i < parent.getChildCount(); i++) {
                    if (i != position) {
                        TextView v = (TextView) parent.getChildAt(i);
                        v.setBackgroundColor(ContextCompat.getColor(AllMatchesActivity.this
                                , R.color.colorPrimaryDark));
                        v.setTextColor(ContextCompat.getColor(AllMatchesActivity.this
                                , R.color.colorPrimary));
                    }
                }
                mDrawerLayout.closeDrawers();
            }
        });
    }

    public class DrawerListAdapter extends BaseAdapter {

        List<DrawerItem> items;

        public DrawerListAdapter(List<DrawerItem> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.drawer_list_item, null);
                DrawerItem drawerItem = items.get(position);
                if (drawerItem.isSelected) {
                    ((TextView) convertView).setText(drawerItem.value);
                    ((TextView) convertView).setTextColor(ContextCompat.getColor(AllMatchesActivity.this
                            , R.color.colorPrimaryDark));
                    convertView.setBackgroundColor(ContextCompat
                            .getColor(AllMatchesActivity.this, R.color.colorPrimary));

                } else {
                    ((TextView) convertView).setText(drawerItem.value);
                    ((TextView) convertView).setTextColor(ContextCompat.getColor(AllMatchesActivity.this
                            , R.color.colorPrimary));
                    convertView.setBackgroundColor(ContextCompat
                            .getColor(AllMatchesActivity.this, R.color.colorPrimaryDark));
                }
            }
            return convertView;
        }

        public void setData(List<DrawerItem> drawerItems) {
            items = drawerItems;
            notifyDataSetChanged();
        }
    }

    public class DrawerItem {
        public String value;
        public Boolean isSelected;

        public DrawerItem(String item, Boolean isSelected) {
            this.value = item;
            this.isSelected = isSelected;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(resultCode == RESULT_OK){
                    mDrawerList.setSelection(0);
                }
        }
    }
}
