package com.android.tabishhussain.psllivescoring.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.tabishhussain.psllivescoring.ApiManager.CurrentData;

/**
 * Created by tabish on 1/1/16.
 * A service written to update the score after every 30 sec
 */
public class ScoreUpdatingService extends Service {

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
                if (isNetworkAvailable()) {
                    CurrentData.loadData(mDataLoadListener);
                    mHandler.postDelayed(this, 30000);
                } else {
                    mDataLoadListener.onError();
                }
            }
        }, 30000);
        return START_STICKY;
    }

    public void setLoadListener(CurrentData.DataLoadListener listener) {
        mDataLoadListener = listener;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class MyBinder extends Binder {
        public ScoreUpdatingService getService() {
            return ScoreUpdatingService.this;
        }
    }
}
