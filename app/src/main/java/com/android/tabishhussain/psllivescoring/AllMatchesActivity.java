package com.android.tabishhussain.psllivescoring;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.tabishhussain.psllivescoring.fragments.MainFragment;


public class AllMatchesActivity extends AppCompatActivity {

    public static final String TAG = "All_MATCH_ACTIVITY_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_matches);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame,new MainFragment()).commit();
        }
    }
}
