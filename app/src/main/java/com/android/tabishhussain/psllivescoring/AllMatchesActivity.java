package com.android.tabishhussain.psllivescoring;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.android.tabishhussain.psllivescoring.fragments.MainFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;


public class AllMatchesActivity extends AppCompatActivity {

    public static final String TAG = "All_MATCH_ACTIVITY_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_matches);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                    .replace(R.id.frame,new MainFragment()).commit();
        }
    }
}
