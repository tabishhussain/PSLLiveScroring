package com.android.tabishhussain.psllivescoring.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.tabishhussain.psllivescoring.ApiManager.CurrentData;
import com.android.tabishhussain.psllivescoring.Constants;
import com.android.tabishhussain.psllivescoring.R;
import com.android.tabishhussain.psllivescoring.adapters.ListAdapter;
import com.android.tabishhussain.psllivescoring.services.ScoreUpdatingService;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import static com.android.tabishhussain.psllivescoring.R.layout.layout_fragment;

public class MainFragment extends ListFragment {

    ListAdapter adapter;
    InterstitialAd mInterstitialAd;
    ProgressBar progressBar;
    int drawerSelection;
    int adCount = 0;
    Spinner mSpinner;
    TextView title;
    int mSpinnerSelection = 0;
    private boolean mServiceBound;
    TextView errorView, clickHere;
    SharedPreferences sharedPreferences;
    private CurrentData.DataLoadListener mDataLoadListener = new CurrentData.DataLoadListener() {
        @Override
        public void onLoad(CurrentData currentData) {
            adapter.setData(currentData);
            drawerSelection = sharedPreferences.
                    getInt(getActivity().getString(R.string.key_drawer_selection), 0);
            adapter.TypeFilter(drawerSelection);
            adapter.dayFilter(mSpinnerSelection);
            mSpinner.setSelection(mSpinnerSelection);
            setTitle(drawerSelection);
            if (progressBar != null) {
                progressBar.setVisibility(View.INVISIBLE);
                errorView.setVisibility(View.INVISIBLE);
                clickHere.setVisibility(View.INVISIBLE);
            }
            errorView.setText(R.string.msg_nothing_match);
            getListView().setEmptyView(errorView);
            showAd();
        }

        @Override
        public void onError() {
            if (progressBar != null) {
                Snackbar snackbar = Snackbar
                        .make(progressBar, R.string.msg_connect_to_internet_for_update,
                                Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    };

    public void showAd(){
        if(mInterstitialAd.isLoaded() && adCount >= 6
                && Constants.shouldShowAds){
            mInterstitialAd.show();
            adCount = 0;
        } else {
            adCount++;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.
                registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Toolbar mToolBar = (Toolbar) getActivity().findViewById(R.id.toolbar_actionbar);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.row_spinner_item, getResources().getStringArray(R.array.Spinnerfilter));
        title = (TextView)mToolBar.findViewById(R.id.title1);
        mSpinner = (Spinner)mToolBar.findViewById(R.id.mSpinner);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setSelection(0);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                ((TextView) view).setGravity(Gravity.END);
                ((TextView) view).setTextColor(ContextCompat.
                        getColor(getActivity(), R.color.colorPrimaryDark));
                mSpinnerSelection = position;
                adapter.dayFilter(mSpinnerSelection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return inflater.inflate(layout_fragment, null);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equalsIgnoreCase(getActivity().getString(R.string.key_drawer_selection))){
                drawerSelection = sharedPreferences.
                        getInt(getActivity().getString(R.string.key_drawer_selection), 0);
                adapter.TypeFilter(drawerSelection);
                adapter.dayFilter(mSpinnerSelection);
                setTitle(drawerSelection);
            }
        }
    };

    public  void setTitle(int drawerSelection){
        switch (drawerSelection){
            case 0:
                title.setText("All Matches");
                break;
            case 1:
                title.setText("Live Matches");
                break;
            case 2:
                title.setText("International");
                break;
            case 3:
                title.setText("Domestic Matches");
                break;
            case 4:
                title.setText("Match Results");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        errorView = (TextView) view.findViewById(R.id.error_msg);
        clickHere = (TextView) view.findViewById(R.id.click_button);
        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 1);
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        clickHere.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        if (isNetworkAvailable()) {
            CurrentData.loadData(mDataLoadListener);
        } else {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    errorView.setText(getString(R.string.msg_no_connection));
                    errorView.setVisibility(View.VISIBLE);
                    clickHere.setVisibility(View.VISIBLE);
                }
            }, 3000);
        }
        adapter = new ListAdapter(getContext());
        getListView().setDividerHeight(0);
        setListAdapter(adapter);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                progressBar.setVisibility(View.VISIBLE);
                clickHere.setVisibility(View.INVISIBLE);
                errorView.setVisibility(View.INVISIBLE);
                if (isNetworkAvailable()) {
                    CurrentData.loadData(mDataLoadListener);
                } else {
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            errorView.setVisibility(View.VISIBLE);
                            clickHere.setVisibility(View.VISIBLE);
                        }
                    }, 3000);
                }
        }
    }
}
