package com.android.tabishhussain.psllivescoring;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.android.tabishhussain.psllivescoring.fragments.MainFragment;

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
