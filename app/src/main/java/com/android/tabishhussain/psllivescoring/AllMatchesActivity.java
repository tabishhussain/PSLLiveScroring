package com.android.tabishhussain.psllivescoring;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.android.tabishhussain.psllivescoring.fragments.MainFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;


public class AllMatchesActivity extends AppCompatActivity {

    public static final String TAG = "All_MATCH_ACTIVITY_TAG";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    Toolbar mToolbar;
    MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_matches);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitle("Live Scoring");
        mToolbar.setTitleTextColor(getColor(R.color.colorPrimaryDark));
        setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        int appLaunchCount = sharedPreferences.getInt(getString(R.string.app_launch), 0);
        if(appLaunchCount > 15){
            Constants.shouldShowAds = true;
        } else {
            appLaunchCount++;
            sharedPreferences.edit().putInt(getString(R.string.app_launch), appLaunchCount).apply();
        }
        if(Constants.shouldShowAds) {
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
        mainFragment = new MainFragment();
        if(savedInstanceState == null){
            Random randomBackGround = new Random();
            int back = randomBackGround.nextInt(4);
            FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frame);
            switch (back){
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
                    .replace(R.id.frame,mainFragment).commit();
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
                R.string.navigation_drawer_open, R.string.navigation_drawer_close){
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
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.drawer_list_item, getResources().getStringArray(R.array.filter));
        mDrawerList.setAdapter(arrayAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
