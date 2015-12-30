package com.android.tabishhussain.psllivescoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.android.tabishhussain.psllivescoring.DataClasses.CurrentData;


public class AllMatchesActivity extends AppCompatActivity {

    public static final String TAG = "All_MATCH_ACTIVITY_TAG";
    private CurrentData.DataLoadListener mDataLoadListener = new CurrentData.DataLoadListener() {
        @Override
        public void onLoad(CurrentData currentData) {
//            Log.d(TAG, currentData.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_matches);
        CurrentData.loadData(mDataLoadListener);
    }
}
