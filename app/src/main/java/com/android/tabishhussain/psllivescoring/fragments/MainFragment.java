package com.android.tabishhussain.psllivescoring.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.tabishhussain.psllivescoring.DataClasses.CurrentData;
import com.android.tabishhussain.psllivescoring.R;
import com.android.tabishhussain.psllivescoring.ScoreUpdatingService;
import com.android.tabishhussain.psllivescoring.adapters.ListAdapter;

/**
 * Created by tabish on 12/31/15.
 */
public class MainFragment extends ListFragment {

    ListAdapter adapter;
    ProgressBar progressBar;
    private boolean mServiceBound;
    private ScoreUpdatingService mScoreUpdatingService;
    private CurrentData.DataLoadListener mDataLoadListener = new CurrentData.DataLoadListener() {
        @Override
        public void onLoad(CurrentData currentData) {
            adapter.setData(currentData);
            progressBar.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        CurrentData.loadData(mDataLoadListener);
        adapter = new ListAdapter(getContext());
        getListView().setDividerHeight(0);
        setListAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), ScoreUpdatingService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mServiceBound) {
            getActivity().unbindService(mServiceConnection);
            mServiceBound = false;
            Intent intent = new Intent(getActivity(),
                    ScoreUpdatingService.class);
            getActivity().stopService(intent);
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ScoreUpdatingService.MyBinder myBinder = (ScoreUpdatingService.MyBinder) service;
            mScoreUpdatingService = myBinder.getService();
            mScoreUpdatingService.setLoadListener(mDataLoadListener);
            mServiceBound = true;
        }
    };
}
