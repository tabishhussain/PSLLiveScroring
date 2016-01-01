package com.android.tabishhussain.psllivescoring;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.tabishhussain.psllivescoring.DataClasses.CurrentData;

/**
 * Created by tabish on 1/1/16.
 */
public class ScoreUpdatingService extends Service {

    private static String LOG_TAG = "ScoreUpdatingService";
    private IBinder mBinder = new MyBinder();
    public CurrentData.DataLoadListener mDataLoadListener;
    Handler mHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CurrentData.loadData(mDataLoadListener);
                mHandler.postDelayed(this, 30000);
            }
        }, 30000);
        return START_STICKY;
    }

    public void setLoadListener(CurrentData.DataLoadListener listener) {
        mDataLoadListener = listener;
    }

    public class MyBinder extends Binder {
        public ScoreUpdatingService getService() {
            return ScoreUpdatingService.this;
        }
    }
}
