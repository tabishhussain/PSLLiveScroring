package com.android.tabishhussain.psllivescoring.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

import com.android.tabishhussain.psllivescoring.DataClasses.CurrentData;
import com.android.tabishhussain.psllivescoring.adapters.ListAdapter;

/**
 * Created by tabish on 12/31/15.
 */
public class MainFragment extends ListFragment {

    ListAdapter adapter;
    private CurrentData.DataLoadListener mDataLoadListener = new CurrentData.DataLoadListener() {
        @Override
        public void onLoad(CurrentData currentData) {
            adapter.setData(currentData);
            setListShown(true);
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CurrentData.loadData(mDataLoadListener);
        adapter = new ListAdapter(getContext());
        getListView().setDividerHeight(0);
        setListAdapter(adapter);
    }
}
