package com.android.tabishhussain.psllivescoring.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.tabishhussain.psllivescoring.DataClasses.CurrentData;
import com.android.tabishhussain.psllivescoring.R;
import com.android.tabishhussain.psllivescoring.ScoreUpdatingService;
import com.android.tabishhussain.psllivescoring.adapters.ListAdapter;

import static com.android.tabishhussain.psllivescoring.R.layout.layout_fragment;

public class MainFragment extends ListFragment {

    ListAdapter adapter;
    ProgressBar progressBar;
    int spinnerSelection;
    String[] selectionArray;
    Spinner mSpinner;
    private boolean mServiceBound;
    SharedPreferences sharedPreferences;
    private CurrentData.DataLoadListener mDataLoadListener = new CurrentData.DataLoadListener() {
        @Override
        public void onLoad(CurrentData currentData) {
            adapter.setData(currentData);
            adapter.filterData(spinnerSelection);
            progressBar.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Toolbar mToolBar = (Toolbar) getActivity().findViewById(R.id.toolbar_actionbar);
        RelativeLayout rlToolBarMain = (RelativeLayout) mToolBar.findViewById(R.id.rlToolBarMain);
        selectionArray = new String[]{
                "All",
                "Live",
                "Others",
                "Ended",
                "International"
        };
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        spinnerSelection = sharedPreferences.getInt(getActivity().getString(R.string.key_spinner_selection), 0);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.row_spinner_item, selectionArray);
        mSpinner = new Spinner(getActivity());
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setSelection(spinnerSelection);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPreferences.edit().putInt(getActivity().getString(R.string.key_spinner_selection), position)
                        .apply();
                spinnerSelection = position;
                view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                adapter.filterData(spinnerSelection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rlToolBarMain.addView(mSpinner);
        return inflater.inflate(layout_fragment, null);
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
            ScoreUpdatingService mScoreUpdatingService = myBinder.getService();
            mScoreUpdatingService.setLoadListener(mDataLoadListener);
            mServiceBound = true;
        }
    };
}
